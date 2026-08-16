package com.liverpool.pedidos.domain.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Modelo de dominio puro para representar un Cliente.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Customer {

    /**
     * Identificador único del cliente.
     */
    @EqualsAndHashCode.Include
    private String userId;

    /**
     * Nombre(s) del cliente.
     */
    private String firstName;

    /**
     * Apellido paterno del cliente.
     */
    private String lastName;

    /**
     * Apellido materno del cliente.
     */
    private String secondLastName;

    /**
     * Correo electrónico del cliente.
     */
    private String email;

    /**
     * Listado de pedidos asociados al cliente.
     */
    @Builder.Default
    private List<Order> orders = new ArrayList<>();

}
