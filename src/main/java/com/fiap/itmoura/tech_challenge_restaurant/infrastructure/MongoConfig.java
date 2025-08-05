package com.fiap.itmoura.tech_challenge_restaurant.infrastructure;

import java.util.UUID;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.Arrays;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(MongoConfig.class);

    @Override
    protected String getDatabaseName() {
        return "tech_challenge_restaurants";
    }

    @Bean
    @Override
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(Arrays.asList(
            new SafeStringToUUIDConverter(),
            new UUIDToStringConverter()
        ));
    }

    /**
     * Conversor seguro que só converte strings que são realmente UUIDs
     */
    static class SafeStringToUUIDConverter implements Converter<String, UUID> {
        
        private static final Logger logger = LoggerFactory.getLogger(SafeStringToUUIDConverter.class);
        
        // Pattern rigoroso para UUID
        private static final Pattern UUID_PATTERN = Pattern.compile(
            "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
        );

        @Override
        public UUID convert(String source) {
            // Verificações básicas
            if (source == null || source.trim().isEmpty()) {
                return null;
            }
            
            String trimmedSource = source.trim();
            
            // Se não tem o formato de UUID, não tenta converter
            if (!isValidUUIDFormat(trimmedSource)) {
                logger.debug("Skipping conversion for non-UUID string: '{}'", trimmedSource);
                return null;
            }
            
            try {
                UUID result = UUID.fromString(trimmedSource);
                logger.debug("Successfully converted '{}' to UUID", trimmedSource);
                return result;
            } catch (IllegalArgumentException e) {
                logger.warn("Failed to convert '{}' to UUID: {}", trimmedSource, e.getMessage());
                return null;
            }
        }
        
        private boolean isValidUUIDFormat(String str) {
            // Verificação rápida de tamanho
            if (str.length() != 36) {
                return false;
            }
            
            // Verificação de formato com regex
            return UUID_PATTERN.matcher(str).matches();
        }
    }

    /**
     * Conversor de UUID para String (sempre funciona)
     */
    static class UUIDToStringConverter implements Converter<UUID, String> {
        @Override
        public String convert(UUID source) {
            return source != null ? source.toString() : null;
        }
    }
}
