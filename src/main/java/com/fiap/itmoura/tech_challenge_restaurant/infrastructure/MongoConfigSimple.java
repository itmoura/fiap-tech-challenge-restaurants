package com.fiap.itmoura.tech_challenge_restaurant.infrastructure;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

/**
 * Configuração MongoDB simplificada sem conversores customizados
 * Use esta configuração se houver problemas com conversão de UUID
 */
@Configuration
public class MongoConfigSimple extends AbstractMongoClientConfiguration {

    @Override
    protected String getDatabaseName() {
        return "tech_challenge_restaurants";
    }
}
