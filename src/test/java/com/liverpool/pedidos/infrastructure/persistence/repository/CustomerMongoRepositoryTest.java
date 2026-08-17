package com.liverpool.pedidos.infrastructure.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.liverpool.pedidos.infrastructure.persistence.document.CustomerDocument;

@DataMongoTest
@Testcontainers
class CustomerMongoRepositoryTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0")
            .withStartupTimeout(Duration.ofSeconds(60));

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getConnectionString);
        registry.add("spring.data.mongodb.database", () -> "liverpool_pedidos_test");
    }

    @Autowired
    private CustomerMongoRepository customerMongoRepository;

    @BeforeEach
    void setUp() {
        customerMongoRepository.deleteAll();
    }

    @Test
    @DisplayName("Debería persistir un CustomerDocument y recuperarlo correctamente")
    void saveAndFind_Success() {
        // --- 1. ARRANGE ---
        CustomerDocument document = CustomerDocument.builder()
                .userId("75c97531-abf5-4524-8107-90aa48d08efc")
                .firstName("Miguel")
                .lastName("Gallardo")
                .secondLastName("Toledo")
                .email("osbald91@gmail.com")
                .build();

        // --- 2. ACT ---
        CustomerDocument saved = customerMongoRepository.save(document);
        Optional<CustomerDocument> found = customerMongoRepository.findById(saved.getUserId());

        // --- 3. ASSERT ---
        assertTrue(found.isPresent());
        assertEquals("75c97531-abf5-4524-8107-90aa48d08efc", found.get().getUserId());
        assertEquals("Miguel", found.get().getFirstName());
        assertEquals("osbald91@gmail.com", found.get().getEmail());
    }

    @Test
    @DisplayName("Debería retornar verdadero al validar existencia por existsById")
    void existsById_Success() {
        // --- 1. ARRANGE ---
        CustomerDocument document = CustomerDocument.builder()
                .userId("75c97531-abf5-4524-8107-90aa48d08efc")
                .firstName("Miguel")
                .build();
        customerMongoRepository.save(document);

        // --- 2. ACT ---
        boolean exists = customerMongoRepository.existsById("75c97531-abf5-4524-8107-90aa48d08efc");

        // --- 3. ASSERT ---
        assertTrue(exists);
    }

    @Test
    @DisplayName("Debería eliminar físicamente el documento de la colección")
    void deleteById_Success() {
        // --- 1. ARRANGE ---
        CustomerDocument document = CustomerDocument.builder()
                .userId("75c97531-abf5-4524-8107-90aa48d08efc")
                .build();
        customerMongoRepository.save(document);

        // --- 2. ACT ---
        customerMongoRepository.deleteById("75c97531-abf5-4524-8107-90aa48d08efc");
        Optional<CustomerDocument> found = customerMongoRepository.findById("75c97531-abf5-4524-8107-90aa48d08efc");

        // --- 3. ASSERT ---
        assertFalse(found.isPresent());
    }

}
