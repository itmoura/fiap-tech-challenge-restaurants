# 🧪 Testes

Este guia mostra como executar os testes automatizados do projeto e verificar se tudo está funcionando corretamente.

## 🏃‍♂️ Execução Rápida

```bash
# Executar todos os testes
./gradlew test

# Executar testes com relatório detalhado
./gradlew test --info

# Executar testes e gerar relatório de cobertura
./gradlew test jacocoTestReport
```

## 📊 Tipos de Testes

### 🔬 Testes Unitários

Testes que verificam componentes individuais em isolamento.

```bash
# Executar apenas testes unitários
./gradlew test --tests "*UnitTest"

# Executar testes de uma classe específica
./gradlew test --tests "RestaurantUseCaseTest"

# Executar com padrão específico
./gradlew test --tests "*UseCase*"
```

### 🔗 Testes de Integração

Testes que verificam a integração entre componentes.

```bash
# Executar testes de integração
./gradlew test --tests "*IntegrationTest"

# Testes de integração com MongoDB
./gradlew test --tests "*MongoTest"
```

### 🌐 Testes de API (E2E)

Testes end-to-end que verificam a API completa.

```bash
# Executar testes de API
./gradlew test --tests "*ApiTest"

# Testes com Cucumber (BDD)
./gradlew test --tests "*CucumberTest"
```

## 🛠️ Configuração do Ambiente de Testes

### MongoDB para Testes

O projeto usa **Testcontainers** para testes de integração com MongoDB:

```java
@SpringBootTest
@Testcontainers
class RestaurantIntegrationTest {
    
    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0")
            .withExposedPorts(27017);
    
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }
}
```

### Perfil de Testes

```yaml
# application-test.yml
spring:
  profiles:
    active: test
  data:
    mongodb:
      uri: mongodb://localhost:27017/tech_challenge_test
  main:
    allow-bean-definition-overriding: true

logging:
  level:
    com.fiap.itmoura.tech_challenge_restaurant: DEBUG
    org.springframework.test: DEBUG
```

## 📋 Estrutura de Testes

```
src/test/java/com/fiap/itmoura/tech_challenge_restaurant/
├── application/
│   ├── usecases/
│   │   ├── RestaurantUseCaseTest.java
│   │   ├── KitchenTypeUseCaseTest.java
│   │   └── MenuUseCaseTest.java
├── domain/
│   └── entities/
│       └── RestaurantEntityTest.java
├── presentation/
│   └── controllers/
│       ├── RestaurantControllerTest.java
│       └── KitchenTypeControllerTest.java
├── integration/
│   ├── RestaurantIntegrationTest.java
│   └── MongoRepositoryTest.java
└── cucumber/
    ├── CucumberTestRunner.java
    └── steps/
        └── RestaurantSteps.java
```

## 🧪 Exemplos de Testes

### Teste Unitário - Use Case

```java
@ExtendWith(MockitoExtension.class)
class RestaurantUseCaseTest {
    
    @Mock
    private RestaurantRepository restaurantRepository;
    
    @Mock
    private KitchenTypeRepository kitchenTypeRepository;
    
    @InjectMocks
    private RestaurantUseCase restaurantUseCase;
    
    @Test
    @DisplayName("Deve criar restaurante com sucesso")
    void shouldCreateRestaurantSuccessfully() {
        // Given
        RestaurantRequest request = RestaurantRequest.builder()
                .name("Restaurante Teste")
                .kitchenTypeId(UUID.randomUUID())
                .build();
        
        KitchenTypeEntity kitchenType = new KitchenTypeEntity();
        kitchenType.setId(request.getKitchenTypeId());
        kitchenType.setName("Italiana");
        
        when(kitchenTypeRepository.findById(request.getKitchenTypeId()))
                .thenReturn(Optional.of(kitchenType));
        
        when(restaurantRepository.save(any(RestaurantEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        
        // When
        RestaurantFullResponse response = restaurantUseCase.createRestaurant(request);
        
        // Then
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("Restaurante Teste");
        verify(restaurantRepository).save(any(RestaurantEntity.class));
    }
}
```

### Teste de Integração - Controller

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class RestaurantControllerIntegrationTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private RestaurantRepository restaurantRepository;
    
    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");
    
    @Test
    @DisplayName("Deve criar restaurante via API")
    void shouldCreateRestaurantViaApi() {
        // Given
        RestaurantRequest request = RestaurantRequest.builder()
                .name("Restaurante API Test")
                .address("Rua Teste, 123")
                .build();
        
        // When
        ResponseEntity<RestaurantFullResponse> response = restTemplate.postForEntity(
                "/api/restaurants", 
                request, 
                RestaurantFullResponse.class
        );
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Restaurante API Test");
    }
}
```

### Teste BDD com Cucumber

```gherkin
# restaurant.feature
Feature: Gerenciamento de Restaurantes
  Como um usuário da API
  Eu quero gerenciar restaurantes
  Para manter o catálogo atualizado

  Scenario: Criar um novo restaurante
    Given que existe um tipo de cozinha "Italiana"
    When eu criar um restaurante com nome "Bella Italia"
    Then o restaurante deve ser criado com sucesso
    And deve retornar status 201

  Scenario: Listar restaurantes
    Given que existem restaurantes cadastrados
    When eu listar todos os restaurantes
    Then deve retornar a lista de restaurantes
    And deve retornar status 200
```

```java
@Component
public class RestaurantSteps {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    private ResponseEntity<?> lastResponse;
    private UUID kitchenTypeId;
    
    @Given("que existe um tipo de cozinha {string}")
    public void queExisteUmTipoDeCozinha(String kitchenTypeName) {
        KitchenTypeRequest request = new KitchenTypeRequest();
        request.setName(kitchenTypeName);
        
        ResponseEntity<KitchenTypeResponse> response = restTemplate.postForEntity(
                "/api/kitchen-types", 
                request, 
                KitchenTypeResponse.class
        );
        
        kitchenTypeId = response.getBody().getId();
    }
    
    @When("eu criar um restaurante com nome {string}")
    public void euCriarUmRestauranteComNome(String restaurantName) {
        RestaurantRequest request = RestaurantRequest.builder()
                .name(restaurantName)
                .kitchenTypeId(kitchenTypeId)
                .build();
        
        lastResponse = restTemplate.postForEntity(
                "/api/restaurants", 
                request, 
                RestaurantFullResponse.class
        );
    }
    
    @Then("o restaurante deve ser criado com sucesso")
    public void oRestauranteDeveSerCriadoComSucesso() {
        assertThat(lastResponse.getBody()).isNotNull();
    }
    
    @Then("deve retornar status {int}")
    public void deveRetornarStatus(int expectedStatus) {
        assertThat(lastResponse.getStatusCode().value()).isEqualTo(expectedStatus);
    }
}
```

## 📊 Relatórios de Testes

### Relatório HTML

```bash
# Gerar relatório HTML
./gradlew test

# Abrir relatório
open build/reports/tests/test/index.html
```

### Cobertura de Código

```bash
# Gerar relatório de cobertura
./gradlew jacocoTestReport

# Abrir relatório de cobertura
open build/reports/jacoco/test/html/index.html
```

### Configuração do JaCoCo

```gradle
// build.gradle
apply plugin: 'jacoco'

jacoco {
    toolVersion = "0.8.8"
}

jacocoTestReport {
    reports {
        xml.required = true
        html.required = true
    }
    
    afterEvaluate {
        classDirectories.setFrom(files(classDirectories.files.collect {
            fileTree(dir: it, exclude: [
                '**/TechChallengeRestaurantApplication.class',
                '**/config/**',
                '**/dto/**',
                '**/model/**'
            ])
        }))
    }
}

test.finalizedBy jacocoTestReport
```

## 🚀 Execução em CI/CD

### GitHub Actions

```yaml
# .github/workflows/tests.yml
name: Tests

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  test:
    runs-on: ubuntu-latest
    
    services:
      mongodb:
        image: mongo:7.0
        ports:
          - 27017:27017
        env:
          MONGO_INITDB_ROOT_USERNAME: admin
          MONGO_INITDB_ROOT_PASSWORD: admin123
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 21
      uses: actions/setup-java@v3
      with:
        java-version: '21'
        distribution: 'temurin'
    
    - name: Cache Gradle packages
      uses: actions/cache@v3
      with:
        path: |
          ~/.gradle/caches
          ~/.gradle/wrapper
        key: ${{ runner.os }}-gradle-${{ hashFiles('**/*.gradle*', '**/gradle-wrapper.properties') }}
        restore-keys: |
          ${{ runner.os }}-gradle-
    
    - name: Run tests
      run: ./gradlew test jacocoTestReport
      env:
        MONGO_URI: mongodb://admin:admin123@localhost:27017/tech_challenge_test?authSource=admin
    
    - name: Upload coverage reports
      uses: codecov/codecov-action@v3
      with:
        file: build/reports/jacoco/test/jacocoTestReport.xml
```

## 🔧 Testes Manuais

### Postman Collection

O projeto inclui uma collection do Postman para testes manuais:

```bash
# Importar collection
postman/Tech_Challenge_Restaurants.postman_collection.json

# Importar environment
postman/Local_Environment.postman_environment.json
```

### Scripts de Teste

```bash
#!/bin/bash
# test-api.sh

BASE_URL="http://localhost:8081/api"

echo "🧪 Testando API..."

# Teste 1: Health Check
echo "1. Health Check"
curl -s "$BASE_URL/../actuator/health" | jq .

# Teste 2: Criar Kitchen Type
echo "2. Criando Kitchen Type"
KITCHEN_TYPE_ID=$(curl -s -X POST "$BASE_URL/kitchen-types" \
  -H "Content-Type: application/json" \
  -d '{"name":"Italiana","description":"Culinária italiana"}' | jq -r .id)

echo "Kitchen Type ID: $KITCHEN_TYPE_ID"

# Teste 3: Criar Restaurante
echo "3. Criando Restaurante"
RESTAURANT_ID=$(curl -s -X POST "$BASE_URL/restaurants" \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"Bella Italia\",\"kitchenTypeId\":\"$KITCHEN_TYPE_ID\"}" | jq -r .id)

echo "Restaurant ID: $RESTAURANT_ID"

# Teste 4: Listar Restaurantes
echo "4. Listando Restaurantes"
curl -s "$BASE_URL/restaurants" | jq .

echo "✅ Testes concluídos!"
```

## 🐛 Troubleshooting

### Problemas Comuns

#### 1. Testes falhando por timeout

```bash
# Aumentar timeout nos testes
./gradlew test -Dtest.timeout=60000
```

#### 2. MongoDB não disponível para testes

```bash
# Verificar se Testcontainers está funcionando
docker ps | grep testcontainers

# Verificar logs
./gradlew test --debug
```

#### 3. Testes de integração lentos

```bash
# Executar apenas testes unitários
./gradlew test --tests "*UnitTest"

# Pular testes de integração
./gradlew build -x integrationTest
```

### Debug de Testes

```bash
# Executar teste específico com debug
./gradlew test --tests "RestaurantUseCaseTest" --debug-jvm

# Ver logs detalhados
./gradlew test --info --stacktrace
```

## 📈 Métricas de Qualidade

### SonarQube (Opcional)

```bash
# Executar análise SonarQube
./gradlew sonarqube \
  -Dsonar.projectKey=tech-challenge-restaurants \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=your-token
```

### Configuração de Qualidade

```gradle
// build.gradle
plugins {
    id 'org.sonarqube' version '4.0.0.2929'
    id 'com.github.spotbugs' version '5.0.13'
    id 'checkstyle'
    id 'pmd'
}

checkstyle {
    toolVersion = '10.3.4'
    configFile = file('config/checkstyle/checkstyle.xml')
}

pmd {
    toolVersion = '6.55.0'
    ruleSetFiles = files('config/pmd/pmd-rules.xml')
}

spotbugs {
    toolVersion = '4.7.3'
}
```

## 🚀 Próximos Passos

Após executar os testes:

1. Explore a [documentação da API](../api/overview.md)
2. Entenda a [arquitetura do projeto](../architecture/overview.md)
3. Configure [monitoramento](../architecture/overview.md#monitoramento)
