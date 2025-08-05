# 📚 API Reference - Visão Geral

A Tech Challenge Restaurant API é uma API RESTful desenvolvida com Spring Boot que oferece endpoints para gerenciamento completo de restaurantes, tipos de cozinha, menus e itens.

## 🌐 Base URL

```
http://localhost:8081/api
```

## 🔐 Autenticação

Atualmente, a API não requer autenticação. Todos os endpoints são públicos para fins de desenvolvimento e demonstração.

!!! warning "Produção"
    Em ambiente de produção, implemente autenticação adequada (JWT, OAuth2, etc.)

## 📋 Formato de Dados

### Content-Type

Todos os endpoints aceitam e retornam dados no formato JSON:

```
Content-Type: application/json
Accept: application/json
```

### Estrutura de Resposta

#### Sucesso

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Exemplo",
  "createdAt": "2024-08-05T16:00:00Z",
  "updatedAt": "2024-08-05T16:00:00Z"
}
```

#### Erro

```json
{
  "timestamp": "2024-08-05T16:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/restaurants",
  "details": [
    {
      "field": "name",
      "message": "Nome é obrigatório"
    }
  ]
}
```

## 🏗️ Recursos da API

### 🍳 Kitchen Types
Gerenciamento de tipos de cozinha (Italiana, Japonesa, Brasileira, etc.)

- **Base Path**: `/api/kitchen-types`
- **Operações**: CRUD completo
- **Relacionamentos**: Usado por restaurantes

### 🏪 Restaurants
Gerenciamento de restaurantes com informações básicas e menu

- **Base Path**: `/api/restaurants`
- **Operações**: CRUD completo
- **Relacionamentos**: Possui kitchen type, menu categories e items

### 📋 Menu Categories
Gerenciamento de categorias do menu (Entradas, Pratos Principais, Sobremesas, etc.)

- **Base Path**: `/api/restaurants/{restaurantId}/menu`
- **Operações**: CRUD completo
- **Relacionamentos**: Pertence a um restaurante, contém menu items

### 🍽️ Menu Items
Gerenciamento de itens do menu com preços e descrições

- **Base Path**: `/api/restaurants/{restaurantId}/menu/{menuId}/items`
- **Operações**: CRUD completo
- **Relacionamentos**: Pertence a uma categoria de menu

## 🔄 Padrões da API

### UUIDs

Todos os recursos usam UUIDs como identificadores:

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000"
}
```

### Timestamps

Todos os recursos incluem timestamps de criação e atualização:

```json
{
  "createdAt": "2024-08-05T16:00:00Z",
  "updatedAt": "2024-08-05T16:00:00Z"
}
```

### Paginação

Para endpoints que retornam listas, a paginação pode ser implementada usando query parameters:

```
GET /api/restaurants?page=0&size=10&sort=name,asc
```

### Filtros

Alguns endpoints suportam filtros via query parameters:

```
GET /api/restaurants?kitchenType=italiana&city=sao-paulo
```

## 📊 Códigos de Status HTTP

| Código | Descrição | Uso |
|--------|-----------|-----|
| **200** | OK | Operação bem-sucedida (GET, PUT) |
| **201** | Created | Recurso criado com sucesso (POST) |
| **204** | No Content | Recurso deletado com sucesso (DELETE) |
| **400** | Bad Request | Dados inválidos ou malformados |
| **404** | Not Found | Recurso não encontrado |
| **409** | Conflict | Conflito (ex: nome já existe) |
| **422** | Unprocessable Entity | Erro de validação |
| **500** | Internal Server Error | Erro interno do servidor |

## 🔍 Swagger/OpenAPI

A API possui documentação interativa disponível via Swagger UI:

```
http://localhost:8081/swagger-ui.html
```

### Especificação OpenAPI

A especificação completa está disponível em:

```
http://localhost:8081/v3/api-docs
```

## 📝 Exemplos de Uso

### Fluxo Básico

1. **Criar um tipo de cozinha**
```bash
curl -X POST "http://localhost:8081/api/kitchen-types" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Italiana",
    "description": "Culinária tradicional italiana"
  }'
```

2. **Criar um restaurante**
```bash
curl -X POST "http://localhost:8081/api/restaurants" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Bella Italia",
    "address": "Rua Augusta, 123",
    "kitchenTypeId": "550e8400-e29b-41d4-a716-446655440000"
  }'
```

3. **Criar categoria de menu**
```bash
curl -X POST "http://localhost:8081/api/restaurants/{restaurantId}/menu" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Entradas",
    "description": "Pratos para começar a refeição"
  }'
```

4. **Adicionar item ao menu**
```bash
curl -X POST "http://localhost:8081/api/restaurants/{restaurantId}/menu/{menuId}/items" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Bruschetta",
    "description": "Pão italiano com tomate e manjericão",
    "price": 15.90
  }'
```

## 🛠️ Ferramentas de Teste

### cURL

Exemplos básicos usando cURL estão disponíveis em cada endpoint.

### Postman

Collection completa disponível em:
```
postman/Tech_Challenge_Restaurants.postman_collection.json
```

### HTTPie

```bash
# Instalar HTTPie
pip install httpie

# Exemplo de uso
http POST localhost:8081/api/kitchen-types name="Japonesa" description="Culinária japonesa"
```

## 🔄 Versionamento

Atualmente a API está na versão 1.0. Futuras versões serão versionadas através do path:

```
/api/v1/restaurants  # Versão atual
/api/v2/restaurants  # Versão futura
```

## 📈 Rate Limiting

Atualmente não há rate limiting implementado. Em produção, considere implementar:

- Limite por IP
- Limite por usuário autenticado
- Diferentes limites por tipo de operação

## 🔍 Monitoramento

### Health Check

```bash
curl http://localhost:8081/actuator/health
```

### Métricas

```bash
curl http://localhost:8081/actuator/metrics
```

### Prometheus

```bash
curl http://localhost:8081/actuator/prometheus
```

## 🚀 Próximos Passos

Explore a documentação detalhada de cada recurso:

- [Kitchen Types](kitchen-types.md) - Gerenciamento de tipos de cozinha
- [Restaurants](restaurants.md) - Gerenciamento de restaurantes
- [Menu Categories](menu-categories.md) - Gerenciamento de categorias do menu
- [Menu Items](menu-items.md) - Gerenciamento de itens do menu
- [Códigos de Status](status-codes.md) - Referência completa de códigos HTTP
