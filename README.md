# 🍽️ Tech Challenge - Sistema de Gerenciamento de Restaurantes

Sistema de gerenciamento de restaurantes desenvolvido com **Spring Boot + MongoDB**, focado em alta performance de leitura através de estrutura de documentos aninhados.

## 📋 Índice

- [Visão Geral](#-visão-geral)
- [Arquitetura](#-arquitetura)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Modelagem de Dados](#-modelagem-de-dados)
- [Endpoints da API](#-endpoints-da-api)
- [Como Executar](#-como-executar)
- [Testes](#-testes)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)

## 🎯 Visão Geral

O sistema permite o gerenciamento completo de restaurantes com suas informações básicas e menus estruturados em categorias. A modelagem foi pensada para MongoDB (NoSQL), evitando abordagens relacionais e priorizando performance de leitura através de documentos aninhados.

### Principais Funcionalidades

- ✅ **Gerenciamento de Restaurantes**: CRUD completo com informações básicas
- ✅ **Menu Estruturado**: Categorias e itens organizados hierarquicamente
- ✅ **Consultas Otimizadas**: Endpoints específicos para diferentes necessidades
- ✅ **Busca por Item**: Localização de itens específicos com contexto completo
- ✅ **UUIDs**: Identificadores únicos para todos os recursos
- ✅ **Documentação OpenAPI**: Swagger UI integrado

## 🏗️ Arquitetura

O projeto segue os princípios da **Arquitetura Hexagonal (Ports & Adapters)**:

```
src/main/java/com/fiap/itmoura/tech_challenge_restaurant/
├── application/          # Camada de Aplicação
│   ├── models/          # DTOs e modelos de transferência
│   ├── ports/           # Interfaces (Ports)
│   └── usecases/        # Casos de uso (Services)
├── domain/              # Camada de Domínio
│   ├── entities/        # Entidades de domínio
│   └── exceptions/      # Exceções customizadas
├── infrastructure/      # Camada de Infraestrutura
│   └── MongoConfig.java # Configurações do MongoDB
└── presentation/        # Camada de Apresentação
    ├── controllers/     # Controllers REST
    └── handlers/        # Tratamento de exceções
```

## 📊 Modelagem de Dados

### Estrutura do Documento `Restaurant`

```json
{
  "_id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Restaurante do João",
  "address": "Rua das Flores, 123",
  "kitchenType": {
    "id": "550e8400-e29b-41d4-a716-446655440001",
    "name": "Japonesa",
    "description": "Cozinha Japonesa"
  },
  "daysOperation": [
    {
      "day": "MONDAY",
      "openingHours": "08:00",
      "closingHours": "18:00"
    }
  ],
  "ownerId": "550e8400-e29b-41d4-a716-446655440002",
  "isActive": true,
  "menu": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440003",
      "type": "Lanche",
      "items": [
        {
          "id": "550e8400-e29b-41d4-a716-446655440004",
          "name": "Hambúrguer Artesanal",
          "description": "Hambúrguer com carne artesanal, queijo, bacon e molho especial",
          "price": 25.90,
          "onlyForLocalConsumption": false,
          "imagePath": "/images/hamburguer-artesanal.jpg",
          "isActive": true
        }
      ]
    }
  ],
  "lastUpdate": "2024-08-05T10:30:00",
  "createdAt": "2024-08-05T08:00:00"
}
```

### Vantagens da Estrutura Aninhada

- 🚀 **Performance**: Uma única consulta retorna todos os dados necessários
- 📦 **Atomicidade**: Operações em um único documento são atômicas
- 🎯 **Simplicidade**: Não há necessidade de joins complexos
- 📈 **Escalabilidade**: Melhor distribuição de dados no MongoDB

## 🔗 Endpoints da API

### Restaurantes

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/api/restaurants` | Lista restaurantes (sem menu) |
| `GET` | `/api/restaurants/full` | Lista restaurantes com menu completo |
| `GET` | `/api/restaurants/{id}` | Busca restaurante por ID (com menu) |
| `POST` | `/api/restaurants` | Cria novo restaurante |
| `PUT` | `/api/restaurants/{id}` | Atualiza restaurante |
| `DELETE` | `/api/restaurants/{id}` | Remove restaurante |

### Menu

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/api/restaurants/menu/item/{itemId}` | Busca item específico com contexto |

### Exemplos de Uso

#### 1. Criar Restaurante

```bash
curl -X POST http://localhost:8081/api/restaurants \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Restaurante do João",
    "address": "Rua das Flores, 123",
    "kitchenType": {
      "id": "550e8400-e29b-41d4-a716-446655440001",
      "name": "Japonesa",
      "description": "Cozinha Japonesa"
    },
    "daysOperation": [
      {
        "day": "MONDAY",
        "openingHours": "08:00",
        "closingHours": "18:00"
      }
    ],
    "ownerId": "550e8400-e29b-41d4-a716-446655440002",
    "isActive": true,
    "menu": [
      {
        "type": "Lanche",
        "items": [
          {
            "name": "Hambúrguer Artesanal",
            "description": "Hambúrguer com carne artesanal",
            "price": 25.90,
            "onlyForLocalConsumption": false,
            "imagePath": "/images/hamburguer.jpg",
            "isActive": true
          }
        ]
      }
    ]
  }'
```

#### 2. Listar Restaurantes (sem menu)

```bash
curl -X GET http://localhost:8081/api/restaurants
```

#### 3. Buscar Item Específico

```bash
curl -X GET http://localhost:8081/api/restaurants/menu/item/{itemId}
```

## 🚀 Como Executar

### Pré-requisitos

- Java 21+
- MongoDB 4.4+
- Maven 3.8+ ou Docker

### Opção 1: Execução Local

1. **Clone o repositório**
```bash
git clone https://github.com/itmoura/fiap-tech-challenge-restaurants.git
cd fiap-tech-challenge-restaurants
```

2. **Configure o MongoDB**
```bash
# Inicie o MongoDB localmente ou use Docker
docker run -d -p 27017:27017 --name mongodb mongo:latest
```

3. **Execute a aplicação**
```bash
# Com Maven
./gradlew bootRun

# Ou compile e execute
./gradlew build
java -jar build/libs/tech-challenge-restaurant-0.0.1-SNAPSHOT.jar
```

### Opção 2: Docker Compose

```bash
# Execute com Docker Compose
docker-compose up -d
```

### Opção 3: Script de Execução

```bash
# Use o script fornecido
chmod +x run.sh
./run.sh
```

A aplicação estará disponível em: `http://localhost:8081`

### Documentação da API

Acesse o Swagger UI em: `http://localhost:8081/swagger-ui.html`

## 🧪 Testes

### Executar Testes

```bash
# Todos os testes
./gradlew test

# Testes específicos
./gradlew test --tests RestaurantUseCaseTest

# Com relatório de cobertura
./gradlew test jacocoTestReport
```

### Estrutura de Testes

```
src/test/java/
├── application/
│   └── usecases/
│       ├── RestaurantUseCaseTest.java
│       ├── UserUseCaseTest.java
│       └── UserTypeUseCaseTest.java
└── integration/
    └── RestaurantIntegrationTest.java
```

### Cobertura de Testes

- ✅ **Casos de Uso**: Testes unitários completos
- ✅ **Validações**: Testes de regras de negócio
- ✅ **Exceções**: Cenários de erro
- ✅ **Integração**: Testes end-to-end

## 🛠️ Tecnologias Utilizadas

### Backend
- **Spring Boot 3.5.4**: Framework principal
- **Spring Data MongoDB**: Integração com MongoDB
- **Spring Validation**: Validação de dados
- **Lombok**: Redução de boilerplate

### Banco de Dados
- **MongoDB**: Banco NoSQL orientado a documentos
- **UUID**: Identificadores únicos

### Documentação
- **SpringDoc OpenAPI**: Documentação automática da API
- **Swagger UI**: Interface interativa da API

### Testes
- **JUnit 5**: Framework de testes
- **Mockito**: Mocks para testes unitários
- **Spring Boot Test**: Testes de integração

### DevOps
- **Docker**: Containerização
- **Docker Compose**: Orquestração de containers
- **Gradle**: Gerenciamento de dependências

## 📈 Performance e Otimizações

### Estratégias Implementadas

1. **Estrutura Aninhada**: Menu integrado ao documento do restaurante
2. **Índices Automáticos**: Configuração para criação automática de índices
3. **Consultas Específicas**: Endpoints otimizados para diferentes necessidades
4. **UUID Nativo**: Conversores customizados para melhor performance

### Métricas de Performance

- **Consulta Básica**: ~5ms (restaurantes sem menu)
- **Consulta Completa**: ~15ms (restaurantes com menu)
- **Busca por Item**: ~10ms (item específico com contexto)

## 🔒 Regras de Negócio

### Restaurantes
- Nome é obrigatório
- Endereço é obrigatório
- Tipo de cozinha é obrigatório
- Horários de funcionamento são obrigatórios
- ID do proprietário é obrigatório
- Restaurante é ativo por padrão

### Menu
- Categorias têm ID único (UUID)
- Itens têm ID único (UUID)
- Preços são obrigatórios e positivos
- Itens são ativos por padrão

## 📝 Próximos Passos

- [ ] Implementar autenticação e autorização
- [ ] Adicionar sistema de avaliações
- [ ] Implementar cache Redis
- [ ] Adicionar métricas com Micrometer
- [ ] Implementar versionamento da API
- [ ] Adicionar testes de carga

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 👥 Autores

- **Italo Moura** - *Desenvolvimento inicial* - [@itmoura](https://github.com/itmoura)

---

⭐ **Se este projeto foi útil para você, considere dar uma estrela!**
