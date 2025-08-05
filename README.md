# Tech Challenge - Sistema de Gestão de Restaurantes

## Descrição do Projeto

Este projeto é um sistema de gestão para restaurantes desenvolvido como parte do Tech Challenge da FIAP. O sistema permite que restaurantes gerenciem suas operações de forma eficiente, enquanto os clientes podem consultar informações, deixar avaliações e fazer pedidos online.

## Arquitetura

O projeto segue os princípios da **Clean Architecture**, organizando o código em camadas bem definidas:

### Estrutura de Camadas

```
src/main/java/com/fiap/itmoura/tech_challenge_restaurant/
├── domain/                 # Camada de Domínio
│   ├── entities/          # Entidades de negócio
│   └── exceptions/        # Exceções de domínio
├── application/           # Camada de Aplicação
│   ├── models/           # DTOs e modelos de aplicação
│   ├── ports/            # Interfaces (Ports)
│   └── usecases/         # Casos de uso (Use Cases)
└── presentation/         # Camada de Apresentação
    ├── controllers/      # Controllers REST
    ├── contracts/        # Interfaces dos controllers
    └── handlers/         # Handlers de exceção
```

### Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.5.4**
- **Spring Data MongoDB**
- **MongoDB** como banco de dados
- **Gradle** para gerenciamento de dependências
- **Docker & Docker Compose** para containerização
- **Swagger/OpenAPI** para documentação da API
- **JUnit 5 & Mockito** para testes

## Funcionalidades Implementadas

### Fase 2

#### 1. Gestão de Tipos de Usuário
- **CRUD completo** para tipos de usuário
- Campos: Nome do Tipo
- Endpoints disponíveis:
  - `POST /api/user-types/create` - Criar tipo de usuário
  - `GET /api/user-types` - Listar todos os tipos
  - `GET /api/user-types/{idOrName}` - Buscar por ID ou nome
  - `PUT /api/user-types/{id}/update` - Atualizar tipo
  - `DELETE /api/user-types/{id}/delete` - Deletar tipo

#### 2. Gestão de Usuários
- **CRUD completo** para usuários
- Associação com tipos de usuário
- Campos: Nome, Email, Tipo de Usuário
- Endpoints disponíveis:
  - `POST /api/users/create` - Criar usuário
  - `GET /api/users` - Listar usuários (com filtro por tipo)
  - `GET /api/users/{id}` - Buscar usuário por ID
  - `PUT /api/users/{id}/update` - Atualizar usuário
  - `DELETE /api/users/{id}/delete` - Deletar usuário

#### 3. Cadastro de Restaurantes
- **CRUD completo** para restaurantes
- Associação com usuário (dono do restaurante)
- Campos: Nome, Endereço, Tipo de Cozinha, Horário de Funcionamento, Dono
- Validação de existência do usuário responsável

#### 4. Cadastro de Itens do Cardápio
- **CRUD completo** para itens do cardápio
- Campos: Nome, Descrição, Preço, Disponibilidade Local, Foto, Restaurante
- Funcionalidades extras:
  - Ativar/desativar itens
  - Filtrar por restaurante
  - Filtrar apenas itens ativos
- Endpoints disponíveis:
  - `POST /api/menu-items/create` - Criar item
  - `GET /api/menu-items` - Listar itens (com filtros)
  - `GET /api/menu-items/{id}` - Buscar item por ID
  - `PUT /api/menu-items/{id}/update` - Atualizar item
  - `PATCH /api/menu-items/{id}/toggle-status` - Ativar/desativar
  - `DELETE /api/menu-items/{id}/delete` - Deletar item

## Configuração e Execução

### Pré-requisitos

- Docker e Docker Compose instalados
- Java 21 (para desenvolvimento local)
- Gradle (para desenvolvimento local)

### Executando com Docker Compose

1. Clone o repositório:
```bash
git clone https://github.com/itmoura/fiap-tech-challenge-restaurants.git
cd fiap-tech-challenge-restaurants
```

2. Execute o projeto:
```bash
docker-compose up -d
```

3. A aplicação estará disponível em:
   - **API**: http://localhost:8081
   - **Swagger UI**: http://localhost:8081/swagger-ui.html
   - **MongoDB**: localhost:27017

### Executando Localmente

1. Inicie o MongoDB:
```bash
docker run -d -p 27017:27017 --name mongodb mongo:7.0
```

2. Execute a aplicação:
```bash
./gradlew bootRun
```

### Executando os Testes

```bash
# Executar todos os testes
./gradlew test

# Executar testes com relatório de cobertura
./gradlew test jacocoTestReport
```

## Documentação da API

A documentação completa da API está disponível através do Swagger UI em:
http://localhost:8081/swagger-ui.html

### Principais Endpoints

#### Tipos de Usuário
- `POST /api/user-types/create`
- `GET /api/user-types`
- `GET /api/user-types/{idOrName}`
- `PUT /api/user-types/{id}/update`
- `DELETE /api/user-types/{id}/delete`

#### Usuários
- `POST /api/users/create`
- `GET /api/users?userTypeId={id}`
- `GET /api/users/{id}`
- `PUT /api/users/{id}/update`
- `DELETE /api/users/{id}/delete`

#### Restaurantes
- `POST /api/restaurants/create`
- `GET /api/restaurants`
- `GET /api/restaurants/{id}`
- `PUT /api/restaurants/{id}/update`
- `DELETE /api/restaurants/{id}/delete`

#### Itens do Cardápio
- `POST /api/menu-items/create`
- `GET /api/menu-items?restaurantId={id}&activeOnly={true/false}`
- `GET /api/menu-items/{id}`
- `PUT /api/menu-items/{id}/update`
- `PATCH /api/menu-items/{id}/toggle-status`
- `DELETE /api/menu-items/{id}/delete`

## Estrutura do Banco de Dados

### Collections MongoDB

1. **user_types** - Tipos de usuário
2. **users** - Usuários do sistema
3. **restaurants** - Restaurantes cadastrados
4. **kitchen_types** - Tipos de cozinha
5. **menu_items** - Itens do cardápio

## Testes

O projeto possui cobertura de testes unitários para os principais casos de uso:

- **UserTypeUseCaseTest** - Testes para gestão de tipos de usuário
- **UserUseCaseTest** - Testes para gestão de usuários
- **MenuItemUseCaseTest** - Testes para gestão de itens do cardápio

### Executar Testes com Cobertura

```bash
./gradlew test jacocoTestReport
```

O relatório de cobertura será gerado em `build/reports/jacoco/test/html/index.html`

## Monitoramento e Health Check

A aplicação possui endpoints de monitoramento:

- **Health Check**: `/actuator/health`
- **Métricas**: `/actuator/metrics`

## Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-funcionalidade`)
3. Commit suas mudanças (`git commit -am 'Adiciona nova funcionalidade'`)
4. Push para a branch (`git push origin feature/nova-funcionalidade`)
5. Abra um Pull Request

## Licença

Este projeto está licenciado sob a MIT License - veja o arquivo [LICENSE](LICENSE) para detalhes.

## Contato

- **Desenvolvedor**: Italo Moura
- **GitHub**: https://github.com/itmoura
- **Projeto**: https://github.com/itmoura/fiap-tech-challenge-restaurants
