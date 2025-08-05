# 🍳 Kitchen Types API

API para gerenciamento de tipos de cozinha (Italiana, Japonesa, Brasileira, etc.). Os tipos de cozinha são utilizados para categorizar restaurantes.

## 📋 Base Path

```
/api/kitchen-types
```

## 📊 Modelo de Dados

### KitchenTypeRequest

```json
{
  "name": "string",
  "description": "string"
}
```

| Campo | Tipo | Obrigatório | Descrição |
|-------|------|-------------|-----------|
| `name` | string | ✅ | Nome do tipo de cozinha (único) |
| `description` | string | ❌ | Descrição detalhada do tipo |

### KitchenTypeResponse

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Italiana",
  "description": "Culinária tradicional italiana com massas e pizzas",
  "createdAt": "2024-08-05T16:00:00Z",
  "updatedAt": "2024-08-05T16:00:00Z"
}
```

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | UUID | Identificador único |
| `name` | string | Nome do tipo de cozinha |
| `description` | string | Descrição do tipo |
| `createdAt` | datetime | Data/hora de criação |
| `updatedAt` | datetime | Data/hora da última atualização |

## 🔗 Endpoints

### 1. Criar Tipo de Cozinha

Cria um novo tipo de cozinha no sistema.

```http
POST /api/kitchen-types
```

#### Request

```bash
curl -X POST "http://localhost:8081/api/kitchen-types" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Italiana",
    "description": "Culinária tradicional italiana com massas, pizzas e risotos"
  }'
```

#### Response

**Status: 201 Created**

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Italiana",
  "description": "Culinária tradicional italiana com massas, pizzas e risotos",
  "createdAt": "2024-08-05T16:00:00Z",
  "updatedAt": "2024-08-05T16:00:00Z"
}
```

#### Possíveis Erros

| Status | Descrição | Exemplo |
|--------|-----------|---------|
| **400** | Dados inválidos | Nome vazio ou muito longo |
| **409** | Conflito | Tipo de cozinha já existe |

**Exemplo de erro 400:**

```json
{
  "timestamp": "2024-08-05T16:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/kitchen-types",
  "details": [
    {
      "field": "name",
      "message": "Nome é obrigatório"
    }
  ]
}
```

### 2. Listar Tipos de Cozinha

Retorna todos os tipos de cozinha cadastrados.

```http
GET /api/kitchen-types
```

#### Request

```bash
curl -X GET "http://localhost:8081/api/kitchen-types" \
  -H "Accept: application/json"
```

#### Response

**Status: 200 OK**

```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "Italiana",
    "description": "Culinária tradicional italiana",
    "createdAt": "2024-08-05T16:00:00Z",
    "updatedAt": "2024-08-05T16:00:00Z"
  },
  {
    "id": "550e8400-e29b-41d4-a716-446655440001",
    "name": "Japonesa",
    "description": "Culinária japonesa com sushi e yakisoba",
    "createdAt": "2024-08-05T16:05:00Z",
    "updatedAt": "2024-08-05T16:05:00Z"
  }
]
```

### 3. Buscar Tipo de Cozinha por ID

Retorna um tipo de cozinha específico.

```http
GET /api/kitchen-types/{id}
```

#### Parâmetros

| Parâmetro | Tipo | Localização | Descrição |
|-----------|------|-------------|-----------|
| `id` | UUID | Path | ID do tipo de cozinha |

#### Request

```bash
curl -X GET "http://localhost:8081/api/kitchen-types/550e8400-e29b-41d4-a716-446655440000" \
  -H "Accept: application/json"
```

#### Response

**Status: 200 OK**

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Italiana",
  "description": "Culinária tradicional italiana",
  "createdAt": "2024-08-05T16:00:00Z",
  "updatedAt": "2024-08-05T16:00:00Z"
}
```

#### Possíveis Erros

| Status | Descrição |
|--------|-----------|
| **404** | Tipo de cozinha não encontrado |

### 4. Atualizar Tipo de Cozinha

Atualiza um tipo de cozinha existente.

```http
PUT /api/kitchen-types/{id}
```

#### Parâmetros

| Parâmetro | Tipo | Localização | Descrição |
|-----------|------|-------------|-----------|
| `id` | UUID | Path | ID do tipo de cozinha |

#### Request

```bash
curl -X PUT "http://localhost:8081/api/kitchen-types/550e8400-e29b-41d4-a716-446655440000" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Italiana Moderna",
    "description": "Culinária italiana contemporânea com toques modernos"
  }'
```

#### Response

**Status: 200 OK**

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Italiana Moderna",
  "description": "Culinária italiana contemporânea com toques modernos",
  "createdAt": "2024-08-05T16:00:00Z",
  "updatedAt": "2024-08-05T16:30:00Z"
}
```

#### Possíveis Erros

| Status | Descrição |
|--------|-----------|
| **400** | Dados inválidos |
| **404** | Tipo de cozinha não encontrado |
| **409** | Nome já existe para outro tipo |

### 5. Excluir Tipo de Cozinha

Remove um tipo de cozinha do sistema.

```http
DELETE /api/kitchen-types/{id}
```

#### Parâmetros

| Parâmetro | Tipo | Localização | Descrição |
|-----------|------|-------------|-----------|
| `id` | UUID | Path | ID do tipo de cozinha |

#### Request

```bash
curl -X DELETE "http://localhost:8081/api/kitchen-types/550e8400-e29b-41d4-a716-446655440000"
```

#### Response

**Status: 204 No Content**

#### Possíveis Erros

| Status | Descrição |
|--------|-----------|
| **404** | Tipo de cozinha não encontrado |
| **409** | Tipo está sendo usado por restaurantes |

## 📝 Exemplos Práticos

### Cenário Completo

```bash
#!/bin/bash

# 1. Criar tipos de cozinha
echo "Criando tipos de cozinha..."

ITALIANA_ID=$(curl -s -X POST "http://localhost:8081/api/kitchen-types" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Italiana",
    "description": "Culinária tradicional italiana"
  }' | jq -r '.id')

JAPONESA_ID=$(curl -s -X POST "http://localhost:8081/api/kitchen-types" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Japonesa", 
    "description": "Culinária japonesa autêntica"
  }' | jq -r '.id')

BRASILEIRA_ID=$(curl -s -X POST "http://localhost:8081/api/kitchen-types" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Brasileira",
    "description": "Culinária brasileira regional"
  }' | jq -r '.id')

echo "Italiana ID: $ITALIANA_ID"
echo "Japonesa ID: $JAPONESA_ID"
echo "Brasileira ID: $BRASILEIRA_ID"

# 2. Listar todos os tipos
echo -e "\nListando todos os tipos:"
curl -s "http://localhost:8081/api/kitchen-types" | jq .

# 3. Buscar tipo específico
echo -e "\nBuscando tipo italiano:"
curl -s "http://localhost:8081/api/kitchen-types/$ITALIANA_ID" | jq .

# 4. Atualizar tipo
echo -e "\nAtualizando tipo italiano:"
curl -s -X PUT "http://localhost:8081/api/kitchen-types/$ITALIANA_ID" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Italiana Gourmet",
    "description": "Culinária italiana refinada e gourmet"
  }' | jq .
```

### Validações e Regras de Negócio

#### 1. Nome Único

```bash
# Tentar criar tipo duplicado
curl -X POST "http://localhost:8081/api/kitchen-types" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Italiana",
    "description": "Outra descrição"
  }'

# Resposta: 409 Conflict
{
  "timestamp": "2024-08-05T16:00:00Z",
  "status": 409,
  "error": "Conflict",
  "message": "Kitchen type with name 'Italiana' already exists",
  "path": "/api/kitchen-types"
}
```

#### 2. Validação de Campos

```bash
# Nome vazio
curl -X POST "http://localhost:8081/api/kitchen-types" \
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
  "path": "/api/kitchen-types",
  "details": [
    {
      "field": "name",
      "message": "Nome não pode estar vazio"
    }
  ]
}
```

## 🔍 Filtros e Busca

### Busca por Nome (Futuro)

```bash
# Buscar tipos que contenham "ital"
GET /api/kitchen-types?search=ital

# Buscar por nome exato
GET /api/kitchen-types?name=Italiana
```

### Ordenação (Futuro)

```bash
# Ordenar por nome
GET /api/kitchen-types?sort=name,asc

# Ordenar por data de criação
GET /api/kitchen-types?sort=createdAt,desc
```

## 🔗 Relacionamentos

### Restaurantes que Usam o Tipo

```bash
# Buscar restaurantes de um tipo específico
GET /api/restaurants?kitchenTypeId=550e8400-e29b-41d4-a716-446655440000
```

### Verificar Uso Antes de Deletar

Antes de deletar um tipo de cozinha, o sistema verifica se há restaurantes usando esse tipo:

```bash
curl -X DELETE "http://localhost:8081/api/kitchen-types/550e8400-e29b-41d4-a716-446655440000"

# Se houver restaurantes usando:
# Status: 409 Conflict
{
  "timestamp": "2024-08-05T16:00:00Z",
  "status": 409,
  "error": "Conflict",
  "message": "Cannot delete kitchen type. It is being used by 3 restaurants",
  "path": "/api/kitchen-types/550e8400-e29b-41d4-a716-446655440000"
}
```

## 🧪 Testes

### Postman Collection

```json
{
  "info": {
    "name": "Kitchen Types API",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Create Kitchen Type",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"name\": \"{{kitchenTypeName}}\",\n  \"description\": \"{{kitchenTypeDescription}}\"\n}"
        },
        "url": {
          "raw": "{{baseUrl}}/api/kitchen-types",
          "host": ["{{baseUrl}}"],
          "path": ["api", "kitchen-types"]
        }
      }
    }
  ]
}
```

### Testes Automatizados

```javascript
// Postman Test Script
pm.test("Status code is 201", function () {
    pm.response.to.have.status(201);
});

pm.test("Response has required fields", function () {
    const jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property('id');
    pm.expect(jsonData).to.have.property('name');
    pm.expect(jsonData).to.have.property('createdAt');
    pm.expect(jsonData).to.have.property('updatedAt');
});

pm.test("Name matches request", function () {
    const jsonData = pm.response.json();
    pm.expect(jsonData.name).to.eql(pm.variables.get("kitchenTypeName"));
});
```

## 🚀 Próximos Passos

- [Restaurants API](restaurants.md) - Como usar kitchen types em restaurantes
- [Menu Categories API](menu-categories.md) - Estrutura de menus
- [Códigos de Status](status-codes.md) - Referência completa
