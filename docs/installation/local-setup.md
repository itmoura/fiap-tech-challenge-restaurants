# 🏠 Configuração Local

Este guia mostra como configurar e executar o projeto localmente para desenvolvimento.

## 📥 Clonando o Repositório

```bash
# Clone o repositório
git clone https://github.com/itmoura/fiap-tech-challenge-restaurants.git

# Navegue para o diretório
cd fiap-tech-challenge-restaurants

# Verifique a branch atual
git branch
```

## 🗄️ Configuração do Banco de Dados

### Opção 1: MongoDB via Docker (Recomendado)

```bash
# Executar apenas o MongoDB
docker-compose up -d mongodb

# Verificar se está rodando
docker ps
```

### Opção 2: MongoDB Local

Se você tem MongoDB instalado localmente:

```bash
# Iniciar MongoDB
sudo systemctl start mongod

# Verificar status
sudo systemctl status mongod

# Conectar ao MongoDB
mongosh
```

## ⚙️ Configuração da Aplicação

### Variáveis de Ambiente

Crie um arquivo `.env` na raiz do projeto (opcional):

```bash
# .env
SPRING_PROFILES_ACTIVE=develop
MONGO_URI=mongodb://localhost:27017/tech_challenge_restaurants
APP_PORT=8081
```

### Perfis de Configuração

O projeto possui dois perfis principais:

=== "Desenvolvimento (develop)"
    
    ```yaml
    # application.yml - perfil develop
    spring:
      profiles:
        active: develop
      data:
        mongodb:
          uri: mongodb://localhost:27017/tech_challenge_restaurants
          auto-index-creation: true
    
    server:
      port: 8081
    
    logging:
      level:
        com.fiap.itmoura.tech_challenge_restaurant: DEBUG
    ```

=== "Produção (prod)"
    
    ```yaml
    # application.yml - perfil prod
    spring:
      profiles:
        active: prod
      data:
        mongodb:
          uri: mongodb://admin:admin123@mongodb:27017/tech_challenge_restaurants?authSource=admin
          auto-index-creation: false
    
    logging:
      level:
        com.fiap.itmoura.tech_challenge_restaurant: INFO
    ```

## 🔨 Compilação e Execução

### Usando Gradle Wrapper (Recomendado)

```bash
# Dar permissão de execução (Linux/macOS)
chmod +x gradlew

# Limpar e compilar
./gradlew clean build

# Executar a aplicação
./gradlew bootRun

# Ou executar com perfil específico
./gradlew bootRun --args='--spring.profiles.active=develop'
```

### Usando Gradle Instalado

```bash
# Compilar
gradle clean build

# Executar
gradle bootRun
```

### Executando o JAR

```bash
# Compilar e gerar JAR
./gradlew clean build

# Executar o JAR
java -jar build/libs/tech-challenge-restaurant-0.0.1-SNAPSHOT.jar

# Com perfil específico
java -jar build/libs/tech-challenge-restaurant-0.0.1-SNAPSHOT.jar --spring.profiles.active=develop
```

## 🚀 Verificação da Instalação

### 1. Verificar se a aplicação está rodando

```bash
# Verificar logs
curl http://localhost:8081/actuator/health

# Resposta esperada:
{
  "status": "UP",
  "components": {
    "mongo": {
      "status": "UP"
    }
  }
}
```

### 2. Acessar Swagger UI

Abra seu navegador e acesse:

```
http://localhost:8081/swagger-ui.html
```

### 3. Testar endpoint básico

```bash
# Listar tipos de cozinha
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

## 🔧 Configurações Avançadas

### Configuração de Logging

Para ajustar o nível de logging, edite o `application.yml`:

```yaml
logging:
  level:
    root: INFO
    com.fiap.itmoura.tech_challenge_restaurant: DEBUG
    org.springframework.data.mongodb: DEBUG
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
  file:
    name: logs/application.log
```

### Configuração de CORS (se necessário)

```java
@Configuration
public class CorsConfig {
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
```

## 🛠️ Ferramentas de Desenvolvimento

### Hot Reload com Spring Boot DevTools

Adicione ao `build.gradle`:

```gradle
dependencies {
    developmentOnly 'org.springframework.boot:spring-boot-devtools'
}
```

### Configuração do IDE

=== "IntelliJ IDEA"
    
    1. Importe o projeto como projeto Gradle
    2. Configure o SDK para Java 21
    3. Habilite "Build project automatically"
    4. Configure Run Configuration:
       - Main class: `TechChallengeRestaurantApplication`
       - VM options: `-Dspring.profiles.active=develop`

=== "VS Code"
    
    1. Instale as extensões Java
    2. Configure `.vscode/launch.json`:
    
    ```json
    {
      "version": "0.2.0",
      "configurations": [
        {
          "type": "java",
          "name": "Launch Application",
          "request": "launch",
          "mainClass": "com.fiap.itmoura.tech_challenge_restaurant.TechChallengeRestaurantApplication",
          "projectName": "tech-challenge-restaurant",
          "args": "--spring.profiles.active=develop"
        }
      ]
    }
    ```

=== "Eclipse"
    
    1. Import → Existing Gradle Project
    2. Configure Build Path para Java 21
    3. Run Configuration:
       - Main class: `TechChallengeRestaurantApplication`
       - Program arguments: `--spring.profiles.active=develop`

## 📊 Monitoramento Local

### Actuator Endpoints

A aplicação expõe vários endpoints de monitoramento:

```bash
# Health check
curl http://localhost:8081/actuator/health

# Métricas
curl http://localhost:8081/actuator/metrics

# Info da aplicação
curl http://localhost:8081/actuator/info

# Prometheus metrics
curl http://localhost:8081/actuator/prometheus
```

### Logs da Aplicação

```bash
# Seguir logs em tempo real
tail -f logs/application.log

# Filtrar logs por nível
grep "ERROR" logs/application.log

# Logs do MongoDB
grep "MongoTemplate" logs/application.log
```

## 🐛 Troubleshooting

### Problemas Comuns

#### 1. Erro de conexão com MongoDB

```bash
# Verificar se MongoDB está rodando
docker ps | grep mongo

# Verificar logs do MongoDB
docker logs fiap-mongodb

# Testar conexão
mongosh mongodb://localhost:27017/tech_challenge_restaurants
```

#### 2. Porta 8081 já em uso

```bash
# Encontrar processo usando a porta
lsof -i :8081

# Matar processo
kill -9 <PID>

# Ou alterar porta no application.yml
server:
  port: 8082
```

#### 3. Erro de permissão no gradlew

```bash
# Dar permissão de execução
chmod +x gradlew

# Verificar permissões
ls -la gradlew
```

#### 4. Erro de versão do Java

```bash
# Verificar versão atual
java -version

# Configurar JAVA_HOME
export JAVA_HOME=/path/to/java21
export PATH=$JAVA_HOME/bin:$PATH

# Verificar no Gradle
./gradlew -version
```

### Logs de Debug

Para habilitar logs detalhados:

```yaml
logging:
  level:
    com.fiap.itmoura.tech_challenge_restaurant: TRACE
    org.springframework.data.mongodb.core.MongoTemplate: DEBUG
    org.springframework.web: DEBUG
```

## 🔄 Reinicialização Rápida

Script para reiniciar rapidamente durante desenvolvimento:

```bash
#!/bin/bash
# restart-dev.sh

echo "🔄 Reiniciando aplicação..."

# Parar aplicação se estiver rodando
pkill -f "tech-challenge-restaurant"

# Limpar e compilar
./gradlew clean build -x test

# Executar
./gradlew bootRun --args='--spring.profiles.active=develop' &

echo "✅ Aplicação reiniciada!"
echo "📱 Swagger UI: http://localhost:8081/swagger-ui.html"
echo "🏥 Health Check: http://localhost:8081/actuator/health"
```

## 🚀 Próximos Passos

Após configurar o ambiente local:

1. [Execute os testes](testing.md) para verificar se tudo está funcionando
2. Explore a [documentação da API](../api/overview.md)
3. Consulte a [arquitetura do projeto](../architecture/overview.md)
