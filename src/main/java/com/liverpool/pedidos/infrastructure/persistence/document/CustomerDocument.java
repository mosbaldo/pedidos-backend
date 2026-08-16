package com.liverpool.pedidos.infrastructure.persistence.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad de persistencia que representa la colección de "customers" en MongoDB.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "customers")
public class CustomerDocument {

    /**
     * ID único del cliente.
     */
    @Id
    @EqualsAndHashCode.Include
    private String userId;

    private String firstName;
    private String lastName;
    private String secondLastName;
    private String email;
}