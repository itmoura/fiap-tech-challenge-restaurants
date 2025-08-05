# 🔧 Troubleshooting - Problema de Salvamento MongoDB

## ❌ Problema Identificado

Os dados não estão sendo salvos corretamente no MongoDB. Apenas os campos `_id`, `createdAt`, `lastUpdate` e `_class` aparecem no banco, mas os campos `name` e `description` estão ausentes.

### 📊 Exemplo do Problema

**Request enviado:**
```json
{
  "name": "Teste Cozinha",
  "description": "Descrição de teste"
}
```

**Response da API (200 OK):**
```json
{
  "id": "8b27564b-6d10-4fdb-8cba-6d6c55bf54a9",
  "name": "Teste Cozinha",
  "description": "Descrição de teste",
  "createdAt": "2025-08-05T14:55:46.0087274",
  "lastUpdate": "2025-08-05T14:55:46.0087274"
}
```

**Dados no MongoDB (INCORRETO):**
```json
{
  "_id": "8b27564b-6d10-4fdb-8cba-6d6c55bf54a9",
  "createdAt": "2025-08-05T17:55:46.008+00:00",
  "lastUpdate": "2025-08-05T17:55:46.008+00:00",
  "_class": "com.fiap.itmoura.tech_challenge_restaurant.domain.entities.KitchenTypeDocumentEntity"
}
```

**❌ FALTANDO:** `name` e `description`

## 🔍 Possíveis Causas

1. **Problema na serialização MongoDB**
2. **Configuração incorreta dos conversores**
3. **Anotações @Field incorretas ou conflitantes**
4. **Problema na configuração do MongoTemplate**
5. **Conflito entre diferentes estratégias de naming**

## 🛠️ Soluções Implementadas

### 1. **Entidade Corrigida com @Field Explícito**

```java
@Document(collection = "kitchen_types")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KitchenTypeDocumentEntity {

    @Id
    @Field("_id")
    private UUID id;

    @Indexed(unique = true)
    @Field("name")  // ← Explícito
    private String name;

    @Field("description")  // ← Explícito
    private String description;

    @CreatedDate
    @Field("createdAt")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field("lastUpdate")
    private LocalDateTime lastUpdate;
}
```

### 2. **MongoConfig Melhorado**

```java
@Configuration
@EnableMongoAuditing  // ← Importante para @CreatedDate/@LastModifiedDate
public class MongoConfig extends AbstractMongoClientConfiguration {
    // Configuração melhorada com logs
}
```

### 3. **UseCase com Logs Detalhados**

```java
@Transactional
public KitchenTypeResponse createKitchenType(KitchenTypeRequest request) {
    log.info("Entity before save: {}", entity);
    KitchenTypeDocumentEntity savedEntity = kitchenTypeRepository.save(entity);
    log.info("Entity after save: {}", savedEntity);
    
    // Verificação adicional
    var foundEntity = kitchenTypeRepository.findById(savedEntity.getId());
    log.info("Verification: Entity found in database: {}", foundEntity.get());
    
    return KitchenTypeResponse.fromEntity(savedEntity);
}
```

### 4. **Controller de Debug**

Criado endpoint `/api/debug/kitchen-type-direct` para testar salvamento direto.

## 🧪 Como Testar

### 1. **Executar Script de Teste**

```bash
./test-mongo-save.sh
```

### 2. **Testar Endpoints de Debug**

```bash
# Teste direto com repository
curl -X POST "http://localhost:8081/api/debug/kitchen-type-direct" \
  -H "Content-Type: application/json" \
  -d '{"name": "Debug Test", "description": "Debug Description"}'

# Teste com MongoTemplate
curl -X POST "http://localhost:8081/api/debug/kitchen-type-template" \
  -H "Content-Type: application/json" \
  -d '{"name": "Template Test", "description": "Template Description"}'

# Ver dados raw do MongoDB
curl -X GET "http://localhost:8081/api/debug/kitchen-types-raw"
```

### 3. **Verificar Logs**

Com o profile `develop`, os logs do MongoDB estarão em DEBUG:

```bash
./gradlew bootRun --args="--spring.profiles.active=develop"
```

Procure por logs como:
```
DEBUG o.s.d.m.c.MongoTemplate - Saving Document
DEBUG c.f.i.t.a.u.KitchenTypeUseCase - Entity before save: ...
DEBUG c.f.i.t.a.u.KitchenTypeUseCase - Entity after save: ...
```

## 🔧 Verificações Adicionais

### 1. **Verificar Versões**

```bash
# Verificar versão do Spring Data MongoDB
./gradlew dependencies | grep mongodb

# Verificar versão do MongoDB
mongosh --eval "db.version()"
```

### 2. **Verificar Configuração MongoDB**

```bash
# Conectar ao MongoDB
mongosh tech_challenge_restaurants

# Verificar coleção
db.kitchen_types.find().pretty()

# Verificar índices
db.kitchen_types.getIndexes()
```

### 3. **Limpar Cache/Dados**

```bash
# Limpar dados inconsistentes
mongosh tech_challenge_restaurants --eval "db.kitchen_types.deleteMany({})"

# Reiniciar aplicação
./gradlew clean bootRun
```

## 🎯 Próximos Passos

1. **Execute o script de teste**: `./test-mongo-save.sh`
2. **Verifique os logs** em modo DEBUG
3. **Compare os resultados** dos diferentes endpoints
4. **Identifique onde está falhando** a serialização

## 📊 Dados Esperados no MongoDB

Após a correção, o documento deve ficar assim:

```json
{
  "_id": "8b27564b-6d10-4fdb-8cba-6d6c55bf54a9",
  "name": "Teste Cozinha",           // ← Deve aparecer
  "description": "Descrição de teste", // ← Deve aparecer
  "createdAt": "2025-08-05T17:55:46.008+00:00",
  "lastUpdate": "2025-08-05T17:55:46.008+00:00",
  "_class": "com.fiap.itmoura.tech_challenge_restaurant.domain.entities.KitchenTypeDocumentEntity"
}
```

## 🆘 Se o Problema Persistir

1. **Verifique se há conflitos** entre anotações JPA e MongoDB
2. **Teste com uma entidade simples** sem anotações complexas
3. **Verifique se há interceptadores** ou listeners interferindo
4. **Considere usar MongoTemplate diretamente** em vez do repository

## 📞 Debug Avançado

Se precisar de debug mais profundo:

```java
// Adicionar no MongoConfig
@Bean
public MongoTemplate mongoTemplate(MongoClient mongoClient) {
    MongoTemplate template = new MongoTemplate(mongoClient, getDatabaseName());
    template.setWriteConcern(WriteConcern.ACKNOWLEDGED);
    return template;
}
```

Execute os testes e vamos identificar exatamente onde está o problema! 🔍
