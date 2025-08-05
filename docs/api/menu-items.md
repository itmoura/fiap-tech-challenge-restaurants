# 🍽️ Menu Items API

API para gerenciamento de itens do menu com preços, descrições e informações detalhadas. Os itens pertencem a categorias específicas do menu.

## 📋 Base Path

```
/api/restaurants/{restaurantId}/menu/{menuId}/items
```

## 📊 Modelo de Dados

### MenuItemRequest

```json
{
  "name": "string",
  "description": "string",
  "price": 0.00,
  "available": true,
  "preparationTime": 15,
  "allergens": ["string"],
  "tags": ["string"]
}
```

| Campo | Tipo | Obrigatório | Descrição |
|-------|------|-------------|-----------|
| `name` | string | ✅ | Nome do item |
| `description` | string | ❌ | Descrição detalhada |
| `price` | decimal | ✅ | Preço do item |
| `available` | boolean | ❌ | Se o item está disponível (padrão: true) |
| `preparationTime` | integer | ❌ | Tempo de preparo em minutos |
| `allergens` | array | ❌ | Lista de alérgenos |
| `tags` | array | ❌ | Tags do item (vegetariano, picante, etc.) |

### MenuItemResponse

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Spaghetti Carbonara",
  "description": "Massa italiana com bacon, ovos e queijo parmesão",
  "price": 45.90,
  "available": true,
  "preparationTime": 20,
  "allergens": ["glúten", "lactose", "ovos"],
  "tags": ["tradicional", "italiana"],
  "createdAt": "2024-08-05T16:00:00Z",
  "updatedAt": "2024-08-05T16:00:00Z"
}
```

### MenuItemWithContextDTO

Usado para busca direta por ID do item, incluindo contexto completo:

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Spaghetti Carbonara",
  "description": "Massa italiana com bacon, ovos e queijo parmesão",
  "price": 45.90,
  "available": true,
  "restaurant": {
    "id": "550e8400-e29b-41d4-a716-446655440001",
    "name": "Bella Italia",
    "address": "Rua Augusta, 123"
  },
  "category": {
    "id": "550e8400-e29b-41d4-a716-446655440002",
    "name": "Pratos Principais",
    "description": "Pratos principais da casa"
  },
  "createdAt": "2024-08-05T16:00:00Z",
  "updatedAt": "2024-08-05T16:00:00Z"
}
```

## 🔗 Endpoints

### 1. Criar Item do Menu

Adiciona um novo item a uma categoria específica do menu.

```http
POST /api/restaurants/{restaurantId}/menu/{menuId}/items
```

#### Parâmetros

| Parâmetro | Tipo | Localização | Descrição |
|-----------|------|-------------|-----------|
| `restaurantId` | UUID | Path | ID do restaurante |
| `menuId` | UUID | Path | ID da categoria do menu |

#### Request

```bash
curl -X POST "http://localhost:8081/api/restaurants/550e8400-e29b-41d4-a716-446655440000/menu/550e8400-e29b-41d4-a716-446655440001/items" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Spaghetti Carbonara",
    "description": "Massa italiana tradicional com bacon, ovos, queijo parmesão e pimenta preta",
    "price": 45.90,
    "available": true,
    "preparationTime": 20,
    "allergens": ["glúten", "lactose", "ovos"],
    "tags": ["tradicional", "italiana", "massa"]
  }'
```

#### Response

**Status: 201 Created**

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440002",
  "name": "Spaghetti Carbonara",
  "description": "Massa italiana tradicional com bacon, ovos, queijo parmesão e pimenta preta",
  "price": 45.90,
  "available": true,
  "preparationTime": 20,
  "allergens": ["glúten", "lactose", "ovos"],
  "tags": ["tradicional", "italiana", "massa"],
  "createdAt": "2024-08-05T16:00:00Z",
  "updatedAt": "2024-08-05T16:00:00Z"
}
```

#### Possíveis Erros

| Status | Descrição |
|--------|-----------|
| **400** | Dados inválidos |
| **404** | Restaurante ou categoria não encontrada |

### 2. Buscar Item por ID

Retorna um item específico com contexto completo (restaurante e categoria).

```http
GET /api/menu-items/{itemId}
```

#### Parâmetros

| Parâmetro | Tipo | Localização | Descrição |
|-----------|------|-------------|-----------|
| `itemId` | UUID | Path | ID do item do menu |

#### Request

```bash
curl -X GET "http://localhost:8081/api/menu-items/550e8400-e29b-41d4-a716-446655440002" \
  -H "Accept: application/json"
```

#### Response

**Status: 200 OK**

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440002",
  "name": "Spaghetti Carbonara",
  "description": "Massa italiana tradicional com bacon, ovos, queijo parmesão e pimenta preta",
  "price": 45.90,
  "available": true,
  "preparationTime": 20,
  "allergens": ["glúten", "lactose", "ovos"],
  "tags": ["tradicional", "italiana", "massa"],
  "restaurant": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "Bella Italia",
    "address": "Rua Augusta, 123 - São Paulo, SP",
    "phone": "(11) 99999-9999"
  },
  "category": {
    "id": "550e8400-e29b-41d4-a716-446655440001",
    "name": "Pratos Principais",
    "description": "Pratos principais da casa"
  },
  "createdAt": "2024-08-05T16:00:00Z",
  "updatedAt": "2024-08-05T16:00:00Z"
}
```

### 3. Atualizar Item do Menu

Atualiza um item existente do menu.

```http
PUT /api/restaurants/{restaurantId}/menu/{menuId}/items/{itemId}
```

#### Request

```bash
curl -X PUT "http://localhost:8081/api/restaurants/550e8400-e29b-41d4-a716-446655440000/menu/550e8400-e29b-41d4-a716-446655440001/items/550e8400-e29b-41d4-a716-446655440002" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Spaghetti Carbonara Premium",
    "description": "Massa italiana artesanal com guanciale, ovos orgânicos e parmigiano reggiano 24 meses",
    "price": 52.90,
    "available": true,
    "preparationTime": 25,
    "allergens": ["glúten", "lactose", "ovos"],
    "tags": ["premium", "italiana", "massa", "artesanal"]
  }'
```

#### Response

**Status: 200 OK**

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440002",
  "name": "Spaghetti Carbonara Premium",
  "description": "Massa italiana artesanal com guanciale, ovos orgânicos e parmigiano reggiano 24 meses",
  "price": 52.90,
  "available": true,
  "preparationTime": 25,
  "allergens": ["glúten", "lactose", "ovos"],
  "tags": ["premium", "italiana", "massa", "artesanal"],
  "createdAt": "2024-08-05T16:00:00Z",
  "updatedAt": "2024-08-05T16:30:00Z"
}
```

### 4. Excluir Item do Menu

Remove um item do menu.

```http
DELETE /api/restaurants/{restaurantId}/menu/{menuId}/items/{itemId}
```

#### Request

```bash
curl -X DELETE "http://localhost:8081/api/restaurants/550e8400-e29b-41d4-a716-446655440000/menu/550e8400-e29b-41d4-a716-446655440001/items/550e8400-e29b-41d4-a716-446655440002"
```

#### Response

**Status: 204 No Content**

## 📝 Exemplos Práticos

### Menu Italiano Completo

```bash
#!/bin/bash

RESTAURANT_ID="550e8400-e29b-41d4-a716-446655440000"
ENTRADAS_ID="550e8400-e29b-41d4-a716-446655440001"
PRATOS_ID="550e8400-e29b-41d4-a716-446655440002"
SOBREMESAS_ID="550e8400-e29b-41d4-a716-446655440003"

# ENTRADAS
echo "Criando entradas..."

curl -X POST "http://localhost:8081/api/restaurants/$RESTAURANT_ID/menu/$ENTRADAS_ID/items" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Bruschetta Classica",
    "description": "Pão italiano tostado com tomate, manjericão e azeite extra virgem",
    "price": 18.90,
    "preparationTime": 10,
    "allergens": ["glúten"],
    "tags": ["vegetariano", "tradicional"]
  }'

curl -X POST "http://localhost:8081/api/restaurants/$RESTAURANT_ID/menu/$ENTRADAS_ID/items" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Antipasto della Casa",
    "description": "Seleção de frios, queijos, azeitonas e vegetais marinados",
    "price": 32.90,
    "preparationTime": 5,
    "allergens": ["lactose"],
    "tags": ["tradicional", "para-compartilhar"]
  }'

curl -X POST "http://localhost:8081/api/restaurants/$RESTAURANT_ID/menu/$ENTRADAS_ID/items" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Carpaccio di Manzo",
    "description": "Fatias finas de carne bovina crua com rúcula, parmesão e alcaparras",
    "price": 38.90,
    "preparationTime": 8,
    "allergens": ["lactose"],
    "tags": ["premium", "carne-crua"]
  }'

# PRATOS PRINCIPAIS
echo "Criando pratos principais..."

curl -X POST "http://localhost:8081/api/restaurants/$RESTAURANT_ID/menu/$PRATOS_ID/items" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Spaghetti Carbonara",
    "description": "Massa com bacon, ovos, queijo parmesão e pimenta preta",
    "price": 45.90,
    "preparationTime": 20,
    "allergens": ["glúten", "lactose", "ovos"],
    "tags": ["tradicional", "massa"]
  }'

curl -X POST "http://localhost:8081/api/restaurants/$RESTAURANT_ID/menu/$PRATOS_ID/items" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Risotto ai Funghi Porcini",
    "description": "Risotto cremoso com cogumelos porcini e trufa branca",
    "price": 58.90,
    "preparationTime": 30,
    "allergens": ["lactose"],
    "tags": ["premium", "vegetariano", "risotto"]
  }'

curl -X POST "http://localhost:8081/api/restaurants/$RESTAURANT_ID/menu/$PRATOS_ID/items" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Osso Buco alla Milanese",
    "description": "Jarrete de vitela cozido lentamente com vegetais e vinho branco",
    "price": 78.90,
    "preparationTime": 45,
    "allergens": [],
    "tags": ["premium", "carne", "tradicional"]
  }'

# SOBREMESAS
echo "Criando sobremesas..."

curl -X POST "http://localhost:8081/api/restaurants/$RESTAURANT_ID/menu/$SOBREMESAS_ID/items" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Tiramisu",
    "description": "Sobremesa italiana com café, mascarpone e cacau",
    "price": 22.90,
    "preparationTime": 5,
    "allergens": ["lactose", "ovos", "glúten"],
    "tags": ["tradicional", "café"]
  }'

curl -X POST "http://localhost:8081/api/restaurants/$RESTAURANT_ID/menu/$SOBREMESAS_ID/items" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Panna Cotta ai Frutti di Bosco",
    "description": "Creme italiano com calda de frutas vermelhas",
    "price": 19.90,
    "preparationTime": 5,
    "allergens": ["lactose"],
    "tags": ["cremoso", "frutas"]
  }'

echo "Menu completo criado!"
```

### Itens com Características Especiais

```bash
# Item vegano
curl -X POST "http://localhost:8081/api/restaurants/$RESTAURANT_ID/menu/$PRATOS_ID/items" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Pasta Arrabbiata Vegana",
    "description": "Penne com molho de tomate picante, alho e pimenta",
    "price": 38.90,
    "preparationTime": 15,
    "allergens": ["glúten"],
    "tags": ["vegano", "picante", "sem-lactose"]
  }'

# Item sem glúten
curl -X POST "http://localhost:8081/api/restaurants/$RESTAURANT_ID/menu/$PRATOS_ID/items" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Risotto Senza Glutine",
    "description": "Risotto de camarão com aspargos (sem glúten)",
    "price": 52.90,
    "preparationTime": 25,
    "allergens": ["lactose", "crustáceos"],
    "tags": ["sem-glúten", "frutos-do-mar"]
  }'

# Item temporariamente indisponível
curl -X POST "http://localhost:8081/api/restaurants/$RESTAURANT_ID/menu/$PRATOS_ID/items" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Linguine alle Vongole",
    "description": "Massa com vongoles frescas (sujeito à disponibilidade)",
    "price": 65.90,
    "available": false,
    "preparationTime": 25,
    "allergens": ["glúten", "moluscos"],
    "tags": ["frutos-do-mar", "sazonal"]
  }'
```

## 🔍 Filtros e Busca (Futuro)

### Por Características

```bash
# Itens vegetarianos
GET /api/restaurants/{restaurantId}/menu/items?tags=vegetariano

# Itens sem glúten
GET /api/restaurants/{restaurantId}/menu/items?allergens=!glúten

# Itens por faixa de preço
GET /api/restaurants/{restaurantId}/menu/items?minPrice=20&maxPrice=50

# Itens disponíveis
GET /api/restaurants/{restaurantId}/menu/items?available=true
```

### Por Tempo de Preparo

```bash
# Itens rápidos (até 15 minutos)
GET /api/restaurants/{restaurantId}/menu/items?maxPreparationTime=15

# Itens que demoram mais (acima de 30 minutos)
GET /api/restaurants/{restaurantId}/menu/items?minPreparationTime=30
```

## 🧪 Validações

### Campos Obrigatórios

```bash
# Nome vazio
curl -X POST "http://localhost:8081/api/restaurants/$RESTAURANT_ID/menu/$PRATOS_ID/items" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "",
    "price": 25.90
  }'

# Resposta: 400 Bad Request
{
  "timestamp": "2024-08-05T16:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "details": [
    {
      "field": "name",
      "message": "Nome do item é obrigatório"
    }
  ]
}
```

### Preço Válido

```bash
# Preço negativo
curl -X POST "http://localhost:8081/api/restaurants/$RESTAURANT_ID/menu/$PRATOS_ID/items" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Item Teste",
    "price": -10.00
  }'

# Resposta: 400 Bad Request
{
  "details": [
    {
      "field": "price",
      "message": "Preço deve ser maior que zero"
    }
  ]
}
```

### Tempo de Preparo

```bash
# Tempo negativo
curl -X POST "http://localhost:8081/api/restaurants/$RESTAURANT_ID/menu/$PRATOS_ID/items" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Item Teste",
    "price": 25.90,
    "preparationTime": -5
  }'

# Resposta: 400 Bad Request
{
  "details": [
    {
      "field": "preparationTime",
      "message": "Tempo de preparo deve ser positivo"
    }
  ]
}
```

## 📊 Casos de Uso Avançados

### Gestão de Disponibilidade

```bash
# Marcar item como indisponível
curl -X PUT "http://localhost:8081/api/restaurants/$RESTAURANT_ID/menu/$PRATOS_ID/items/$ITEM_ID" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Linguine alle Vongole",
    "description": "Massa com vongoles frescas (temporariamente indisponível)",
    "price": 65.90,
    "available": false,
    "preparationTime": 25,
    "allergens": ["glúten", "moluscos"],
    "tags": ["frutos-do-mar", "indisponível"]
  }'

# Reativar item
curl -X PUT "http://localhost:8081/api/restaurants/$RESTAURANT_ID/menu/$PRATOS_ID/items/$ITEM_ID" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Linguine alle Vongole",
    "description": "Massa com vongoles frescas",
    "price": 65.90,
    "available": true,
    "preparationTime": 25,
    "allergens": ["glúten", "moluscos"],
    "tags": ["frutos-do-mar", "disponível"]
  }'
```

### Atualização de Preços

```bash
#!/bin/bash

# Script para atualizar preços com aumento de 10%
ITEMS=(
  "550e8400-e29b-41d4-a716-446655440001"
  "550e8400-e29b-41d4-a716-446655440002"
  "550e8400-e29b-41d4-a716-446655440003"
)

for ITEM_ID in "${ITEMS[@]}"; do
  # Buscar item atual
  CURRENT_ITEM=$(curl -s "http://localhost:8081/api/menu-items/$ITEM_ID")
  
  # Calcular novo preço (10% de aumento)
  CURRENT_PRICE=$(echo $CURRENT_ITEM | jq -r '.price')
  NEW_PRICE=$(echo "$CURRENT_PRICE * 1.1" | bc -l)
  
  # Atualizar item
  curl -X PUT "http://localhost:8081/api/restaurants/$RESTAURANT_ID/menu/$MENU_ID/items/$ITEM_ID" \
    -H "Content-Type: application/json" \
    -d "$(echo $CURRENT_ITEM | jq --arg price "$NEW_PRICE" '.price = ($price | tonumber)')"
  
  echo "Item $ITEM_ID atualizado: $CURRENT_PRICE -> $NEW_PRICE"
done
```

### Menu Sazonal

```bash
# Adicionar itens de verão
curl -X POST "http://localhost:8081/api/restaurants/$RESTAURANT_ID/menu/$PRATOS_ID/items" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Insalata di Mare",
    "description": "Salada refrescante com frutos do mar (disponível apenas no verão)",
    "price": 42.90,
    "preparationTime": 15,
    "allergens": ["crustáceos", "moluscos"],
    "tags": ["verão", "fresco", "sazonal", "frutos-do-mar"]
  }'

# Adicionar itens de inverno
curl -X POST "http://localhost:8081/api/restaurants/$RESTAURANT_ID/menu/$PRATOS_ID/items" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Minestrone della Nonna",
    "description": "Sopa italiana tradicional com vegetais e feijão (disponível no inverno)",
    "price": 28.90,
    "preparationTime": 20,
    "allergens": [],
    "tags": ["inverno", "quente", "sazonal", "vegetariano", "sopa"]
  }'
```

## 🚀 Próximos Passos

- [Menu Categories API](menu-categories.md) - Gerenciar categorias do menu
- [Restaurants API](restaurants.md) - Gerenciar restaurantes
- [Códigos de Status](status-codes.md) - Referência completa de códigos HTTP
