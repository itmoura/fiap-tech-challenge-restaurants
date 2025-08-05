# 📝 Changelog

Todas as mudanças notáveis neste projeto serão documentadas neste arquivo.

O formato é baseado em [Keep a Changelog](https://keepachangelog.com/pt-BR/1.0.0/),
e este projeto adere ao [Versionamento Semântico](https://semver.org/lang/pt-BR/).

## [Não Lançado]

### 🔄 Em Desenvolvimento
- Sistema de autenticação e autorização
- Filtros avançados para busca de restaurantes
- Sistema de avaliações e comentários
- Integração com APIs de pagamento
- Notificações em tempo real

## [1.0.0] - 2024-08-05

### 🎉 Primeira Versão Estável

#### ✨ Adicionado
- **API completa de gerenciamento de restaurantes**
  - CRUD completo para restaurantes
  - Informações básicas (nome, endereço, contato)
  - Horários de funcionamento
  - Associação com tipos de cozinha

- **Sistema de tipos de cozinha**
  - CRUD completo para kitchen types
  - Validação de unicidade de nomes
  - Proteção contra exclusão quando em uso

- **Gerenciamento de menu estruturado**
  - Categorias de menu (Entradas, Pratos Principais, etc.)
  - Itens do menu com preços e descrições
  - Informações nutricionais e alérgenos
  - Tags para classificação (vegetariano, vegano, etc.)

- **Arquitetura Clean Architecture**
  - Separação clara de responsabilidades
  - Inversão de dependências
  - Alta testabilidade
  - Independência de frameworks

- **Modelagem NoSQL otimizada**
  - Documentos aninhados para performance
  - Índices estratégicos
  - Consultas otimizadas

- **Documentação completa**
  - OpenAPI/Swagger integrado
  - Documentação técnica detalhada
  - Exemplos práticos de uso
  - Guias de instalação

- **Testes automatizados**
  - Testes unitários com alta cobertura
  - Testes de integração com Testcontainers
  - Testes BDD com Cucumber

- **Containerização**
  - Dockerfile otimizado multi-stage
  - Docker Compose para desenvolvimento
  - Configurações para produção

#### 🛠️ Tecnologias Utilizadas
- Java 21
- Spring Boot 3.5.4
- MongoDB 7.0
- Gradle 8.x
- Docker & Docker Compose
- JUnit 5 & Mockito
- OpenAPI 3.0

## [0.3.0] - 2024-08-04

### ✨ Adicionado
- **Implementação de Menu Items API**
  - Criação, leitura, atualização e exclusão de itens
  - Busca de item por ID com contexto completo
  - Validações de negócio para preços e disponibilidade
  - Suporte a alérgenos e tags

- **Melhorias na modelagem de dados**
  - Campos adicionais para itens (tempo de preparo, disponibilidade)
  - Informações nutricionais opcionais
  - Sistema de tags flexível

#### 🔧 Alterado
- Refatoração dos mappers para melhor performance
- Otimização das consultas MongoDB
- Melhoria na estrutura de exceções

#### 🐛 Corrigido
- Validação de horários de funcionamento
- Tratamento de casos edge em consultas aninhadas

## [0.2.0] - 2024-08-03

### ✨ Adicionado
- **Menu Categories API**
  - CRUD completo para categorias de menu
  - Integração com restaurantes
  - Validações de negócio

- **Estrutura aninhada no MongoDB**
  - Menu categories como subdocumentos
  - Otimização para consultas de leitura
  - Manutenção da consistência transacional

#### 🔧 Alterado
- Migração de estrutura relacional para aninhada
- Refatoração dos use cases para nova estrutura
- Atualização da documentação da API

## [0.1.0] - 2024-08-02

### ✨ Adicionado
- **Estrutura inicial do projeto**
  - Configuração Spring Boot
  - Integração com MongoDB
  - Arquitetura Clean Architecture básica

- **Restaurants API**
  - CRUD básico para restaurantes
  - Validações de entrada
  - Tratamento de exceções

- **Kitchen Types API**
  - Gerenciamento de tipos de cozinha
  - Validação de unicidade
  - Relacionamento com restaurantes

- **Configuração de desenvolvimento**
  - Docker Compose para MongoDB
  - Profiles de configuração
  - Logging estruturado

#### 🛠️ Infraestrutura
- Configuração inicial do repositório
- Estrutura de pacotes
- Configurações de build com Gradle

## [0.0.1] - 2024-08-01

### 🎯 Projeto Inicial
- Criação do repositório
- Configuração inicial do Spring Boot
- Estrutura básica de pacotes
- README inicial

---

## 🔮 Roadmap Futuro

### v1.1.0 - Melhorias de Performance
- [ ] Cache Redis para consultas frequentes
- [ ] Otimização de índices MongoDB
- [ ] Compressão de responses
- [ ] Paginação avançada

### v1.2.0 - Funcionalidades Avançadas
- [ ] Sistema de busca textual
- [ ] Filtros geográficos
- [ ] Recomendações personalizadas
- [ ] Sistema de favoritos

### v1.3.0 - Integração e APIs
- [ ] Integração com APIs de delivery
- [ ] Sistema de reservas
- [ ] Integração com redes sociais
- [ ] API de analytics

### v2.0.0 - Arquitetura Distribuída
- [ ] Migração para microservices
- [ ] Event Sourcing
- [ ] CQRS implementation
- [ ] Service mesh

## 📊 Estatísticas de Desenvolvimento

### Commits por Versão
- **v1.0.0**: 45 commits
- **v0.3.0**: 28 commits
- **v0.2.0**: 22 commits
- **v0.1.0**: 15 commits

### Linhas de Código
- **Total**: ~8,500 linhas
- **Código fonte**: ~6,200 linhas
- **Testes**: ~2,300 linhas
- **Cobertura de testes**: 85%+

### Arquivos por Tipo
- **Java**: 45 arquivos
- **YAML/Properties**: 8 arquivos
- **Markdown**: 15 arquivos
- **Docker**: 3 arquivos

## 🤝 Contribuidores

### Principais Contribuidores
- **Ítalo Moura** ([@itmoura](https://github.com/itmoura)) - Autor principal

### Como Contribuir
Veja nosso [guia de contribuição](../installation/local-setup.md) para saber como participar do desenvolvimento.

## 🏷️ Convenções de Versionamento

Este projeto segue o [Versionamento Semântico](https://semver.org/):

- **MAJOR** (X.0.0): Mudanças incompatíveis na API
- **MINOR** (0.X.0): Funcionalidades adicionadas de forma compatível
- **PATCH** (0.0.X): Correções de bugs compatíveis

### Tipos de Mudanças
- **✨ Adicionado**: Para novas funcionalidades
- **🔧 Alterado**: Para mudanças em funcionalidades existentes
- **❌ Descontinuado**: Para funcionalidades que serão removidas
- **🗑️ Removido**: Para funcionalidades removidas
- **🐛 Corrigido**: Para correções de bugs
- **🔒 Segurança**: Para correções de vulnerabilidades

## 📅 Cronograma de Releases

### Ciclo de Release
- **Major releases**: A cada 6-12 meses
- **Minor releases**: A cada 1-2 meses
- **Patch releases**: Conforme necessário

### Próximas Datas
- **v1.1.0**: Setembro 2024
- **v1.2.0**: Novembro 2024
- **v2.0.0**: Q1 2025

## 🔗 Links Úteis

- [Repositório GitHub](https://github.com/itmoura/fiap-tech-challenge-restaurants)
- [Issues](https://github.com/itmoura/fiap-tech-challenge-restaurants/issues)
- [Pull Requests](https://github.com/itmoura/fiap-tech-challenge-restaurants/pulls)
- [Releases](https://github.com/itmoura/fiap-tech-challenge-restaurants/releases)

---

<div align="center">
  <p><strong>Mantenha-se atualizado!</strong></p>
  <p>⭐ Favorite o repositório para receber notificações de novas versões</p>
</div>
