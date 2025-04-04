package com.frequencies;

import com.frequencies.logs.entity.Log;
import com.frequencies.logs.service.LogService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;

import java.time.LocalDateTime;

@SpringBootTest
public class LogServiceTests {
    private static final String MONGO_IMAGE = "mongo:8.0";

    private static final String MONGO_DB_NAME = "dbTest";

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    @Autowired
    private LogService logService;

    private static MongoDBContainer mongoDBContainer = new MongoDBContainer(MONGO_IMAGE);

    @DynamicPropertySource
    static void mongoDBProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", () -> mongoDBContainer.getReplicaSetUrl(MONGO_DB_NAME));
    }

    @BeforeAll
    static void init() {
        mongoDBContainer.start();
    }

    @BeforeEach
    public void beforeEach() {
        mongoTemplate.dropCollection(Log.class)
                     .block();
    }

    @Test
    public void insertValidLog() {
        Log log = new Log();
        log.setMessage("test message");
        log.setDate(LocalDateTime.now());
        log.setScope("some scope");

        logService.insertLog(log);
    }
}
