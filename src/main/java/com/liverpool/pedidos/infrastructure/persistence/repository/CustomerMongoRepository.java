package com.liverpool.pedidos.infrastructure.persistence.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.liverpool.pedidos.infrastructure.persistence.document.CustomerDocument;

/**
 * Repositorio de persistencia para la entidad CustomerDocument.
*/
@Repository
public interface CustomerMongoRepository extends MongoRepository<CustomerDocument, String> {}
