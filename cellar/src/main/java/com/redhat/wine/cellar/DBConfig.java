package com.redhat.wine.cellar;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

@Configuration
public class DBConfig extends AbstractMongoConfiguration{
    private static final String MONGODB_USER = "MONGODB_USER";
    private static final String MONGODB_PASSWORD = "MONGODB_PASSWORD";
    private static final String MONGODB_DATABASE = "MONGODB_DATABASE";
    private static final String DATABASE_SERVICE_NAME = "DATABASE_SERVICE_NAME";
    private static final String DATABASE_SERVICE_PORT = "DATABASE_SERVICE_PORT";
    
    @Override
    @Bean
    public Mongo mongo() throws Exception {
        String hostname = System.getenv(DATABASE_SERVICE_NAME) != null ? System.getenv(DATABASE_SERVICE_NAME) : "localhost";
        String port = System.getenv(DATABASE_SERVICE_PORT) != null ? System.getenv(DATABASE_SERVICE_PORT) : "27017";
        String user = System.getenv(MONGODB_USER);
        String pass = System.getenv(MONGODB_PASSWORD);
        String authSource = System.getenv(MONGODB_DATABASE);
        
        String uri = String.format("mongodb://%s/%s", hostname, port);
        if (user != null && pass != null) {
            uri = String.format("mongodb://%s:%s@%s/%s", user, pass, hostname, port);
        }

        if (authSource != null) {
            uri += "?authSource=" + authSource;
        }

        System.out.println("uri: " + uri);

        MongoClient client = null;
        try {
            client = new MongoClient(new MongoClientURI(uri));
        } catch (Throwable e) {
            System.err.println("Error connecting to Mongo: " + uri);
        }

        return client;
    }

    @Override
    protected String getDatabaseName() {
        return System.getenv(MONGODB_DATABASE) != null ? System.getenv(MONGODB_DATABASE) : "mydb";
    }
}

