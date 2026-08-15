package com.liverpool.pedidos.infrastructure.rest.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO para la respuesta del endpoint externo de órdenes.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class OrderDto {

    /**
     * Identificador único del pedido.
     */
    @EqualsAndHashCode.Include
    @JsonProperty("id")
    private long id;

    /**
     * Identificador del cliente asociado al pedido.
     */
    @JsonProperty("userId")
    private String userId;

    /**
     * Número del pedido.
     */
    @JsonProperty("orderRef")
    private String orderRef;

    /**
     * Fecha de entrega estimada del pedido.
     */
    @JsonProperty("orderStatus")
    private String orderStatus;

    /**
     * Nombre de la tienda.
     */
    @JsonProperty("storeName")
    private String storeName;

    /**
     * Código de identificación del producto asociado al pedido.
     */
    @JsonProperty("itemId")
    private String itemId;

    /**
     * Cantidad de productos en el pedido.
     */
    @JsonProperty("quantity")
    private int quantity;

    /**
     * Canal de venta del pedido (p.e. online, physical).
     */
    @JsonProperty("salesChannel")
    private String salesChannel;

}
