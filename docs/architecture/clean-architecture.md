# 🏛️ Clean Architecture

Implementação detalhada dos princípios da Clean Architecture no Tech Challenge Restaurant API, seguindo as diretrizes de Robert C. Martin (Uncle Bob).

## 🎯 Princípios Fundamentais

### 1. Regra da Dependência

As dependências sempre apontam para dentro, das camadas externas para as internas:

```mermaid
graph TB
    subgraph "External"
        UI[UI/Web]
        DB[Database]
        External[External APIs]
    end
    
    subgraph "Interface Adapters"
        Controllers[Controllers]
        Presenters[Presenters]
        Gateways[Gateways]
    end
    
    subgraph "Application Business Rules"
        UseCases[Use Cases]
    end
    
    subgraph "Enterprise Business Rules"
        Entities[Entities]
    end
    
    UI --> Controllers
    Controllers --> UseCases
    UseCases --> Entities
    Gateways --> DB
    UseCases --> Gateways
```

### 2. Independência de Frameworks

A lógica de negócio não depende de frameworks específicos:

```java
// ❌ Dependência direta do framework
@Service
public class RestaurantService {
    @Autowired
    private RestaurantRepository repository; // Spring específico
}

// ✅ Independente do framework
public class RestaurantUseCase {
    private final RestaurantRepository repository; // Interface pura
    
    public RestaurantUseCase(RestaurantRepository repository) {
        this.repository = repository;
    }
}
```

### 3. Testabilidade

Cada camada pode ser testada isoladamente:

```java
// Use Case testável sem Spring
@Test
void shouldCreateRestaurant() {
    // Given
    RestaurantRepository mockRepository = mock(RestaurantRepository.class);
    RestaurantUseCase useCase = new RestaurantUseCase(mockRepository);
    
    // When & Then
    // Teste puro, sem dependências externas
}
```

## 🏗️ Implementação por Camadas

### 🌟 Entities (Camada Mais Interna)

**Responsabilidade**: Regras de negócio empresariais mais gerais.

**Características**:
- Não dependem de nada externo
- Contêm regras de negócio puras
- Podem ser reutilizadas em diferentes aplicações

```java
@Document(collection = "restaurants")
public class RestaurantEntity {
    
    @Id
    private UUID id;
    
    @NotBlank
    private String name;
    
    private String address;
    private KitchenTypeEntity kitchenType;
    private List<OperationDaysTimeData> operationDays;
    private List<MenuCategoryEntity> menu;
    
    // Regras de negócio puras
    public boolean isOpenAt(LocalDateTime dateTime) {
        if (operationDays == null || operationDays.isEmpty()) {
            return false;
        }
        
        DayEnum day = DayEnum.fromDayOfWeek(dateTime.getDayOfWeek());
        LocalTime time = dateTime.toLocalTime();
        
        return operationDays.stream()
            .filter(op -> op.getDay().equals(day))
            .anyMatch(op -> isTimeBetween(time, op.getOpenTime(), op.getCloseTime()));
    }
    
    public void addMenuCategory(MenuCategoryEntity category) {
        if (menu == null) {
            menu = new ArrayList<>();
        }
        
        // Regra de negócio: não permitir categorias duplicadas
        boolean categoryExists = menu.stream()
            .anyMatch(existing -> existing.getName().equalsIgnoreCase(category.getName()));
        
        if (categoryExists) {
            throw new ConflictRequestException("Menu category already exists: " + category.getName());
        }
        
        category.setId(UUID.randomUUID());
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        menu.add(category);
    }
    
    public MenuCategoryEntity findMenuCategoryById(UUID categoryId) {
        if (menu == null) {
            return null;
        }
        
        return menu.stream()
            .filter(category -> category.getId().equals(categoryId))
            .findFirst()
            .orElse(null);
    }
    
    private boolean isTimeBetween(LocalTime time, LocalTime start, LocalTime end) {
        if (end.isBefore(start)) {
            // Horário que cruza meia-noite (ex: 22:00 às 02:00)
            return time.isAfter(start) || time.isBefore(end);
        }
        return time.isAfter(start) && time.isBefore(end);
    }
}
```

### 🔧 Use Cases (Camada de Aplicação)

**Responsabilidade**: Regras de negócio específicas da aplicação.

**Características**:
- Orquestram o fluxo de dados
- Implementam casos de uso específicos
- Dependem apenas de interfaces (ports)

```java
@Service
public class RestaurantUseCase {
    
    private final RestaurantRepository restaurantRepository;
    private final KitchenTypeRepository kitchenTypeRepository;
    
    public RestaurantUseCase(RestaurantRepository restaurantRepository,
                           KitchenTypeRepository kitchenTypeRepository) {
        this.restaurantRepository = restaurantRepository;
        this.kitchenTypeRepository = kitchenTypeRepository;
    }
    
    public RestaurantFullResponse createRestaurant(RestaurantRequest request) {
        // 1. Validar entrada
        validateRestaurantRequest(request);
        
        // 2. Buscar kitchen type
        KitchenTypeEntity kitchenType = kitchenTypeRepository
            .findById(request.getKitchenTypeId())
            .orElseThrow(() -> new NotFoundException("Kitchen type not found"));
        
        // 3. Criar entidade
        RestaurantEntity restaurant = RestaurantEntity.builder()
            .id(UUID.randomUUID())
            .name(request.getName())
            .address(request.getAddress())
            .phone(request.getPhone())
            .email(request.getEmail())
            .website(request.getWebsite())
            .kitchenType(kitchenType)
            .operationDays(mapOperationDays(request.getOperationDays()))
            .menu(new ArrayList<>())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        
        // 4. Aplicar regras de negócio
        validateBusinessRules(restaurant);
        
        // 5. Salvar
        RestaurantEntity savedRestaurant = restaurantRepository.save(restaurant);
        
        // 6. Retornar response
        return RestaurantMapper.toFullResponse(savedRestaurant);
    }
    
    public RestaurantFullResponse addMenuCategory(UUID restaurantId, MenuCategoryRequest request) {
        // 1. Buscar restaurante
        RestaurantEntity restaurant = restaurantRepository
            .findById(restaurantId)
            .orElseThrow(() -> new NotFoundException("Restaurant not found"));
        
        // 2. Criar categoria
        MenuCategoryEntity category = MenuCategoryEntity.builder()
            .name(request.getName())
            .description(request.getDescription())
            .items(new ArrayList<>())
            .build();
        
        // 3. Adicionar categoria (regra de negócio na entidade)
        restaurant.addMenuCategory(category);
        
        // 4. Salvar
        RestaurantEntity savedRestaurant = restaurantRepository.save(restaurant);
        
        // 5. Retornar response
        return RestaurantMapper.toFullResponse(savedRestaurant);
    }
    
    private void validateRestaurantRequest(RestaurantRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new BadRequestException("Restaurant name is required");
        }
        
        if (request.getEmail() != null && !isValidEmail(request.getEmail())) {
            throw new BadRequestException("Invalid email format");
        }
        
        if (request.getOperationDays() != null) {
            validateOperationDays(request.getOperationDays());
        }
    }
    
    private void validateBusinessRules(RestaurantEntity restaurant) {
        // Regra: não pode haver dois restaurantes com mesmo nome
        Optional<RestaurantEntity> existing = restaurantRepository.findByName(restaurant.getName());
        if (existing.isPresent()) {
            throw new ConflictRequestException("Restaurant with this name already exists");
        }
    }
}
```

### 🔌 Interface Adapters (Controllers e Gateways)

**Responsabilidade**: Converter dados entre formatos externos e internos.

#### Controllers

```java
@RestController
@RequestMapping("/api/restaurants")
@Validated
public class RestaurantController implements RestaurantControllerInterface {
    
    private final RestaurantUseCase restaurantUseCase;
    
    public RestaurantController(RestaurantUseCase restaurantUseCase) {
        this.restaurantUseCase = restaurantUseCase;
    }
    
    @Override
    @PostMapping
    public ResponseEntity<RestaurantFullResponse> createRestaurant(
            @Valid @RequestBody RestaurantRequest request) {
        
        RestaurantFullResponse response = restaurantUseCase.createRestaurant(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantFullResponse> getRestaurantById(@PathVariable UUID id) {
        RestaurantFullResponse response = restaurantUseCase.getRestaurantById(id);
        return ResponseEntity.ok(response);
    }
}
```

#### Repository Interfaces (Ports)

```java
// Port (interface na camada de aplicação)
public interface RestaurantRepository {
    RestaurantEntity save(RestaurantEntity restaurant);
    Optional<RestaurantEntity> findById(UUID id);
    List<RestaurantEntity> findAll();
    Optional<RestaurantEntity> findByName(String name);
    void deleteById(UUID id);
    boolean existsById(UUID id);
}
```

#### Repository Implementation (Adapter)

```java
// Adapter (implementação na camada de infraestrutura)
@Repository
public class RestaurantRepositoryImpl implements RestaurantRepository {
    
    private final MongoRestaurantRepository mongoRepository;
    
    public RestaurantRepositoryImpl(MongoRestaurantRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }
    
    @Override
    public RestaurantEntity save(RestaurantEntity restaurant) {
        return mongoRepository.save(restaurant);
    }
    
    @Override
    public Optional<RestaurantEntity> findById(UUID id) {
        return mongoRepository.findById(id);
    }
    
    @Override
    public List<RestaurantEntity> findAll() {
        return mongoRepository.findAll();
    }
    
    @Override
    public Optional<RestaurantEntity> findByName(String name) {
        return mongoRepository.findByNameIgnoreCase(name);
    }
    
    @Override
    public void deleteById(UUID id) {
        mongoRepository.deleteById(id);
    }
    
    @Override
    public boolean existsById(UUID id) {
        return mongoRepository.existsById(id);
    }
}

// Interface específica do MongoDB
interface MongoRestaurantRepository extends MongoRepository<RestaurantEntity, UUID> {
    Optional<RestaurantEntity> findByNameIgnoreCase(String name);
}
```

## 🔄 Inversão de Dependências

### Problema Tradicional

```java
// ❌ Dependência direta (acoplamento forte)
public class RestaurantService {
    private MongoRestaurantRepository repository; // Dependência concreta
    
    public void createRestaurant(Restaurant restaurant) {
        repository.save(restaurant); // Acoplado ao MongoDB
    }
}
```

### Solução com Clean Architecture

```java
// ✅ Dependência invertida (baixo acoplamento)
public class RestaurantUseCase {
    private final RestaurantRepository repository; // Interface abstrata
    
    public RestaurantUseCase(RestaurantRepository repository) {
        this.repository = repository; // Injeção de dependência
    }
    
    public void createRestaurant(RestaurantRequest request) {
        RestaurantEntity restaurant = mapToEntity(request);
        repository.save(restaurant); // Não sabe qual implementação
    }
}
```

### Configuração de Injeção

```java
@Configuration
public class UseCaseConfig {
    
    @Bean
    public RestaurantUseCase restaurantUseCase(RestaurantRepository restaurantRepository,
                                             KitchenTypeRepository kitchenTypeRepository) {
        return new RestaurantUseCase(restaurantRepository, kitchenTypeRepository);
    }
    
    @Bean
    public RestaurantRepository restaurantRepository(MongoRestaurantRepository mongoRepository) {
        return new RestaurantRepositoryImpl(mongoRepository);
    }
}
```

## 🧪 Testabilidade Avançada

### Testes de Use Cases

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
    @DisplayName("Should create restaurant successfully")
    void shouldCreateRestaurantSuccessfully() {
        // Given
        RestaurantRequest request = RestaurantRequest.builder()
            .name("Test Restaurant")
            .kitchenTypeId(UUID.randomUUID())
            .build();
        
        KitchenTypeEntity kitchenType = KitchenTypeEntity.builder()
            .id(request.getKitchenTypeId())
            .name("Italian")
            .build();
        
        when(kitchenTypeRepository.findById(request.getKitchenTypeId()))
            .thenReturn(Optional.of(kitchenType));
        
        when(restaurantRepository.findByName(request.getName()))
            .thenReturn(Optional.empty());
        
        when(restaurantRepository.save(any(RestaurantEntity.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        
        // When
        RestaurantFullResponse response = restaurantUseCase.createRestaurant(request);
        
        // Then
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(request.getName());
        assertThat(response.getKitchenType().getName()).isEqualTo("Italian");
        
        verify(restaurantRepository).save(any(RestaurantEntity.class));
        verify(kitchenTypeRepository).findById(request.getKitchenTypeId());
    }
    
    @Test
    @DisplayName("Should throw exception when kitchen type not found")
    void shouldThrowExceptionWhenKitchenTypeNotFound() {
        // Given
        RestaurantRequest request = RestaurantRequest.builder()
            .name("Test Restaurant")
            .kitchenTypeId(UUID.randomUUID())
            .build();
        
        when(kitchenTypeRepository.findById(request.getKitchenTypeId()))
            .thenReturn(Optional.empty());
        
        // When & Then
        assertThatThrownBy(() -> restaurantUseCase.createRestaurant(request))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("Kitchen type not found");
        
        verify(restaurantRepository, never()).save(any());
    }
}
```

### Testes de Entidades

```java
class RestaurantEntityTest {
    
    @Test
    @DisplayName("Should be open during operation hours")
    void shouldBeOpenDuringOperationHours() {
        // Given
        RestaurantEntity restaurant = RestaurantEntity.builder()
            .operationDays(Arrays.asList(
                OperationDaysTimeData.builder()
                    .day(DayEnum.MONDAY)
                    .openTime(LocalTime.of(18, 0))
                    .closeTime(LocalTime.of(23, 0))
                    .build()
            ))
            .build();
        
        LocalDateTime mondayEvening = LocalDateTime.of(2024, 8, 5, 20, 0); // Segunda, 20h
        
        // When
        boolean isOpen = restaurant.isOpenAt(mondayEvening);
        
        // Then
        assertThat(isOpen).isTrue();
    }
    
    @Test
    @DisplayName("Should not allow duplicate menu categories")
    void shouldNotAllowDuplicateMenuCategories() {
        // Given
        RestaurantEntity restaurant = new RestaurantEntity();
        
        MenuCategoryEntity category1 = MenuCategoryEntity.builder()
            .name("Entradas")
            .build();
        
        MenuCategoryEntity category2 = MenuCategoryEntity.builder()
            .name("entradas") // Mesmo nome, case insensitive
            .build();
        
        // When
        restaurant.addMenuCategory(category1);
        
        // Then
        assertThatThrownBy(() -> restaurant.addMenuCategory(category2))
            .isInstanceOf(ConflictRequestException.class)
            .hasMessage("Menu category already exists: entradas");
    }
}
```

## 📊 Métricas de Qualidade

### Cobertura de Testes

```gradle
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
                '**/dto/**'
            ])
        }))
    }
}

jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = 0.80 // 80% de cobertura mínima
            }
        }
    }
}
```

### Análise de Dependências

```bash
# Verificar dependências circulares
./gradlew dependencyInsight --dependency spring-boot-starter-web

# Analisar estrutura de pacotes
find src/main/java -name "*.java" | xargs grep -l "import.*presentation" | grep -v presentation
```

## 🔧 Ferramentas de Apoio

### ArchUnit - Testes Arquiteturais

```java
@AnalyzeClasses(packages = "com.fiap.itmoura.tech_challenge_restaurant")
class ArchitectureTest {
    
    @ArchTest
    static final ArchRule domain_should_not_depend_on_other_layers =
        classes().that().resideInAPackage("..domain..")
            .should().onlyDependOnClassesInPackages("..domain..", "java..", "jakarta.validation..");
    
    @ArchTest
    static final ArchRule use_cases_should_not_depend_on_presentation =
        classes().that().resideInAPackage("..application..")
            .should().notDependOnClassesInPackages("..presentation..");
    
    @ArchTest
    static final ArchRule controllers_should_only_call_use_cases =
        classes().that().resideInAPackage("..presentation.controllers..")
            .should().onlyAccessClassesThat()
            .resideInAnyPackage("..application.usecases..", "..application.models..", "java..", "org.springframework..");
}
```

## 🚀 Benefícios Alcançados

### 1. **Independência de Frameworks**
- Lógica de negócio isolada
- Fácil migração de tecnologias
- Testes sem dependências externas

### 2. **Testabilidade Superior**
- Testes unitários rápidos
- Mocks simples e eficazes
- Cobertura de código alta

### 3. **Manutenibilidade**
- Código organizado por responsabilidade
- Mudanças localizadas
- Refatoração segura

### 4. **Flexibilidade**
- Fácil adição de novos casos de uso
- Troca de implementações sem impacto
- Evolução incremental

## 🔄 Evolução da Arquitetura

### Próximos Passos

1. **Event Sourcing**: Para auditoria completa
2. **CQRS**: Separar comandos de consultas
3. **Microservices**: Dividir por contextos limitados
4. **Hexagonal Architecture**: Expandir ports e adapters

### Refatorações Planejadas

```java
// Futuro: Separar comandos de consultas
public interface RestaurantCommandRepository {
    RestaurantEntity save(RestaurantEntity restaurant);
    void deleteById(UUID id);
}

public interface RestaurantQueryRepository {
    Optional<RestaurantEntity> findById(UUID id);
    List<RestaurantEntity> findAll();
    List<RestaurantEntity> findByKitchenType(String kitchenType);
}
```

## 🚀 Próximos Passos

- [Modelagem de Dados](data-modeling.md) - Estrutura do MongoDB
- [Visão Geral](overview.md) - Arquitetura completa
- [API Reference](../api/overview.md) - Documentação dos endpoints
