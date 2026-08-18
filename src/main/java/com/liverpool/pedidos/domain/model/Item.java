package com.liverpool.pedidos.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Modelo de dominio puro para representar un producto.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Item {

    /**
     * Identificador único del producto.
     */
    @EqualsAndHashCode.Include
    private long id;

    /**
     * Identificador del producto.
     */
    @EqualsAndHashCode.Include
    private String itemId;

    /**
     * Identificador del SKU del producto.
     */
    private String skuId;

    /**
     * Nombre del producto.
     */
    private String displayName;

    /**
     * Cantidad del producto en el pedido.
     */
    private int quantity;

    /**
     * Estatus de la entrega del producto.
     */
    private String deliveryStatus;

}