# 🍽️ Tech Challenge - Sistema de Gestão de Restaurantes

## 📋 Descrição do Projeto

Este projeto é um **microserviço** de gestão para restaurantes desenvolvido como parte do Tech Challenge da FIAP. O sistema permite que restaurantes gerenciem suas operações de forma eficiente, incluindo cadastro de restaurantes, tipos de cozinha e itens do cardápio.

## 🏗️ Arquitetura do Sistema

### Visão Geral da Arquitetura

```mermaid
graph TB
    subgraph "Cliente"
        A[Web Browser]
        B[Mobile App]
        C[Postman/API Client]
    end
    
    subgraph "API Gateway"
        D[Load Balancer]
    end
    
    subgraph "Microserviço Restaurantes"
        E[Spring Boot Application]
    end
    
    subgraph "Banco de Dados"
        F[(MongoDB)]
    end
    
    subgraph "Outros Microserviços"
        G[MS Usuários]
        H[MS Pedidos]
        I[MS Pagamentos]
    end
    
    A --> D
    B --> D
    C --> D
    D --> E
    E --> F
    E -.-> G
    E -.-> H
    E -.-> I
    
    style E fill:#e1f5fe
    style F fill:#f3e5f5
    style G fill:#fff3e0
    style H fill:#fff3e0
    style I fill:#fff3e0
```

### Clean Architecture - Camadas

```mermaid
graph TB
    subgraph "🎯 Domain Layer"
        A[Entities]
        B[Exceptions]
    end
    
    subgraph "💼 Application Layer"
        C[Use Cases]
        D[DTOs]
        E[Ports/Interfaces]
    end
    
    subgraph "🌐 Presentation Layer"
        F[Controllers]
        G[Contracts/Interfaces]
        H[Exception Handlers]
    end
    
    subgraph "🔧 Infrastructure Layer"
        I[MongoDB Repositories]
        J[External APIs]
        K[Configurations]
    end
    
    F --> C
    G --> F
    H --> F
    C --> A
    C --> E
    I --> E
    J --> E
    K --> I
    
    style A fill:#ffebee
    style C fill:#e8f5e8
    style F fill:#e3f2fd
    style I fill:#fff3e0
```

### Fluxo de Dados

```mermaid
sequenceDiagram
    participant Client as 📱 Cliente
    participant Controller as 🎮 Controller
    participant UseCase as 💼 Use Case
    participant Repository as 🗄️ Repository
    participant MongoDB as 🍃 MongoDB
    
    Client->>Controller: HTTP Request
    Controller->>UseCase: Business Logic Call
    UseCase->>Repository: Data Access
    Repository->>MongoDB: Query/Command
    MongoDB-->>Repository: Data Response
    Repository-->>UseCase: Entity/Result
    UseCase-->>Controller: DTO Response
    Controller-->>Client: HTTP Response
```

## 🏛️ Estrutura de Camadas

### 📁 Organização do Código

```
src/main/java/com/fiap/itmoura/tech_challenge_restaurant/
├── 🎯 domain/                 # Camada de Domínio
│   ├── entities/             # Entidades de negócio
│   │   ├── RestaurantEntity.java
│   │   ├── KitchenTypeEntity.java
│   │   ├── MenuItemEntity.java
│   │   └── OperationDaysTimeData.java
│   └── exceptions/           # Exceções de domínio
│       ├── NotFoundException.java
│       ├── ConflictRequestException.java
│       └── BadRequestException.java
├── 💼 application/           # Camada de Aplicação
│   ├── models/              # DTOs e modelos
│   │   ├── restaurant/
│   │   ├── kitchentype/
│   │   └── menuitem/
│   ├── ports/               # Interfaces (Ports)
│   │   └── out/
│   │       ├── RestaurantRepository.java
│   │       ├── KitchenTypeRepository.java
│   │       └── MenuItemRepository.java
│   └── usecases/            # Casos de uso
│       ├── RestaurantUseCase.java
│       ├── KitchenTypeUseCase.java
│       └── MenuItemUseCase.java
└── 🌐 presentation/         # Camada de Apresentação
    ├── controllers/         # Controllers REST
    ├── contracts/           # Interfaces dos controllers
    └── handlers/            # Handlers de exceção
```

### 🔄 Padrões Arquiteturais Utilizados

#### **Clean Architecture**
- **Separação clara de responsabilidades**
- **Inversão de dependências**
- **Testabilidade**
- **Independência de frameworks**

#### **Hexagonal Architecture (Ports & Adapters)**
- **Ports**: Interfaces que definem contratos
- **Adapters**: Implementações concretas dos ports
- **Isolamento da lógica de negócio**

#### **CQRS (Command Query Responsibility Segregation)**
- **Separação entre comandos e consultas**
- **Otimização específica para cada operação**

## 🛠️ Tecnologias Utilizadas

### **Backend**
- ☕ **Java 21** - Linguagem de programação
- 🍃 **Spring Boot 3.5.4** - Framework principal
- 📊 **Spring Data MongoDB** - Persistência de dados
- ✅ **Spring Validation** - Validação de dados
- 🌐 **Spring Web** - APIs REST

### **Banco de Dados**
- 🍃 **MongoDB 7.0** - Banco de dados NoSQL
- 📝 **Collections**: restaurants, kitchen_types, menu_items

### **Ferramentas de Build e Deploy**
- 🐘 **Gradle 8.14.3** - Gerenciamento de dependências
- 🐳 **Docker & Docker Compose** - Containerização
- 📋 **Multi-stage Dockerfile** - Otimização de imagem

### **Documentação e Testes**
- 📚 **Swagger/OpenAPI 3** - Documentação da API
- 🧪 **JUnit 5** - Testes unitários
- 🎭 **Mockito** - Mocks para testes
- 📮 **Postman** - Testes de API

### **Monitoramento**
- 📊 **Spring Actuator** - Health checks e métricas
- 🔍 **Logging** - SLF4J + Logback

## 🚀 Funcionalidades Implementadas

### 🏪 **Gestão de Restaurantes**
```mermaid
graph LR
    A[📝 Criar] --> B[👁️ Consultar]
    B --> C[✏️ Atualizar]
    C --> D[🗑️ Deletar]
    D --> E[⏸️ Desativar]
    
    style A fill:#c8e6c9
    style B fill:#bbdefb
    style C fill:#fff9c4
    style D fill:#ffcdd2
    style E fill:#f8bbd9
```

**Campos do Restaurante:**
- 🏷️ Nome
- 📍 Endereço  
- 🍳 Tipo de cozinha
- ⏰ Horário de funcionamento
- 👤 ID do dono (referência externa)
- ⭐ Avaliação
- ✅ Status ativo/inativo

### 🍳 **Gestão de Tipos de Cozinha**
```mermaid
graph LR
    A[📝 Criar Tipo] --> B[👁️ Buscar]
    B --> C[✏️ Atualizar]
    C --> D[🗑️ Deletar]
    
    style A fill:#c8e6c9
    style B fill:#bbdefb
    style C fill:#fff9c4
    style D fill:#ffcdd2
```

**Campos do Tipo de Cozinha:**
- 🏷️ Nome (único)
- 📝 Descrição

### 🍽️ **Gestão de Itens do Cardápio**
```mermaid
graph LR
    A[📝 Criar Item] --> B[👁️ Listar]
    B --> C[✏️ Atualizar]
    C --> D[🔄 Ativar/Desativar]
    D --> E[🗑️ Deletar]
    E --> F[🔍 Filtrar]
    
    style A fill:#c8e6c9
    style B fill:#bbdefb
    style C fill:#fff9c4
    style D fill:#e1bee7
    style E fill:#ffcdd2
    style F fill:#dcedc8
```

**Campos do Item do Cardápio:**
- 🏷️ Nome
- 📝 Descrição
- 💰 Preço
- 🏪 Disponibilidade apenas no local
- 📸 Caminho da imagem
- 🏪 ID do restaurante
- ✅ Status ativo/inativo

## 📡 Endpoints da API

### 🍳 Kitchen Types
```http
POST   /api/kitchen-types/create           # Criar tipo de cozinha
GET    /api/kitchen-types/{idOrName}       # Buscar por ID ou nome
PUT    /api/kitchen-types/{id}/update      # Atualizar tipo
DELETE /api/kitchen-types/{id}/delete      # Deletar tipo
```

### 🏪 Restaurants
```http
POST   /api/restaurants/create             # Criar restaurante
GET    /api/restaurants                    # Listar restaurantes ativos
GET    /api/restaurants/{id}               # Buscar por ID
PUT    /api/restaurants/{id}/update        # Atualizar restaurante
PATCH  /api/restaurants/{id}/disable       # Desativar restaurante
DELETE /api/restaurants/{id}/delete        # Deletar restaurante
```

### 🍽️ Menu Items
```http
POST   /api/menu-items/create              # Criar item do cardápio
GET    /api/menu-items                     # Listar itens (com filtros)
GET    /api/menu-items/{id}                # Buscar por ID
PUT    /api/menu-items/{id}/update         # Atualizar item
PATCH  /api/menu-items/{id}/toggle-status  # Ativar/desativar item
DELETE /api/menu-items/{id}/delete         # Deletar item
```

### 🔍 Filtros Disponíveis para Menu Items
```http
GET /api/menu-items?restaurantId={id}           # Por restaurante
GET /api/menu-items?activeOnly=true             # Apenas ativos
GET /api/menu-items?restaurantId={id}&activeOnly=true  # Combinado
```

## 🗄️ Modelo de Dados

### 📊 Diagrama de Entidades

```mermaid
erDiagram
    RESTAURANT {
        string id PK
        string name
        string address
        string ownerId
        boolean isActive
        double rating
        datetime createdAt
        datetime lastUpdate
    }
    
    KITCHEN_TYPE {
        string id PK
        string name UK
        string description
        datetime createdAt
        datetime lastUpdate
    }
    
    MENU_ITEM {
        string id PK
        string name
        string description
        decimal price
        boolean onlyForLocalConsumption
        string imagePath
        string restaurantId FK
        boolean isActive
        datetime createdAt
        datetime lastUpdate
    }
    
    OPERATION_DAYS {
        string day
        string openTime
        string closeTime
    }
    
    RESTAURANT ||--|| KITCHEN_TYPE : "has"
    RESTAURANT ||--o{ MENU_ITEM : "contains"
    RESTAURANT ||--o{ OPERATION_DAYS : "operates"
```

### 📝 Collections MongoDB

#### **restaurants**
```json
{
  "_id": "ObjectId",
  "name": "String",
  "address": "String", 
  "kitchenType": {
    "id": "String",
    "name": "String",
    "description": "String"
  },
  "daysOperation": [
    {
      "day": "MONDAY|TUESDAY|...",
      "openTime": "HH:mm",
      "closeTime": "HH:mm"
    }
  ],
  "ownerId": "String",
  "isActive": "Boolean",
  "rating": "Double",
  "createdAt": "DateTime",
  "lastUpdate": "DateTime"
}
```

#### **kitchen_types**
```json
{
  "_id": "ObjectId",
  "name": "String (unique)",
  "description": "String",
  "createdAt": "DateTime",
  "lastUpdate": "DateTime"
}
```

#### **menu_items**
```json
{
  "_id": "ObjectId",
  "name": "String",
  "description": "String",
  "price": "Decimal",
  "onlyForLocalConsumption": "Boolean",
  "imagePath": "String",
  "restaurantId": "String",
  "isActive": "Boolean",
  "createdAt": "DateTime",
  "lastUpdate": "DateTime"
}
```

## 🧪 Testes

### 📊 Cobertura de Testes

O projeto possui **cobertura abrangente de testes unitários** para os principais casos de uso:

```mermaid
graph TB
    subgraph "🧪 Test Coverage"
        A[KitchenTypeUseCaseTest]
        B[RestaurantUseCaseTest]
        C[MenuItemUseCaseTest]
    end
    
    subgraph "📋 Test Categories"
        D[Create Tests]
        E[Read Tests]
        F[Update Tests]
        G[Delete Tests]
        H[Business Logic Tests]
        I[Exception Tests]
    end
    
    A --> D
    A --> E
    A --> F
    A --> G
    A --> H
    A --> I
    
    B --> D
    B --> E
    B --> F
    B --> G
    B --> H
    B --> I
    
    C --> D
    C --> E
    C --> F
    C --> G
    C --> H
    C --> I
    
    style A fill:#c8e6c9
    style B fill:#bbdefb
    style C fill:#fff9c4
```

### 🎯 Tipos de Testes Implementados

#### **Testes Unitários**
- ✅ **Create Operations** - Criação de entidades
- ✅ **Read Operations** - Consultas e buscas
- ✅ **Update Operations** - Atualizações
- ✅ **Delete Operations** - Remoções
- ✅ **Business Logic** - Regras de negócio
- ✅ **Exception Handling** - Tratamento de erros
- ✅ **Validation Tests** - Validações de entrada
- ✅ **Edge Cases** - Casos extremos

#### **Estrutura dos Testes**
```java
@ExtendWith(MockitoExtension.class)
@DisplayName("UseCase Tests")
class UseCaseTest {
    
    @Nested
    @DisplayName("Create Tests")
    class CreateTests { /* ... */ }
    
    @Nested
    @DisplayName("Read Tests") 
    class ReadTests { /* ... */ }
    
    // ... outros grupos de testes
}
```

### 🚀 Executando os Testes

```bash
# Executar todos os testes
./run.sh test

# Ou usando Gradle diretamente
./gradlew test

# Gerar relatório de cobertura
./gradlew test jacocoTestReport
```

## 🐳 Configuração e Execução

### 📋 Pré-requisitos

- 🐳 **Docker** e **Docker Compose**
- ☕ **Java 21** (para desenvolvimento local)
- 🐘 **Gradle** (para desenvolvimento local)

### 🚀 Execução com Docker Compose

#### **1. Clone o repositório:**
```bash
git clone https://github.com/itmoura/fiap-tech-challenge-restaurants.git
cd fiap-tech-challenge-restaurants
git checkout feature/novas-funcionalidades
```

#### **2. Execute a aplicação:**
```bash
# Usando o script helper
./run.sh start

# Ou diretamente com docker-compose
docker-compose up -d
```

#### **3. Acesse os serviços:**
- 🌐 **API**: http://localhost:8081
- 📚 **Swagger UI**: http://localhost:8081/swagger-ui.html
- ❤️ **Health Check**: http://localhost:8081/actuator/health
- 🍃 **MongoDB**: localhost:27017

### 🛠️ Desenvolvimento Local

#### **1. Inicie o MongoDB:**
```bash
docker run -d -p 27017:27017 --name mongodb mongo:7.0
```

#### **2. Execute a aplicação:**
```bash
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
./gradlew bootRun
```

### 📋 Script Helper

O projeto inclui um script `run.sh` para facilitar o gerenciamento:

```bash
./run.sh start     # Inicia a aplicação
./run.sh stop      # Para a aplicação  
./run.sh restart   # Reinicia a aplicação
./run.sh logs      # Mostra os logs
./run.sh test      # Executa os testes
./run.sh build     # Faz o build
./run.sh clean     # Limpa containers e volumes
./run.sh help      # Mostra ajuda
```

## 📚 Documentação da API

### 🌐 Swagger UI

A documentação completa da API está disponível através do **Swagger UI**:

**URL**: http://localhost:8081/swagger-ui.html

### 📮 Collection do Postman

Importe a collection do Postman para testar os endpoints:

**Arquivo**: `postman/Tech-Challenge-Restaurants.postman_collection.json`

### 🔧 Variáveis de Ambiente

Configure as seguintes variáveis no Postman:

| Variável | Valor | Descrição |
|----------|-------|-----------|
| `baseUrl` | `http://localhost:8081` | URL base da API |
| `kitchenTypeId` | `{id}` | ID do tipo de cozinha |
| `restaurantId` | `{id}` | ID do restaurante |
| `menuItemId` | `{id}` | ID do item do cardápio |

## 🔍 Monitoramento e Observabilidade

### 📊 Health Checks

```bash
# Status geral da aplicação
curl http://localhost:8081/actuator/health

# Métricas detalhadas
curl http://localhost:8081/actuator/metrics
```

### 📝 Logs

```bash
# Ver logs em tempo real
./run.sh logs

# Ou com docker-compose
docker-compose logs -f app
```

### 🎯 Métricas Disponíveis

- ❤️ **Health Status** - Status da aplicação e dependências
- 🍃 **MongoDB Connection** - Status da conexão com o banco
- 📊 **JVM Metrics** - Métricas da JVM
- 🌐 **HTTP Metrics** - Métricas das requisições HTTP

## 🔒 Segurança e Boas Práticas

### 🛡️ Validações Implementadas

- ✅ **Validação de entrada** com Bean Validation
- ✅ **Tratamento de exceções** centralizado
- ✅ **Validação de integridade referencial**
- ✅ **Sanitização de dados**

### 🏗️ Padrões de Código

- ✅ **Clean Code** principles
- ✅ **SOLID** principles  
- ✅ **DRY** (Don't Repeat Yourself)
- ✅ **KISS** (Keep It Simple, Stupid)
- ✅ **Conventional Commits**

### 🔐 Configurações de Segurança

- 🐳 **Non-root user** no Docker
- 🔒 **Minimal base image**
- 📊 **Health checks** configurados
- 🚫 **Sensitive data** não exposta

## 🚀 Deploy e CI/CD

### 🐳 Docker

```bash
# Build da imagem
docker build -t tech-challenge-restaurants .

# Executar container
docker run -p 8081:8081 tech-challenge-restaurants
```

### 🔄 Pipeline Sugerido

```mermaid
graph LR
    A[📝 Code] --> B[🧪 Tests]
    B --> C[🔨 Build]
    C --> D[🐳 Docker Build]
    D --> E[📊 Security Scan]
    E --> F[🚀 Deploy]
    
    style A fill:#c8e6c9
    style B fill:#fff9c4
    style C fill:#bbdefb
    style D fill:#e1bee7
    style E fill:#ffcdd2
    style F fill:#dcedc8
```

## 🤝 Contribuição

### 📋 Como Contribuir

1. 🍴 **Fork** o projeto
2. 🌿 **Crie uma branch** (`git checkout -b feature/nova-funcionalidade`)
3. 💾 **Commit** suas mudanças (`git commit -am 'feat: adiciona nova funcionalidade'`)
4. 📤 **Push** para a branch (`git push origin feature/nova-funcionalidade`)
5. 🔄 **Abra um Pull Request**

### 📝 Padrões de Commit

Utilizamos **Conventional Commits**:

```
feat: adiciona nova funcionalidade
fix: corrige bug específico
docs: atualiza documentação
test: adiciona ou modifica testes
refactor: refatora código sem alterar funcionalidade
```

## 📄 Licença

Este projeto está licenciado sob a **MIT License** - veja o arquivo [LICENSE](LICENSE) para detalhes.

## 👥 Contato

- **Desenvolvedor**: Italo Moura
- **GitHub**: https://github.com/itmoura
- **Projeto**: https://github.com/itmoura/fiap-tech-challenge-restaurants

---

<div align="center">

**🍽️ Tech Challenge - Sistema de Gestão de Restaurantes**

*Desenvolvido com ❤️ para o Tech Challenge da FIAP*

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MongoDB](https://img.shields.io/badge/MongoDB-7.0-green.svg)](https://www.mongodb.com/)
[![Docker](https://img.shields.io/badge/Docker-Enabled-blue.svg)](https://www.docker.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

</div>
