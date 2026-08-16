package com.liverpool.pedidos.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO para la respuesta del endpoint externo de items.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Item {

    /**
     * Identificador único del item.
     */
    @EqualsAndHashCode.Include
    private long id;

    /**
     * Identificador del item.
     */
    private long itemId;

    /**
     * Identificador del SKU del item.
     */
    private String skuId;

    /**
     * Nombre del producto.
     */
    private String displayName;

    /**
     * Cantidad del pedido.
     */
    private int quantity;

    /**
     * Descripción del producto.
     */
    private String deliveryStatus;

}