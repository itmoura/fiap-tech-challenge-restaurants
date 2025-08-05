package com.fiap.itmoura.tech_challenge_restaurant.infrastructure;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Utilitário para limpeza e correção de dados MongoDB
 * Execute com: ./gradlew bootRun --args="--spring.profiles.active=cleanup"
 */
@Component
@Profile("cleanup")
public class MongoCleanupUtility implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(MongoCleanupUtility.class);
    
    // Pattern para validar UUID
    private static final Pattern UUID_PATTERN = Pattern.compile(
        "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
    );

    @Override
    public void run(String... args) throws Exception {
        logger.info("🧹 Iniciando limpeza do MongoDB...");
        
        String mongoUri = System.getProperty("mongo.uri", "mongodb://localhost:27017");
        String databaseName = "tech_challenge_restaurants";
        
        try (MongoClient mongoClient = MongoClients.create(mongoUri)) {
            MongoDatabase database = mongoClient.getDatabase(databaseName);
            
            logger.info("📊 Conectado ao MongoDB: {}", mongoUri);
            
            // Limpar e recriar kitchen types
            cleanupKitchenTypes(database);
            
            // Limpar restaurants (eles dependem de kitchen types válidos)
            cleanupRestaurants(database);
            
            logger.info("✅ Limpeza concluída com sucesso!");
            logger.info("💡 Reinicie a aplicação principal para aplicar as mudanças.");
            
        } catch (Exception e) {
            logger.error("❌ Erro durante a limpeza: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    private void cleanupKitchenTypes(MongoDatabase database) {
        logger.info("🍳 Limpando kitchen types...");
        
        MongoCollection<Document> collection = database.getCollection("kitchen_types");
        
        // Remover todos os documentos existentes
        long deletedCount = collection.deleteMany(new Document()).getDeletedCount();
        logger.info("🗑️ Removidos {} kitchen types inválidos", deletedCount);
        
        // Criar kitchen types válidos
        List<Document> validKitchenTypes = Arrays.asList(
            createKitchenTypeDocument("Brasileira", "Culinária tradicional brasileira"),
            createKitchenTypeDocument("Italiana", "Culinária tradicional italiana com massas e pizzas"),
            createKitchenTypeDocument("Japonesa", "Culinária japonesa autêntica com sushi e yakisoba"),
            createKitchenTypeDocument("Mexicana", "Culinária mexicana com tacos e burritos"),
            createKitchenTypeDocument("Francesa", "Culinária francesa refinada"),
            createKitchenTypeDocument("Chinesa", "Culinária chinesa tradicional"),
            createKitchenTypeDocument("Indiana", "Culinária indiana com especiarias"),
            createKitchenTypeDocument("Árabe", "Culinária árabe com pratos típicos")
        );
        
        collection.insertMany(validKitchenTypes);
        logger.info("✅ Criados {} kitchen types válidos", validKitchenTypes.size());
        
        // Listar os kitchen types criados
        logger.info("📋 Kitchen types disponíveis:");
        collection.find().forEach(doc -> {
            logger.info("  • {} (ID: {})", doc.getString("name"), doc.getString("_id"));
        });
    }
    
    private void cleanupRestaurants(MongoDatabase database) {
        logger.info("🏪 Limpando restaurants...");
        
        MongoCollection<Document> collection = database.getCollection("restaurants");
        
        // Remover todos os restaurantes existentes (eles podem ter referências inválidas)
        long deletedCount = collection.deleteMany(new Document()).getDeletedCount();
        logger.info("🗑️ Removidos {} restaurants com dados inconsistentes", deletedCount);
        
        logger.info("✅ Restaurants limpos. Você pode criar novos via API.");
    }
    
    private Document createKitchenTypeDocument(String name, String description) {
        String id = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        
        return new Document()
            .append("_id", id)
            .append("name", name)
            .append("description", description)
            .append("createdAt", now)
            .append("lastUpdate", now);
    }
    
    private boolean isValidUUID(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }
        return UUID_PATTERN.matcher(str.trim()).matches();
    }
}
