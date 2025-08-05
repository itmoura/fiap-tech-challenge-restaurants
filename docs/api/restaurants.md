# 🏪 Restaurants API

API para gerenciamento completo de restaurantes, incluindo informações básicas, endereço, horários de funcionamento e menu estruturado.

## 📋 Base Path

```
/api/restaurants
```

## 📊 Modelo de Dados

### RestaurantRequest

```json
{
  "name": "string",
  "address": "string",
  "phone": "string",
  "email": "string",
  "website": "string",
  "kitchenTypeId": "uuid",
  "operationDays": [
    {
      "day": "MONDAY",
      "openTime": "08:00",
      "closeTime": "22:00"
    }
  ]
}
```

| Campo | Tipo | Obrigatório | Descrição |
|-------|------|-------------|-----------|
| `name` | string | ✅ | Nome do restaurante |
| `address` | string | ❌ | Endereço completo |
| `phone` | string | ❌ | Telefone de contato |
| `email` | string | ❌ | Email de contato |
| `website` | string | ❌ | Website do restaurante |
| `kitchenTypeId` | UUID | ✅ | ID do tipo de cozinha |
| `operationDays` | array | ❌ | Horários de funcionamento |

### RestaurantBasicResponse

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Bella Italia",
  "address": "Rua Augusta, 123 - São Paulo, SP",
  "phone": "(11) 99999-9999",
  "email": "contato@bellaitalia.com",
  "website": "https://bellaitalia.com",
  "kitchenType": {
    "id": "550e8400-e29b-41d4-a716-446655440001",
    "name": "Italiana",
    "description": "Culinária tradicional italiana"
  },
  "operationDays": [
    {
      "day": "MONDAY",
      "openTime": "18:00",
      "closeTime": "23:00"
    }
  ],
  "createdAt": "2024-08-05T16:00:00Z",
  "updatedAt": "2024-08-05T16:00:00Z"
}
```

### RestaurantFullResponse

Inclui todos os campos de `RestaurantBasicResponse` mais o menu completo:

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Bella Italia",
  "address": "Rua Augusta, 123 - São Paulo, SP",
  "kitchenType": {
    "id": "550e8400-e29b-41d4-a716-446655440001",
    "name": "Italiana"
  },
  "menu": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440002",
      "name": "Entradas",
      "description": "Pratos para começar",
      "items": [
        {
          "id": "550e8400-e29b-41d4-a716-446655440003",
          "name": "Bruschetta",
          "description": "Pão italiano com tomate",
          "price": 15.90
        }
      ]
    }
  ],
  "createdAt": "2024-08-05T16:00:00Z",
  "updatedAt": "2024-08-05T16:00:00Z"
}
```

## 🔗 Endpoints

### 1. Criar Restaurante

Cria um novo restaurante no sistema.

```http
POST /api/restaurants
```

#### Request

```bash
curl -X POST "http://localhost:8081/api/restaurants" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Bella Italia",
    "address": "Rua Augusta, 123 - São Paulo, SP",
    "phone": "(11) 99999-9999",
    "email": "contato@bellaitalia.com",
    "website": "https://bellaitalia.com",
    "kitchenTypeId": "550e8400-e29b-41d4-a716-446655440001",
    "operationDays": [
      {
        "day": "MONDAY",
        "openTime": "18:00",
        "closeTime": "23:00"
      },
      {
        "day": "TUESDAY",
        "openTime": "18:00",
        "closeTime": "23:00"
      }
    ]
  }'
```

#### Response

**Status: 201 Created**

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Bella Italia",
  "address": "Rua Augusta, 123 - São Paulo, SP",
  "phone": "(11) 99999-9999",
  "email": "contato@bellaitalia.com",
  "website": "https://bellaitalia.com",
  "kitchenType": {
    "id": "550e8400-e29b-41d4-a716-446655440001",
    "name": "Italiana",
    "description": "Culinária tradicional italiana"
  },
  "operationDays": [
    {
      "day": "MONDAY",
      "openTime": "18:00",
      "closeTime": "23:00"
    },
    {
      "day": "TUESDAY",
      "openTime": "18:00",
      "closeTime": "23:00"
    }
  ],
  "menu": [],
  "createdAt": "2024-08-05T16:00:00Z",
  "updatedAt": "2024-08-05T16:00:00Z"
}
```

#### Possíveis Erros

| Status | Descrição |
|--------|-----------|
| **400** | Dados inválidos |
| **404** | Tipo de cozinha não encontrado |

### 2. Listar Restaurantes (Básico)

Retorna todos os restaurantes com informações básicas (sem menu).

```http
GET /api/restaurants
```

#### Request

```bash
curl -X GET "http://localhost:8081/api/restaurants" \
  -H "Accept: application/json"
```

#### Response

**Status: 200 OK**

```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "Bella Italia",
    "address": "Rua Augusta, 123 - São Paulo, SP",
    "phone": "(11) 99999-9999",
    "kitchenType": {
      "id": "550e8400-e29b-41d4-a716-446655440001",
      "name": "Italiana"
    },
    "createdAt": "2024-08-05T16:00:00Z",
    "updatedAt": "2024-08-05T16:00:00Z"
  }
]
```

### 3. Listar Restaurantes (Completo)

Retorna todos os restaurantes com menu completo.

```http
GET /api/restaurants/full
```

#### Request

```bash
curl -X GET "http://localhost:8081/api/restaurants/full" \
  -H "Accept: application/json"
```

#### Response

**Status: 200 OK**

```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "Bella Italia",
    "address": "Rua Augusta, 123 - São Paulo, SP",
    "kitchenType": {
      "id": "550e8400-e29b-41d4-a716-446655440001",
      "name": "Italiana"
    },
    "menu": [
      {
        "id": "550e8400-e29b-41d4-a716-446655440002",
        "name": "Entradas",
        "items": [
          {
            "id": "550e8400-e29b-41d4-a716-446655440003",
            "name": "Bruschetta",
            "price": 15.90
          }
        ]
      }
    ],
    "createdAt": "2024-08-05T16:00:00Z",
    "updatedAt": "2024-08-05T16:00:00Z"
  }
]
```

### 4. Buscar Restaurante por ID

Retorna um restaurante específico com menu completo.

```http
GET /api/restaurants/{id}
```

#### Parâmetros

| Parâmetro | Tipo | Localização | Descrição |
|-----------|------|-------------|-----------|
| `id` | UUID | Path | ID do restaurante |

#### Request

```bash
curl -X GET "http://localhost:8081/api/restaurants/550e8400-e29b-41d4-a716-446655440000" \
  -H "Accept: application/json"
```

#### Response

**Status: 200 OK**

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Bella Italia",
  "address": "Rua Augusta, 123 - São Paulo, SP",
  "phone": "(11) 99999-9999",
  "email": "contato@bellaitalia.com",
  "website": "https://bellaitalia.com",
  "kitchenType": {
    "id": "550e8400-e29b-41d4-a716-446655440001",
    "name": "Italiana",
    "description": "Culinária tradicional italiana"
  },
  "operationDays": [
    {
      "day": "MONDAY",
      "openTime": "18:00",
      "closeTime": "23:00"
    }
  ],
  "menu": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440002",
      "name": "Entradas",
      "description": "Pratos para começar a refeição",
      "items": [
        {
          "id": "550e8400-e29b-41d4-a716-446655440003",
          "name": "Bruschetta",
          "description": "Pão italiano com tomate e manjericão",
          "price": 15.90,
          "createdAt": "2024-08-05T16:10:00Z",
          "updatedAt": "2024-08-05T16:10:00Z"
        }
      ],
      "createdAt": "2024-08-05T16:05:00Z",
      "updatedAt": "2024-08-05T16:05:00Z"
    }
  ],
  "createdAt": "2024-08-05T16:00:00Z",
  "updatedAt": "2024-08-05T16:00:00Z"
}
```

### 5. Atualizar Restaurante

Atualiza as informações de um restaurante existente.

```http
PUT /api/restaurants/{id}
```

#### Request

```bash
curl -X PUT "http://localhost:8081/api/restaurants/550e8400-e29b-41d4-a716-446655440000" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Bella Italia Ristorante",
    "address": "Rua Augusta, 123 - Consolação, São Paulo, SP",
    "phone": "(11) 98888-8888",
    "email": "reservas@bellaitalia.com",
    "website": "https://bellaitalia.com.br",
    "kitchenTypeId": "550e8400-e29b-41d4-a716-446655440001",
    "operationDays": [
      {
        "day": "MONDAY",
        "openTime": "17:30",
        "closeTime": "23:30"
      },
      {
        "day": "TUESDAY",
        "openTime": "17:30",
        "closeTime": "23:30"
      },
      {
        "day": "WEDNESDAY",
        "openTime": "17:30",
        "closeTime": "23:30"
      }
    ]
  }'
```

#### Response

**Status: 200 OK**

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Bella Italia Ristorante",
  "address": "Rua Augusta, 123 - Consolação, São Paulo, SP",
  "phone": "(11) 98888-8888",
  "email": "reservas@bellaitalia.com",
  "website": "https://bellaitalia.com.br",
  "kitchenType": {
    "id": "550e8400-e29b-41d4-a716-446655440001",
    "name": "Italiana"
  },
  "operationDays": [
    {
      "day": "MONDAY",
      "openTime": "17:30",
      "closeTime": "23:30"
    },
    {
      "day": "TUESDAY",
      "openTime": "17:30",
      "closeTime": "23:30"
    },
    {
      "day": "WEDNESDAY",
      "openTime": "17:30",
      "closeTime": "23:30"
    }
  ],
  "menu": [],
  "createdAt": "2024-08-05T16:00:00Z",
  "updatedAt": "2024-08-05T16:30:00Z"
}
```

### 6. Excluir Restaurante

Remove um restaurante do sistema.

```http
DELETE /api/restaurants/{id}
```

#### Request

```bash
curl -X DELETE "http://localhost:8081/api/restaurants/550e8400-e29b-41d4-a716-446655440000"
```

#### Response

**Status: 204 No Content**

## 📝 Exemplos Práticos

### Cenário Completo - Criação de Restaurante

```bash
#!/bin/bash

# 1. Primeiro, criar um tipo de cozinha
echo "Criando tipo de cozinha..."
KITCHEN_TYPE_ID=$(curl -s -X POST "http://localhost:8081/api/kitchen-types" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Italiana",
    "description": "Culinária tradicional italiana"
  }' | jq -r '.id')

echo "Kitchen Type ID: $KITCHEN_TYPE_ID"

# 2. Criar restaurante
echo "Criando restaurante..."
RESTAURANT_ID=$(curl -s -X POST "http://localhost:8081/api/restaurants" \
  -H "Content-Type: application/json" \
  -d "{
    \"name\": \"Bella Italia\",
    \"address\": \"Rua Augusta, 123 - São Paulo, SP\",
    \"phone\": \"(11) 99999-9999\",
    \"email\": \"contato@bellaitalia.com\",
    \"website\": \"https://bellaitalia.com\",
    \"kitchenTypeId\": \"$KITCHEN_TYPE_ID\",
    \"operationDays\": [
      {
        \"day\": \"MONDAY\",
        \"openTime\": \"18:00\",
        \"closeTime\": \"23:00\"
      },
      {
        \"day\": \"TUESDAY\",
        \"openTime\": \"18:00\",
        \"closeTime\": \"23:00\"
      },
      {
        \"day\": \"WEDNESDAY\",
        \"openTime\": \"18:00\",
        \"closeTime\": \"23:00\"
      },
      {
        \"day\": \"THURSDAY\",
        \"openTime\": \"18:00\",
        \"closeTime\": \"23:00\"
      },
      {
        \"day\": \"FRIDAY\",
        \"openTime\": \"18:00\",
        \"closeTime\": \"00:00\"
      },
      {
        \"day\": \"SATURDAY\",
        \"openTime\": \"18:00\",
        \"closeTime\": \"00:00\"
      }
    ]
  }" | jq -r '.id')

echo "Restaurant ID: $RESTAURANT_ID"

# 3. Buscar restaurante criado
echo "Buscando restaurante:"
curl -s "http://localhost:8081/api/restaurants/$RESTAURANT_ID" | jq .

# 4. Listar todos os restaurantes
echo "Listando todos os restaurantes:"
curl -s "http://localhost:8081/api/restaurants" | jq .
```

### Horários de Funcionamento

```bash
# Restaurante que funciona todos os dias
curl -X POST "http://localhost:8081/api/restaurants" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Restaurante 24h",
    "kitchenTypeId": "550e8400-e29b-41d4-a716-446655440001",
    "operationDays": [
      {"day": "MONDAY", "openTime": "00:00", "closeTime": "23:59"},
      {"day": "TUESDAY", "openTime": "00:00", "closeTime": "23:59"},
      {"day": "WEDNESDAY", "openTime": "00:00", "closeTime": "23:59"},
      {"day": "THURSDAY", "openTime": "00:00", "closeTime": "23:59"},
      {"day": "FRIDAY", "openTime": "00:00", "closeTime": "23:59"},
      {"day": "SATURDAY", "openTime": "00:00", "closeTime": "23:59"},
      {"day": "SUNDAY", "openTime": "00:00", "closeTime": "23:59"}
    ]
  }'

# Restaurante que funciona apenas fins de semana
curl -X POST "http://localhost:8081/api/restaurants" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Weekend Bistro",
    "kitchenTypeId": "550e8400-e29b-41d4-a716-446655440001",
    "operationDays": [
      {"day": "FRIDAY", "openTime": "19:00", "closeTime": "02:00"},
      {"day": "SATURDAY", "openTime": "19:00", "closeTime": "02:00"},
      {"day": "SUNDAY", "openTime": "12:00", "closeTime": "22:00"}
    ]
  }'
```

## 🔍 Filtros e Busca (Futuro)

### Por Tipo de Cozinha

```bash
GET /api/restaurants?kitchenType=italiana
```

### Por Localização

```bash
GET /api/restaurants?city=sao-paulo
GET /api/restaurants?neighborhood=vila-madalena
```

### Por Status de Funcionamento

```bash
# Restaurantes abertos agora
GET /api/restaurants?status=open

# Restaurantes que abrem hoje
GET /api/restaurants?openToday=true
```

## 🔗 Relacionamentos

### Com Kitchen Types

Todo restaurante deve ter um tipo de cozinha válido:

```bash
# Tentar criar restaurante com tipo inválido
curl -X POST "http://localhost:8081/api/restaurants" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Restaurante Teste",
    "kitchenTypeId": "00000000-0000-0000-0000-000000000000"
  }'

# Resposta: 404 Not Found
{
  "timestamp": "2024-08-05T16:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Kitchen type not found with id: 00000000-0000-0000-0000-000000000000",
  "path": "/api/restaurants"
}
```

### Com Menu

O menu é gerenciado através de endpoints específicos:

- [Menu Categories](menu-categories.md)
- [Menu Items](menu-items.md)

## 🧪 Validações

### Campos Obrigatórios

```bash
# Nome vazio
curl -X POST "http://localhost:8081/api/restaurants" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "",
    "kitchenTypeId": "550e8400-e29b-41d4-a716-446655440001"
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
      "message": "Nome é obrigatório"
    }
  ]
}
```

### Formato de Email

```bash
# Email inválido
curl -X POST "http://localhost:8081/api/restaurants" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Restaurante Teste",
    "email": "email-invalido",
    "kitchenTypeId": "550e8400-e29b-41d4-a716-446655440001"
  }'

# Resposta: 400 Bad Request
{
  "details": [
    {
      "field": "email",
      "message": "Email deve ter formato válido"
    }
  ]
}
```

### Horários de Funcionamento

```bash
# Horário inválido (abertura após fechamento)
curl -X POST "http://localhost:8081/api/restaurants" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Restaurante Teste",
    "kitchenTypeId": "550e8400-e29b-41d4-a716-446655440001",
    "operationDays": [
      {
        "day": "MONDAY",
        "openTime": "23:00",
        "closeTime": "18:00"
      }
    ]
  }'

# Resposta: 400 Bad Request
{
  "details": [
    {
      "field": "operationDays[0].closeTime",
      "message": "Horário de fechamento deve ser posterior ao de abertura"
    }
  ]
}
```

## 🚀 Próximos Passos

- [Menu Categories API](menu-categories.md) - Gerenciar categorias do menu
- [Menu Items API](menu-items.md) - Gerenciar itens do menu
- [Kitchen Types API](kitchen-types.md) - Gerenciar tipos de cozinha
