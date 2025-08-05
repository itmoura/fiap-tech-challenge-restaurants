# 📋 Menu Categories API

API para gerenciamento de categorias do menu (Entradas, Pratos Principais, Sobremesas, etc.). As categorias organizam os itens do menu de forma hierárquica.

## 📋 Base Path

```
/api/restaurants/{restaurantId}/menu
```

## 📊 Modelo de Dados

### MenuCategoryRequest

```json
{
  "name": "string",
  "description": "string"
}
```

| Campo | Tipo | Obrigatório | Descrição |
|-------|------|-------------|-----------|
| `name` | string | ✅ | Nome da categoria |
| `description` | string | ❌ | Descrição da categoria |

### MenuCategoryResponse

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Entradas",
  "description": "Pratos para começar a refeição",
  "items": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440001",
      "name": "Bruschetta",
      "description": "Pão italiano com tomate e manjericão",
      "price": 15.90,
      "createdAt": "2024-08-05T16:10:00Z",
      "updatedAt": "2024-08-05T16:10:00Z"
    }
  ],
  "createdAt": "2024-08-05T16:00:00Z",
  "updatedAt": "2024-08-05T16:00:00Z"
}
```

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | UUID | Identificador único da categoria |
| `name` | string | Nome da categoria |
| `description` | string | Descrição da categoria |
| `items` | array | Lista de itens da categoria |
| `createdAt` | datetime | Data/hora de criação |
| `updatedAt` | datetime | Data/hora da última atualização |

## 🔗 Endpoints

### 1. Criar Categoria de Menu

Cria uma nova categoria de menu para um restaurante específico.

```http
POST /api/restaurants/{restaurantId}/menu
```

#### Parâmetros

| Parâmetro | Tipo | Localização | Descrição |
|-----------|------|-------------|-----------|
| `restaurantId` | UUID | Path | ID do restaurante |

#### Request

```bash
curl -X POST "http://localhost:8081/api/restaurants/550e8400-e29b-41d4-a716-446655440000/menu" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Entradas",
    "description": "Pratos para começar a refeição"
  }'
```

#### Response

**Status: 201 Created**

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440001",
  "name": "Entradas",
  "description": "Pratos para começar a refeição",
  "items": [],
  "createdAt": "2024-08-05T16:00:00Z",
  "updatedAt": "2024-08-05T16:00:00Z"
}
```

#### Possíveis Erros

| Status | Descrição |
|--------|-----------|
| **400** | Dados inválidos |
| **404** | Restaurante não encontrado |

### 2. Buscar Categoria de Menu

Retorna uma categoria específica com todos os seus itens.

```http
GET /api/restaurants/{restaurantId}/menu/{menuId}
```

#### Parâmetros

| Parâmetro | Tipo | Localização | Descrição |
|-----------|------|-------------|-----------|
| `restaurantId` | UUID | Path | ID do restaurante |
| `menuId` | UUID | Path | ID da categoria do menu |

#### Request

```bash
curl -X GET "http://localhost:8081/api/restaurants/550e8400-e29b-41d4-a716-446655440000/menu/550e8400-e29b-41d4-a716-446655440001" \
  -H "Accept: application/json"
```

#### Response

**Status: 200 OK**

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440001",
  "name": "Entradas",
  "description": "Pratos para começar a refeição",
  "items": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440002",
      "name": "Bruschetta",
      "description": "Pão italiano com tomate e manjericão",
      "price": 15.90,
      "createdAt": "2024-08-05T16:10:00Z",
      "updatedAt": "2024-08-05T16:10:00Z"
    },
    {
      "id": "550e8400-e29b-41d4-a716-446655440003",
      "name": "Antipasto",
      "description": "Seleção de frios e queijos italianos",
      "price": 28.50,
      "createdAt": "2024-08-05T16:15:00Z",
      "updatedAt": "2024-08-05T16:15:00Z"
    }
  ],
  "createdAt": "2024-08-05T16:00:00Z",
  "updatedAt": "2024-08-05T16:00:00Z"
}
```

### 3. Atualizar Categoria de Menu

Atualiza uma categoria de menu existente.

```http
PUT /api/restaurants/{restaurantId}/menu/{menuId}
```

#### Request

```bash
curl -X PUT "http://localhost:8081/api/restaurants/550e8400-e29b-41d4-a716-446655440000/menu/550e8400-e29b-41d4-a716-446655440001" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Antipasti",
    "description": "Entradas tradicionais italianas"
  }'
```

#### Response

**Status: 200 OK**

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440001",
  "name": "Antipasti",
  "description": "Entradas tradicionais italianas",
  "items": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440002",
      "name": "Bruschetta",
      "description": "Pão italiano com tomate e manjericão",
      "price": 15.90
    }
  ],
  "createdAt": "2024-08-05T16:00:00Z",
  "updatedAt": "2024-08-05T16:30:00Z"
}
```

### 4. Excluir Categoria de Menu

Remove uma categoria de menu do restaurante.

```http
DELETE /api/restaurants/{restaurantId}/menu/{menuId}
```

#### Request

```bash
curl -X DELETE "http://localhost:8081/api/restaurants/550e8400-e29b-41d4-a716-446655440000/menu/550e8400-e29b-41d4-a716-446655440001"
```

#### Response

**Status: 204 No Content**

!!! warning "Atenção"
    Excluir uma categoria remove também todos os itens associados a ela.

## 📝 Exemplos Práticos

### Criando Menu Completo

```bash
#!/bin/bash

RESTAURANT_ID="550e8400-e29b-41d4-a716-446655440000"

# 1. Criar categoria "Entradas"
echo "Criando categoria Entradas..."
ENTRADAS_ID=$(curl -s -X POST "http://localhost:8081/api/restaurants/$RESTAURANT_ID/menu" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Entradas",
    "description": "Pratos para começar a refeição"
  }' | jq -r '.id')

echo "Entradas ID: $ENTRADAS_ID"

# 2. Criar categoria "Pratos Principais"
echo "Criando categoria Pratos Principais..."
PRATOS_ID=$(curl -s -X POST "http://localhost:8081/api/restaurants/$RESTAURANT_ID/menu" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Pratos Principais",
    "description": "Pratos principais da casa"
  }' | jq -r '.id')

echo "Pratos Principais ID: $PRATOS_ID"

# 3. Criar categoria "Sobremesas"
echo "Criando categoria Sobremesas..."
SOBREMESAS_ID=$(curl -s -X POST "http://localhost:8081/api/restaurants/$RESTAURANT_ID/menu" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Sobremesas",
    "description": "Doces para finalizar a refeição"
  }' | jq -r '.id')

echo "Sobremesas ID: $SOBREMESAS_ID"

# 4. Criar categoria "Bebidas"
echo "Criando categoria Bebidas..."
BEBIDAS_ID=$(curl -s -X POST "http://localhost:8081/api/restaurants/$RESTAURANT_ID/menu" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Bebidas",
    "description": "Vinhos, sucos e refrigerantes"
  }' | jq -r '.id')

echo "Bebidas ID: $BEBIDAS_ID"

# 5. Verificar restaurante com menu criado
echo "Verificando restaurante com menu:"
curl -s "http://localhost:8081/api/restaurants/$RESTAURANT_ID" | jq '.menu'
```

### Categorias Típicas de Restaurante

```bash
# Restaurante Italiano
curl -X POST "http://localhost:8081/api/restaurants/$RESTAURANT_ID/menu" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Antipasti",
    "description": "Entradas tradicionais italianas"
  }'

curl -X POST "http://localhost:8081/api/restaurants/$RESTAURANT_ID/menu" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Primi Piatti",
    "description": "Massas e risotos"
  }'

curl -X POST "http://localhost:8081/api/restaurants/$RESTAURANT_ID/menu" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Secondi Piatti",
    "description": "Carnes e peixes"
  }'

curl -X POST "http://localhost:8081/api/restaurants/$RESTAURANT_ID/menu" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Dolci",
    "description": "Sobremesas italianas"
  }'

# Restaurante Japonês
curl -X POST "http://localhost:8081/api/restaurants/$RESTAURANT_ID/menu" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Sushi",
    "description": "Peixes frescos e frutos do mar"
  }'

curl -X POST "http://localhost:8081/api/restaurants/$RESTAURANT_ID/menu" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Sashimi",
    "description": "Peixes cortados em fatias"
  }'

curl -X POST "http://localhost:8081/api/restaurants/$RESTAURANT_ID/menu" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Yakimono",
    "description": "Pratos grelhados"
  }'

curl -X POST "http://localhost:8081/api/restaurants/$RESTAURANT_ID/menu" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Nimono",
    "description": "Pratos cozidos"
  }'
```

## 🔍 Estrutura Hierárquica

### Visualização do Menu

```bash
# Buscar restaurante completo para ver estrutura
curl -s "http://localhost:8081/api/restaurants/$RESTAURANT_ID" | jq '{
  name: .name,
  menu: [.menu[] | {
    category: .name,
    description: .description,
    itemCount: (.items | length),
    items: [.items[] | {name: .name, price: .price}]
  }]
}'
```

**Saída esperada:**

```json
{
  "name": "Bella Italia",
  "menu": [
    {
      "category": "Entradas",
      "description": "Pratos para começar a refeição",
      "itemCount": 3,
      "items": [
        {"name": "Bruschetta", "price": 15.90},
        {"name": "Antipasto", "price": 28.50},
        {"name": "Carpaccio", "price": 32.00}
      ]
    },
    {
      "category": "Pratos Principais",
      "description": "Pratos principais da casa",
      "itemCount": 5,
      "items": [
        {"name": "Spaghetti Carbonara", "price": 45.00},
        {"name": "Risotto ai Funghi", "price": 42.00}
      ]
    }
  ]
}
```

## 🧪 Validações

### Nome da Categoria

```bash
# Nome vazio
curl -X POST "http://localhost:8081/api/restaurants/$RESTAURANT_ID/menu" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "",
    "description": "Descrição válida"
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
      "message": "Nome da categoria é obrigatório"
    }
  ]
}
```

### Restaurante Inexistente

```bash
# Tentar criar categoria para restaurante inexistente
curl -X POST "http://localhost:8081/api/restaurants/00000000-0000-0000-0000-000000000000/menu" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Entradas",
    "description": "Pratos para começar"
  }'

# Resposta: 404 Not Found
{
  "timestamp": "2024-08-05T16:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Restaurant not found with id: 00000000-0000-0000-0000-000000000000",
  "path": "/api/restaurants/00000000-0000-0000-0000-000000000000/menu"
}
```

## 🔄 Relacionamentos

### Com Restaurante

Toda categoria pertence a um restaurante específico:

```bash
# A categoria só existe no contexto do restaurante
GET /api/restaurants/{restaurantId}/menu/{menuId}
```

### Com Menu Items

Cada categoria pode conter múltiplos itens:

```bash
# Adicionar itens à categoria (ver Menu Items API)
POST /api/restaurants/{restaurantId}/menu/{menuId}/items
```

## 📊 Casos de Uso Avançados

### Reorganização do Menu

```bash
#!/bin/bash

RESTAURANT_ID="550e8400-e29b-41d4-a716-446655440000"

# 1. Renomear categoria existente
curl -X PUT "http://localhost:8081/api/restaurants/$RESTAURANT_ID/menu/$ENTRADAS_ID" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Aperitivos",
    "description": "Pequenos pratos para abrir o apetite"
  }'

# 2. Criar nova categoria sazonal
curl -X POST "http://localhost:8081/api/restaurants/$RESTAURANT_ID/menu" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Menu de Verão",
    "description": "Pratos especiais da estação"
  }'

# 3. Criar categoria de promoções
curl -X POST "http://localhost:8081/api/restaurants/$RESTAURANT_ID/menu" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Promoções",
    "description": "Ofertas especiais do dia"
  }'
```

### Menu por Período

```bash
# Menu do almoço
curl -X POST "http://localhost:8081/api/restaurants/$RESTAURANT_ID/menu" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Menu Executivo",
    "description": "Opções rápidas para o almoço (11h às 15h)"
  }'

# Menu do jantar
curl -X POST "http://localhost:8081/api/restaurants/$RESTAURANT_ID/menu" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Menu Degustação",
    "description": "Experiência gastronômica completa (19h às 23h)"
  }'

# Menu infantil
curl -X POST "http://localhost:8081/api/restaurants/$RESTAURANT_ID/menu" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Menu Kids",
    "description": "Pratos especiais para crianças"
  }'
```

## 🚀 Próximos Passos

- [Menu Items API](menu-items.md) - Adicionar itens às categorias
- [Restaurants API](restaurants.md) - Gerenciar restaurantes
- [Kitchen Types API](kitchen-types.md) - Tipos de cozinha
