// MongoDB Cleanup Script
// Este script limpa dados inconsistentes que podem causar erros de conversão UUID

print("🧹 Iniciando limpeza do MongoDB...");

// Conectar ao banco de dados
use tech_challenge_restaurants;

print("📊 Verificando estado atual do banco...");

// Verificar coleções existentes
var collections = db.getCollectionNames();
print("Coleções encontradas: " + collections.join(", "));

// Função para verificar se uma string é um UUID válido
function isValidUUID(str) {
    if (typeof str !== 'string') return false;
    var uuidRegex = /^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/i;
    return uuidRegex.test(str);
}

// Função para gerar UUID v4
function generateUUID() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
    });
}

// 1. Limpar e corrigir kitchen_types
print("\n🍳 Verificando kitchen_types...");

if (collections.includes("kitchen_types")) {
    var kitchenTypes = db.kitchen_types.find().toArray();
    print("Kitchen types encontrados: " + kitchenTypes.length);
    
    var invalidKitchenTypes = [];
    var validKitchenTypes = [];
    
    kitchenTypes.forEach(function(kt) {
        if (!kt._id || !isValidUUID(kt._id.toString())) {
            invalidKitchenTypes.push(kt);
        } else {
            validKitchenTypes.push(kt);
        }
    });
    
    print("Kitchen types válidos: " + validKitchenTypes.length);
    print("Kitchen types inválidos: " + invalidKitchenTypes.length);
    
    // Corrigir kitchen types inválidos
    if (invalidKitchenTypes.length > 0) {
        print("🔧 Corrigindo kitchen types inválidos...");
        
        invalidKitchenTypes.forEach(function(kt) {
            var newId = generateUUID();
            var newKitchenType = {
                _id: newId,
                name: kt.name || "Tipo Desconhecido",
                description: kt.description || "Descrição não disponível",
                createdAt: kt.createdAt || new Date(),
                lastUpdate: new Date()
            };
            
            // Remover o documento inválido
            db.kitchen_types.deleteOne({_id: kt._id});
            
            // Inserir o documento corrigido
            db.kitchen_types.insertOne(newKitchenType);
            
            print("✅ Corrigido kitchen type: " + kt.name + " (novo ID: " + newId + ")");
        });
    }
} else {
    print("⚠️ Coleção kitchen_types não encontrada. Criando kitchen types padrão...");
    
    var defaultKitchenTypes = [
        {
            _id: generateUUID(),
            name: "Brasileira",
            description: "Culinária tradicional brasileira",
            createdAt: new Date(),
            lastUpdate: new Date()
        },
        {
            _id: generateUUID(),
            name: "Italiana",
            description: "Culinária tradicional italiana",
            createdAt: new Date(),
            lastUpdate: new Date()
        },
        {
            _id: generateUUID(),
            name: "Japonesa",
            description: "Culinária japonesa autêntica",
            createdAt: new Date(),
            lastUpdate: new Date()
        }
    ];
    
    db.kitchen_types.insertMany(defaultKitchenTypes);
    print("✅ Kitchen types padrão criados: " + defaultKitchenTypes.length);
}

// 2. Limpar e corrigir restaurants
print("\n🏪 Verificando restaurants...");

if (collections.includes("restaurants")) {
    var restaurants = db.restaurants.find().toArray();
    print("Restaurantes encontrados: " + restaurants.length);
    
    var invalidRestaurants = [];
    var validRestaurants = [];
    
    restaurants.forEach(function(restaurant) {
        var isValid = true;
        var issues = [];
        
        // Verificar ID do restaurante
        if (!restaurant._id || !isValidUUID(restaurant._id.toString())) {
            isValid = false;
            issues.push("ID inválido");
        }
        
        // Verificar kitchenType
        if (restaurant.kitchenType && restaurant.kitchenType.id && !isValidUUID(restaurant.kitchenType.id.toString())) {
            isValid = false;
            issues.push("Kitchen type ID inválido");
        }
        
        // Verificar menu categories
        if (restaurant.menu && Array.isArray(restaurant.menu)) {
            restaurant.menu.forEach(function(category, categoryIndex) {
                if (!category.id || !isValidUUID(category.id.toString())) {
                    isValid = false;
                    issues.push("Menu category ID inválido no índice " + categoryIndex);
                }
                
                // Verificar menu items
                if (category.items && Array.isArray(category.items)) {
                    category.items.forEach(function(item, itemIndex) {
                        if (!item.id || !isValidUUID(item.id.toString())) {
                            isValid = false;
                            issues.push("Menu item ID inválido no índice " + categoryIndex + "." + itemIndex);
                        }
                    });
                }
            });
        }
        
        if (isValid) {
            validRestaurants.push(restaurant);
        } else {
            invalidRestaurants.push({restaurant: restaurant, issues: issues});
        }
    });
    
    print("Restaurantes válidos: " + validRestaurants.length);
    print("Restaurantes inválidos: " + invalidRestaurants.length);
    
    // Corrigir restaurantes inválidos
    if (invalidRestaurants.length > 0) {
        print("🔧 Corrigindo restaurantes inválidos...");
        
        invalidRestaurants.forEach(function(invalid) {
            var restaurant = invalid.restaurant;
            var issues = invalid.issues;
            
            print("Corrigindo restaurante: " + (restaurant.name || "Sem nome") + " - Issues: " + issues.join(", "));
            
            // Corrigir ID do restaurante
            if (!restaurant._id || !isValidUUID(restaurant._id.toString())) {
                var oldId = restaurant._id;
                restaurant._id = generateUUID();
                print("  ✅ ID corrigido: " + oldId + " -> " + restaurant._id);
            }
            
            // Corrigir kitchenType
            if (restaurant.kitchenType) {
                if (!restaurant.kitchenType.id || !isValidUUID(restaurant.kitchenType.id.toString())) {
                    // Buscar um kitchen type válido ou usar o primeiro disponível
                    var validKitchenType = db.kitchen_types.findOne();
                    if (validKitchenType) {
                        restaurant.kitchenType.id = validKitchenType._id;
                        restaurant.kitchenType.name = validKitchenType.name;
                        print("  ✅ Kitchen type corrigido para: " + validKitchenType.name);
                    }
                }
            }
            
            // Corrigir menu categories e items
            if (restaurant.menu && Array.isArray(restaurant.menu)) {
                restaurant.menu.forEach(function(category) {
                    if (!category.id || !isValidUUID(category.id.toString())) {
                        category.id = generateUUID();
                        print("  ✅ Menu category ID corrigido: " + category.name);
                    }
                    
                    if (category.items && Array.isArray(category.items)) {
                        category.items.forEach(function(item) {
                            if (!item.id || !isValidUUID(item.id.toString())) {
                                item.id = generateUUID();
                                print("  ✅ Menu item ID corrigido: " + item.name);
                            }
                        });
                    }
                });
            }
            
            // Atualizar timestamps
            restaurant.lastUpdate = new Date();
            if (!restaurant.createdAt) {
                restaurant.createdAt = new Date();
            }
            
            // Salvar o restaurante corrigido
            db.restaurants.replaceOne({_id: invalid.restaurant._id}, restaurant, {upsert: true});
        });
    }
}

// 3. Criar índices necessários
print("\n📊 Criando índices...");

// Índices para kitchen_types
db.kitchen_types.createIndex({"name": 1}, {unique: true});
print("✅ Índice único criado para kitchen_types.name");

// Índices para restaurants
db.restaurants.createIndex({"name": 1});
db.restaurants.createIndex({"kitchenType.id": 1});
db.restaurants.createIndex({"kitchenType.name": 1});
print("✅ Índices criados para restaurants");

// 4. Verificação final
print("\n🔍 Verificação final...");

var finalKitchenTypesCount = db.kitchen_types.countDocuments();
var finalRestaurantsCount = db.restaurants.countDocuments();

print("Kitchen types finais: " + finalKitchenTypesCount);
print("Restaurantes finais: " + finalRestaurantsCount);

// Verificar se ainda há UUIDs inválidos
var invalidUUIDs = db.kitchen_types.find({
    "_id": {$not: /^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/i}
}).count();

print("Kitchen types com UUID inválido: " + invalidUUIDs);

if (invalidUUIDs === 0) {
    print("✅ Todos os UUIDs estão válidos!");
} else {
    print("⚠️ Ainda existem UUIDs inválidos. Execute o script novamente.");
}

print("\n🎉 Limpeza concluída!");
print("💡 Dica: Reinicie a aplicação Spring Boot para aplicar as mudanças.");
