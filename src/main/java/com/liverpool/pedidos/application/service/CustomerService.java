package com.liverpool.pedidos.application.service;

import com.liverpool.pedidos.domain.model.Customer;

/**
 * Interfaz de servicio para la gestión de Clientes.
 */
public interface CustomerService {

    /**
     * Registra un nuevo cliente en el sistema.
     *
     * @param customer Datos del cliente a registrar.
     * @return El cliente guardado.
     */
    Customer createCustomer(Customer customer);

    /**
     * Recupera un cliente por su identificador.
     *
     * @param userId Identificador único del cliente.
     * @return El cliente con sus datos .
     */
    Customer getCustomerById(String userId);

    /**
     * Actualiza la información de un cliente existente.
     *
     * @param userId   Identificador del cliente a modificar.
     * @param customer Datos actualizados del cliente.
     * @return El cliente actualizado.
     */
    Customer updateCustomer(String userId, Customer customer);

    /**
     * Elimina a un cliente de la base de datos.
     *
     * @param userId Identificador único del cliente.
     */
    void deleteCustomer(String userId);
}
