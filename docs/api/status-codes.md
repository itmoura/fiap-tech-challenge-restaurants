# 📊 Códigos de Status HTTP

Referência completa dos códigos de status HTTP utilizados pela Tech Challenge Restaurant API.

## 🟢 Códigos de Sucesso (2xx)

### 200 OK

**Uso**: Operação bem-sucedida para requisições GET e PUT.

**Quando ocorre**:
- Buscar recurso existente
- Atualizar recurso com sucesso
- Listar recursos

**Exemplo**:
```bash
curl -X GET "http://localhost:8081/api/restaurants/550e8400-e29b-41d4-a716-446655440000"
# Status: 200 OK
```

**Response**:
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Bella Italia",
  "address": "Rua Augusta, 123"
}
```

### 201 Created

**Uso**: Recurso criado com sucesso.

**Quando ocorre**:
- Criar novo restaurante
- Criar novo tipo de cozinha
- Criar nova categoria de menu
- Criar novo item do menu

**Exemplo**:
```bash
curl -X POST "http://localhost:8081/api/kitchen-types" \
  -H "Content-Type: application/json" \
  -d '{"name": "Italiana", "description": "Culinária italiana"}'
# Status: 201 Created
```

**Response**:
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Italiana",
  "description": "Culinária italiana",
  "createdAt": "2024-08-05T16:00:00Z",
  "updatedAt": "2024-08-05T16:00:00Z"
}
```

### 204 No Content

**Uso**: Operação bem-sucedida sem conteúdo de retorno.

**Quando ocorre**:
- Excluir recurso com sucesso

**Exemplo**:
```bash
curl -X DELETE "http://localhost:8081/api/restaurants/550e8400-e29b-41d4-a716-446655440000"
# Status: 204 No Content
# Body: (vazio)
```

## 🟡 Códigos de Erro do Cliente (4xx)

### 400 Bad Request

**Uso**: Dados inválidos ou malformados na requisição.

**Quando ocorre**:
- Campos obrigatórios ausentes
- Formato de dados inválido
- Validação de negócio falhou
- JSON malformado

**Exemplos**:

#### Campo obrigatório ausente
```bash
curl -X POST "http://localhost:8081/api/kitchen-types" \
  -H "Content-Type: application/json" \
  -d '{"description": "Sem nome"}'
# Status: 400 Bad Request
```

**Response**:
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

#### Formato inválido
```bash
curl -X POST "http://localhost:8081/api/restaurants" \
  -H "Content-Type: application/json" \
  -d '{"name": "Teste", "email": "email-inválido", "kitchenTypeId": "550e8400-e29b-41d4-a716-446655440000"}'
# Status: 400 Bad Request
```

**Response**:
```json
{
  "timestamp": "2024-08-05T16:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/restaurants",
  "details": [
    {
      "field": "email",
      "message": "Email deve ter formato válido"
    }
  ]
}
```

#### Preço inválido
```bash
curl -X POST "http://localhost:8081/api/restaurants/550e8400-e29b-41d4-a716-446655440000/menu/550e8400-e29b-41d4-a716-446655440001/items" \
  -H "Content-Type: application/json" \
  -d '{"name": "Item", "price": -10.00}'
# Status: 400 Bad Request
```

**Response**:
```json
{
  "timestamp": "2024-08-05T16:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/restaurants/550e8400-e29b-41d4-a716-446655440000/menu/550e8400-e29b-41d4-a716-446655440001/items",
  "details": [
    {
      "field": "price",
      "message": "Preço deve ser maior que zero"
    }
  ]
}
```

### 404 Not Found

**Uso**: Recurso não encontrado.

**Quando ocorre**:
- Buscar recurso com ID inexistente
- Tentar atualizar recurso inexistente
- Tentar excluir recurso inexistente
- Referência a recurso relacionado inexistente

**Exemplos**:

#### Recurso não encontrado
```bash
curl -X GET "http://localhost:8081/api/restaurants/00000000-0000-0000-0000-000000000000"
# Status: 404 Not Found
```

**Response**:
```json
{
  "timestamp": "2024-08-05T16:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Restaurant not found with id: 00000000-0000-0000-0000-000000000000",
  "path": "/api/restaurants/00000000-0000-0000-0000-000000000000"
}
```

#### Kitchen Type não encontrado
```bash
curl -X POST "http://localhost:8081/api/restaurants" \
  -H "Content-Type: application/json" \
  -d '{"name": "Teste", "kitchenTypeId": "00000000-0000-0000-0000-000000000000"}'
# Status: 404 Not Found
```

**Response**:
```json
{
  "timestamp": "2024-08-05T16:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Kitchen type not found with id: 00000000-0000-0000-0000-000000000000",
  "path": "/api/restaurants"
}
```

#### Menu item não encontrado
```bash
curl -X GET "http://localhost:8081/api/menu-items/00000000-0000-0000-0000-000000000000"
# Status: 404 Not Found
```

**Response**:
```json
{
  "timestamp": "2024-08-05T16:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Menu item not found with id: 00000000-0000-0000-0000-000000000000",
  "path": "/api/menu-items/00000000-0000-0000-0000-000000000000"
}
```

### 409 Conflict

**Uso**: Conflito com o estado atual do recurso.

**Quando ocorre**:
- Tentar criar recurso com nome duplicado
- Tentar excluir recurso que está sendo usado
- Violação de regras de negócio

**Exemplos**:

#### Nome duplicado
```bash
# Primeiro, criar um tipo
curl -X POST "http://localhost:8081/api/kitchen-types" \
  -H "Content-Type: application/json" \
  -d '{"name": "Italiana", "description": "Primeira"}'

# Tentar criar outro com mesmo nome
curl -X POST "http://localhost:8081/api/kitchen-types" \
  -H "Content-Type: application/json" \
  -d '{"name": "Italiana", "description": "Segunda"}'
# Status: 409 Conflict
```

**Response**:
```json
{
  "timestamp": "2024-08-05T16:00:00Z",
  "status": 409,
  "error": "Conflict",
  "message": "Kitchen type with name 'Italiana' already exists",
  "path": "/api/kitchen-types"
}
```

#### Recurso em uso
```bash
# Tentar excluir kitchen type usado por restaurantes
curl -X DELETE "http://localhost:8081/api/kitchen-types/550e8400-e29b-41d4-a716-446655440000"
# Status: 409 Conflict
```

**Response**:
```json
{
  "timestamp": "2024-08-05T16:00:00Z",
  "status": 409,
  "error": "Conflict",
  "message": "Cannot delete kitchen type. It is being used by 3 restaurants",
  "path": "/api/kitchen-types/550e8400-e29b-41d4-a716-446655440000"
}
```

### 422 Unprocessable Entity

**Uso**: Entidade não processável devido a erros semânticos.

**Quando ocorre**:
- Dados sintaticamente corretos mas semanticamente inválidos
- Regras de negócio complexas violadas

**Exemplo**:
```bash
# Horário de funcionamento inválido
curl -X POST "http://localhost:8081/api/restaurants" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Teste",
    "kitchenTypeId": "550e8400-e29b-41d4-a716-446655440000",
    "operationDays": [
      {
        "day": "MONDAY",
        "openTime": "23:00",
        "closeTime": "18:00"
      }
    ]
  }'
# Status: 422 Unprocessable Entity
```

**Response**:
```json
{
  "timestamp": "2024-08-05T16:00:00Z",
  "status": 422,
  "error": "Unprocessable Entity",
  "message": "Business rule validation failed",
  "path": "/api/restaurants",
  "details": [
    {
      "field": "operationDays[0]",
      "message": "Horário de fechamento deve ser posterior ao de abertura"
    }
  ]
}
```

## 🔴 Códigos de Erro do Servidor (5xx)

### 500 Internal Server Error

**Uso**: Erro interno do servidor.

**Quando ocorre**:
- Erro não tratado na aplicação
- Falha na conexão com banco de dados
- Erro de configuração
- Exception não capturada

**Exemplo**:
```bash
# Erro interno (simulado)
curl -X GET "http://localhost:8081/api/restaurants"
# Status: 500 Internal Server Error
```

**Response**:
```json
{
  "timestamp": "2024-08-05T16:00:00Z",
  "status": 500,
  "error": "Internal Server Error",
  "message": "An unexpected error occurred",
  "path": "/api/restaurants"
}
```

### 503 Service Unavailable

**Uso**: Serviço temporariamente indisponível.

**Quando ocorre**:
- Banco de dados indisponível
- Serviço em manutenção
- Sobrecarga do sistema

**Response**:
```json
{
  "timestamp": "2024-08-05T16:00:00Z",
  "status": 503,
  "error": "Service Unavailable",
  "message": "Database connection unavailable",
  "path": "/api/restaurants"
}
```

## 📋 Resumo por Endpoint

### Kitchen Types

| Endpoint | Método | Sucesso | Possíveis Erros |
|----------|--------|---------|-----------------|
| `/api/kitchen-types` | POST | 201 | 400, 409 |
| `/api/kitchen-types` | GET | 200 | 500 |
| `/api/kitchen-types/{id}` | GET | 200 | 404, 500 |
| `/api/kitchen-types/{id}` | PUT | 200 | 400, 404, 409 |
| `/api/kitchen-types/{id}` | DELETE | 204 | 404, 409 |

### Restaurants

| Endpoint | Método | Sucesso | Possíveis Erros |
|----------|--------|---------|-----------------|
| `/api/restaurants` | POST | 201 | 400, 404, 422 |
| `/api/restaurants` | GET | 200 | 500 |
| `/api/restaurants/full` | GET | 200 | 500 |
| `/api/restaurants/{id}` | GET | 200 | 404, 500 |
| `/api/restaurants/{id}` | PUT | 200 | 400, 404, 422 |
| `/api/restaurants/{id}` | DELETE | 204 | 404 |

### Menu Categories

| Endpoint | Método | Sucesso | Possíveis Erros |
|----------|--------|---------|-----------------|
| `/api/restaurants/{restaurantId}/menu` | POST | 201 | 400, 404 |
| `/api/restaurants/{restaurantId}/menu/{menuId}` | GET | 200 | 404, 500 |
| `/api/restaurants/{restaurantId}/menu/{menuId}` | PUT | 200 | 400, 404 |
| `/api/restaurants/{restaurantId}/menu/{menuId}` | DELETE | 204 | 404 |

### Menu Items

| Endpoint | Método | Sucesso | Possíveis Erros |
|----------|--------|---------|-----------------|
| `/api/restaurants/{restaurantId}/menu/{menuId}/items` | POST | 201 | 400, 404 |
| `/api/menu-items/{itemId}` | GET | 200 | 404, 500 |
| `/api/restaurants/{restaurantId}/menu/{menuId}/items/{itemId}` | PUT | 200 | 400, 404 |
| `/api/restaurants/{restaurantId}/menu/{menuId}/items/{itemId}` | DELETE | 204 | 404 |

## 🛠️ Tratamento de Erros

### Estrutura Padrão de Erro

Todos os erros seguem a estrutura padrão:

```json
{
  "timestamp": "2024-08-05T16:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Descrição do erro",
  "path": "/api/endpoint",
  "details": [
    {
      "field": "campo",
      "message": "mensagem específica"
    }
  ]
}
```

### Campos de Erro

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `timestamp` | string | Data/hora do erro (ISO 8601) |
| `status` | integer | Código de status HTTP |
| `error` | string | Nome do erro HTTP |
| `message` | string | Mensagem descritiva do erro |
| `path` | string | Endpoint onde ocorreu o erro |
| `details` | array | Detalhes específicos (opcional) |

### Detalhes de Validação

Para erros de validação (400, 422), o campo `details` contém:

```json
{
  "details": [
    {
      "field": "name",
      "message": "Nome é obrigatório"
    },
    {
      "field": "email",
      "message": "Email deve ter formato válido"
    }
  ]
}
```

## 🧪 Testando Códigos de Status

### Script de Teste

```bash
#!/bin/bash

BASE_URL="http://localhost:8081/api"

echo "🧪 Testando códigos de status..."

# 201 - Created
echo "Testando 201 Created:"
curl -s -w "Status: %{http_code}\n" -X POST "$BASE_URL/kitchen-types" \
  -H "Content-Type: application/json" \
  -d '{"name": "Teste", "description": "Teste"}' | tail -1

# 200 - OK
echo "Testando 200 OK:"
curl -s -w "Status: %{http_code}\n" -X GET "$BASE_URL/kitchen-types" | tail -1

# 404 - Not Found
echo "Testando 404 Not Found:"
curl -s -w "Status: %{http_code}\n" -X GET "$BASE_URL/kitchen-types/00000000-0000-0000-0000-000000000000" | tail -1

# 400 - Bad Request
echo "Testando 400 Bad Request:"
curl -s -w "Status: %{http_code}\n" -X POST "$BASE_URL/kitchen-types" \
  -H "Content-Type: application/json" \
  -d '{"description": "Sem nome"}' | tail -1

# 409 - Conflict
echo "Testando 409 Conflict:"
curl -s -w "Status: %{http_code}\n" -X POST "$BASE_URL/kitchen-types" \
  -H "Content-Type: application/json" \
  -d '{"name": "Teste", "description": "Duplicado"}' | tail -1

echo "✅ Testes concluídos!"
```

### Postman Tests

```javascript
// Test para verificar status code
pm.test("Status code is 201", function () {
    pm.response.to.have.status(201);
});

// Test para verificar estrutura de erro
pm.test("Error response has correct structure", function () {
    if (pm.response.code >= 400) {
        const jsonData = pm.response.json();
        pm.expect(jsonData).to.have.property('timestamp');
        pm.expect(jsonData).to.have.property('status');
        pm.expect(jsonData).to.have.property('error');
        pm.expect(jsonData).to.have.property('message');
        pm.expect(jsonData).to.have.property('path');
    }
});

// Test para validação específica
pm.test("Validation error has details", function () {
    if (pm.response.code === 400) {
        const jsonData = pm.response.json();
        pm.expect(jsonData).to.have.property('details');
        pm.expect(jsonData.details).to.be.an('array');
    }
});
```

## 🚀 Próximos Passos

- [API Overview](overview.md) - Visão geral da API
- [Kitchen Types](kitchen-types.md) - Documentação específica
- [Restaurants](restaurants.md) - Documentação específica
- [Menu Categories](menu-categories.md) - Documentação específica
- [Menu Items](menu-items.md) - Documentação específica
