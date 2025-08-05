# 🐳 Execução com Docker

Este guia mostra como executar o projeto usando Docker e Docker Compose, proporcionando um ambiente isolado e consistente.

## 🚀 Quick Start

### Execução Completa

```bash
# Clone o repositório
git clone https://github.com/itmoura/fiap-tech-challenge-restaurants.git
cd fiap-tech-challenge-restaurants

# Execute tudo com Docker Compose
docker-compose up -d

# Verificar se os containers estão rodando
docker ps
```

A aplicação estará disponível em:
- **API**: http://localhost:8081
- **Swagger UI**: http://localhost:8081/swagger-ui.html
- **MongoDB**: localhost:27017

## 📋 Estrutura do Docker Compose

O arquivo `docker-compose.yml` define dois serviços principais:

```yaml
version: '3.8'

services:
  mongodb:
    image: mongo:7.0
    container_name: fiap-mongodb
    restart: always
    ports:
      - "27017:27017"
    environment:
      MONGO_INITDB_ROOT_USERNAME: admin
      MONGO_INITDB_ROOT_PASSWORD: admin123
      MONGO_INITDB_DATABASE: tech_challenge
    volumes:
      - mongodb_data:/data/db
    networks:
      - fiap-network

  app:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: fiap-tech-challenge-app
    restart: always
    ports:
      - "8081:8081"
    environment:
      SPRING_PROFILES_ACTIVE: prod
      MONGO_URI: mongodb://admin:admin123@mongodb:27017/tech_challenge?authSource=admin
      APP_PORT: 8081
    depends_on:
      - mongodb
    networks:
      - fiap-network

volumes:
  mongodb_data:

networks:
  fiap-network:
    driver: bridge
```

## 🔧 Comandos Docker Essenciais

### Gerenciamento de Containers

```bash
# Iniciar todos os serviços
docker-compose up -d

# Iniciar apenas um serviço específico
docker-compose up -d mongodb
docker-compose up -d app

# Parar todos os serviços
docker-compose down

# Parar e remover volumes
docker-compose down -v

# Reiniciar serviços
docker-compose restart

# Reiniciar serviço específico
docker-compose restart app
```

### Logs e Monitoramento

```bash
# Ver logs de todos os serviços
docker-compose logs

# Ver logs de um serviço específico
docker-compose logs app
docker-compose logs mongodb

# Seguir logs em tempo real
docker-compose logs -f app

# Ver logs das últimas 100 linhas
docker-compose logs --tail=100 app
```

### Build e Rebuild

```bash
# Rebuild da aplicação
docker-compose build app

# Rebuild forçado (sem cache)
docker-compose build --no-cache app

# Rebuild e restart
docker-compose up -d --build app
```

## 🏗️ Dockerfile da Aplicação

O projeto usa um Dockerfile multi-stage para otimizar o tamanho da imagem:

```dockerfile
# Build stage
FROM gradle:8.5-jdk21 AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY src ./src
RUN gradle clean build -x test

# Runtime stage
FROM openjdk:21-jre-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
```

## ⚙️ Configurações Avançadas

### Variáveis de Ambiente

Você pode personalizar a execução através de variáveis de ambiente:

```bash
# Criar arquivo .env
cat > .env << EOF
SPRING_PROFILES_ACTIVE=prod
MONGO_URI=mongodb://admin:admin123@mongodb:27017/tech_challenge?authSource=admin
APP_PORT=8081
MONGO_ROOT_USERNAME=admin
MONGO_ROOT_PASSWORD=admin123
EOF

# Executar com variáveis personalizadas
docker-compose --env-file .env up -d
```

### Configuração de Rede

```bash
# Criar rede personalizada
docker network create fiap-custom-network

# Usar rede personalizada no docker-compose.yml
networks:
  default:
    external:
      name: fiap-custom-network
```

### Volumes Persistentes

```bash
# Listar volumes
docker volume ls

# Inspecionar volume do MongoDB
docker volume inspect fiap-tech-challenge-restaurants_mongodb_data

# Backup do volume
docker run --rm -v fiap-tech-challenge-restaurants_mongodb_data:/data -v $(pwd):/backup alpine tar czf /backup/mongodb-backup.tar.gz -C /data .

# Restaurar backup
docker run --rm -v fiap-tech-challenge-restaurants_mongodb_data:/data -v $(pwd):/backup alpine tar xzf /backup/mongodb-backup.tar.gz -C /data
```

## 🔍 Verificação e Testes

### Health Checks

```bash
# Verificar saúde da aplicação
curl http://localhost:8081/actuator/health

# Verificar se MongoDB está acessível
docker exec fiap-mongodb mongosh --eval "db.adminCommand('ping')"

# Testar conectividade entre containers
docker exec fiap-tech-challenge-app ping mongodb
```

### Testes de API

```bash
# Testar endpoint básico
curl -X GET "http://localhost:8081/api/kitchen-types" \
     -H "accept: application/json"

# Criar um tipo de cozinha
curl -X POST "http://localhost:8081/api/kitchen-types" \
     -H "accept: application/json" \
     -H "Content-Type: application/json" \
     -d '{
       "name": "Italiana",
       "description": "Culinária tradicional italiana"
     }'
```

## 🐛 Troubleshooting

### Problemas Comuns

#### 1. Container não inicia

```bash
# Verificar logs detalhados
docker-compose logs app

# Verificar se a porta está em uso
netstat -tulpn | grep 8081

# Verificar recursos do sistema
docker system df
docker system prune
```

#### 2. Erro de conexão com MongoDB

```bash
# Verificar se MongoDB está rodando
docker ps | grep mongodb

# Testar conexão direta
docker exec -it fiap-mongodb mongosh

# Verificar logs do MongoDB
docker-compose logs mongodb
```

#### 3. Problemas de build

```bash
# Limpar cache do Docker
docker builder prune

# Rebuild completo
docker-compose build --no-cache

# Verificar espaço em disco
docker system df
```

#### 4. Problemas de rede

```bash
# Listar redes
docker network ls

# Inspecionar rede
docker network inspect fiap-tech-challenge-restaurants_fiap-network

# Testar conectividade
docker exec fiap-tech-challenge-app nslookup mongodb
```

## 🔧 Scripts Utilitários

### Script de Deploy Completo

```bash
#!/bin/bash
# deploy.sh

set -e

echo "🚀 Iniciando deploy com Docker..."

# Parar containers existentes
echo "⏹️  Parando containers..."
docker-compose down

# Limpar imagens antigas
echo "🧹 Limpando imagens antigas..."
docker image prune -f

# Build e start
echo "🏗️  Building e iniciando containers..."
docker-compose up -d --build

# Aguardar inicialização
echo "⏳ Aguardando inicialização..."
sleep 30

# Verificar saúde
echo "🏥 Verificando saúde da aplicação..."
curl -f http://localhost:8081/actuator/health || exit 1

echo "✅ Deploy concluído com sucesso!"
echo "📱 Swagger UI: http://localhost:8081/swagger-ui.html"
```

### Script de Backup

```bash
#!/bin/bash
# backup.sh

BACKUP_DIR="./backups/$(date +%Y%m%d_%H%M%S)"
mkdir -p $BACKUP_DIR

echo "💾 Criando backup..."

# Backup do MongoDB
docker exec fiap-mongodb mongodump --out /tmp/backup
docker cp fiap-mongodb:/tmp/backup $BACKUP_DIR/mongodb

# Backup dos logs
docker-compose logs > $BACKUP_DIR/application.log

echo "✅ Backup criado em: $BACKUP_DIR"
```

### Script de Monitoramento

```bash
#!/bin/bash
# monitor.sh

while true; do
    clear
    echo "📊 Status dos Containers - $(date)"
    echo "=================================="
    
    docker-compose ps
    
    echo -e "\n🏥 Health Check:"
    curl -s http://localhost:8081/actuator/health | jq .
    
    echo -e "\n💾 Uso de Recursos:"
    docker stats --no-stream --format "table {{.Container}}\t{{.CPUPerc}}\t{{.MemUsage}}"
    
    sleep 10
done
```

## 🌐 Deploy em Produção

### Configurações de Produção

```yaml
# docker-compose.prod.yml
version: '3.8'

services:
  mongodb:
    image: mongo:7.0
    restart: unless-stopped
    environment:
      MONGO_INITDB_ROOT_USERNAME: ${MONGO_ROOT_USERNAME}
      MONGO_INITDB_ROOT_PASSWORD: ${MONGO_ROOT_PASSWORD}
    volumes:
      - mongodb_data:/data/db
      - ./mongo-init:/docker-entrypoint-initdb.d
    networks:
      - backend
    # Não expor porta em produção
    # ports:
    #   - "27017:27017"

  app:
    build:
      context: .
      dockerfile: Dockerfile.prod
    restart: unless-stopped
    environment:
      SPRING_PROFILES_ACTIVE: prod
      MONGO_URI: mongodb://${MONGO_ROOT_USERNAME}:${MONGO_ROOT_PASSWORD}@mongodb:27017/tech_challenge?authSource=admin
    ports:
      - "8081:8081"
    depends_on:
      - mongodb
    networks:
      - backend
      - frontend
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8081/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 3

networks:
  backend:
    driver: bridge
  frontend:
    driver: bridge

volumes:
  mongodb_data:
    driver: local
```

### Deploy em Produção

```bash
# Deploy em produção
docker-compose -f docker-compose.prod.yml up -d

# Com variáveis de ambiente
export MONGO_ROOT_USERNAME=admin
export MONGO_ROOT_PASSWORD=secure_password
docker-compose -f docker-compose.prod.yml up -d
```

## 🚀 Próximos Passos

Após executar com Docker:

1. [Execute os testes](testing.md) para verificar a instalação
2. Explore a [documentação da API](../api/overview.md)
3. Configure [monitoramento em produção](../architecture/overview.md)
