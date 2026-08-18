package com.liverpool.pedidos.application.service;

import java.util.List;

import com.liverpool.pedidos.domain.model.Order;

public interface OrderSearchService {

    /**
     * Realiza una búsqueda type-ahead sobre pedidos filtrando por referencia, estatus,
     * nombre de tienda o nombre del producto.
     *
     * @param nomalizedQuery Cadena de caracteres normalizada para el filtro.
     * @return Lista de órdenes que coninciden con la query normalizada
     */
    List<Order> searchOrders(String nomalizedQuery);

}
