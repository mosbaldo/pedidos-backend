package com.liverpool.pedidos.domain.exception;

/**
 * Excepción lanzada cuando no se encuentra un cliente con el userId especificado.
*/
public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(String userId) {
        super("No existe el cliente con ID: " + userId);
    }

}
