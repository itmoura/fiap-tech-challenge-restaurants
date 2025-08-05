# 🔧 Troubleshooting - Erro de UUID

## ❌ Problema

Você está recebendo o erro:
```
java.lang.IllegalArgumentException: Invalid UUID string: Brasileira
```

## 🔍 Causa do Problema

Este erro ocorre quando o MongoDB contém dados que não estão no formato UUID correto, mas o sistema está tentando convertê-los para UUID. Isso pode acontecer por:

1. **Dados legados** inseridos antes da implementação de UUIDs
2. **Inserção manual** de dados no MongoDB
3. **Migração** de dados de outro sistema
4. **Conversores MongoDB** tentando converter strings não-UUID

## 🛠️ Soluções

### Solução 1: Limpeza Automática do Banco (Recomendada)

Execute o script de limpeza que criamos:

```bash
# Executar limpeza automática
./scripts/cleanup-database.sh
```

Este script irá:
- ✅ Fazer backup dos dados atuais
- ✅ Corrigir UUIDs inválidos
- ✅ Criar dados padrão se necessário
- ✅ Verificar integridade dos dados

### Solução 2: Limpeza Manual do MongoDB

Se preferir fazer manualmente:

```bash
# Conectar ao MongoDB
mongosh

# Usar o banco de dados
use tech_challenge_restaurants

# Verificar dados problemáticos
db.kitchen_types.find()

# Remover dados inválidos
db.kitchen_types.deleteMany({})
db.restaurants.deleteMany({})

# Criar kitchen types válidos
db.kitchen_types.insertMany([
  {
    "_id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "Brasileira",
    "description": "Culinária tradicional brasileira",
    "createdAt": new Date(),
    "lastUpdate": new Date()
  },
  {
    "_id": "550e8400-e29b-41d4-a716-446655440001", 
    "name": "Italiana",
    "description": "Culinária tradicional italiana",
    "createdAt": new Date(),
    "lastUpdate": new Date()
  }
])
```

### Solução 3: Configuração MongoDB Simplificada

Se o problema persistir, use a configuração simplificada:

1. **Renomeie** `MongoConfig.java` para `MongoConfig.java.backup`
2. **Renomeie** `MongoConfigSimple.java` para `MongoConfig.java`
3. **Reinicie** a aplicação

```bash
cd src/main/java/com/fiap/itmoura/tech_challenge_restaurant/infrastructure/
mv MongoConfig.java MongoConfig.java.backup
mv MongoConfigSimple.java MongoConfig.java
```

### Solução 4: Limpar Completamente o Banco

Se nada mais funcionar:

```bash
# Parar a aplicação
docker-compose down

# Remover volumes do MongoDB
docker-compose down -v

# Reiniciar tudo
docker-compose up -d
```

## 🔍 Verificação

Após aplicar qualquer solução, verifique se funcionou:

### 1. Testar Conexão

```bash
curl http://localhost:8081/actuator/health
```

Deve retornar:
```json
{
  "status": "UP",
  "components": {
    "mongo": {
      "status": "UP"
    }
  }
}
```

### 2. Testar Kitchen Types

```bash
curl http://localhost:8081/api/kitchen-types
```

Deve retornar uma lista de kitchen types válidos.

### 3. Criar Kitchen Type

```bash
curl -X POST "http://localhost:8081/api/kitchen-types" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Japonesa",
    "description": "Culinária japonesa autêntica"
  }'
```

## 🚨 Prevenção

Para evitar este problema no futuro:

### 1. Sempre Use UUIDs Válidos

```java
// ✅ Correto
UUID id = UUID.randomUUID();

// ❌ Incorreto
String id = "Brasileira"; // Não é UUID!
```

### 2. Validação na Entrada

```java
public void validateUUID(String uuidString) {
    try {
        UUID.fromString(uuidString);
    } catch (IllegalArgumentException e) {
        throw new BadRequestException("Invalid UUID format: " + uuidString);
    }
}
```

### 3. Testes de Integração

```java
@Test
void shouldNotAcceptInvalidUUID() {
    // Testar que UUIDs inválidos são rejeitados
    assertThrows(BadRequestException.class, () -> {
        service.findById("Brasileira");
    });
}
```

## 📊 Estrutura Correta dos Dados

### Kitchen Type Correto

```json
{
  "_id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Brasileira",
  "description": "Culinária tradicional brasileira",
  "createdAt": "2024-08-05T16:00:00Z",
  "lastUpdate": "2024-08-05T16:00:00Z"
}
```

### Restaurant Correto

```json
{
  "_id": "550e8400-e29b-41d4-a716-446655440001",
  "name": "Restaurante Exemplo",
  "kitchenType": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "Brasileira"
  },
  "menu": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440002",
      "name": "Entradas",
      "items": [
        {
          "id": "550e8400-e29b-41d4-a716-446655440003",
          "name": "Pão de Açúcar",
          "price": 15.90
        }
      ]
    }
  ]
}
```

## 🆘 Se Nada Funcionar

1. **Verifique os logs** da aplicação para mais detalhes
2. **Execute com debug**:
   ```bash
   ./gradlew bootRun --debug
   ```
3. **Abra uma issue** no GitHub com:
   - Logs completos do erro
   - Dados do MongoDB (`db.kitchen_types.find()`)
   - Versão do Java e MongoDB
   - Passos que levaram ao erro

## 📞 Suporte

- **GitHub Issues**: [Abrir issue](https://github.com/itmoura/fiap-tech-challenge-restaurants/issues)
- **Documentação**: [Ver docs completas](https://itmoura.github.io/fiap-tech-challenge-restaurants/)

---

## 🎯 Resumo Rápido

Para resolver rapidamente:

```bash
# 1. Execute a limpeza automática
./scripts/cleanup-database.sh

# 2. Reinicie a aplicação
docker-compose restart app
# ou
./gradlew bootRun

# 3. Teste se funcionou
curl http://localhost:8081/api/kitchen-types
```

Se ainda não funcionar, use a **Solução 4** (limpar completamente o banco).
