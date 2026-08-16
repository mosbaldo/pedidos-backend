package com.liverpool.pedidos.domain.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Modelo de dominio puro para representar un Pedido.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Order {

    /**
     * Número del pedido.
     */
    @EqualsAndHashCode.Include
    private String orderRef;

    /**
     * Fecha de entrega estimada del pedido.
     */
    private String orderStatus;

    /**
     * Nombre de la tienda.
     */
    private String storeName;

    /**
     * Lista de productos asociados al pedido.
     */
    private List<Item> items;

    /**
     * Cantidad de productos en el pedido.
     */
    private int quantity;

    /**
     * Canal de venta del pedido (p.e. online, physical).
     */
    private String salesChannel;
}
