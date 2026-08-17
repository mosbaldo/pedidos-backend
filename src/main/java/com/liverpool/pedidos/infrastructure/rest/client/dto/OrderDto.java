package com.liverpool.pedidos.infrastructure.rest.client.dto;

import java.util.List;

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
     * Lista de identificadores de los productos asociados al pedido.
     */
    @JsonProperty("itemId")
    private List<String> items;

    /**
     * Canal de venta del pedido (p.e. online, physical).
     */
    @JsonProperty("canal")
    private String canal;

    /**
     * Bandera de marketPlace.
     */
    @JsonProperty("marketPlace")
    private boolean marketPlace;

    /**
     * Bandera de giftRegistry.
     */
    @JsonProperty("giftRegistry")
    private boolean giftRegistry;

}
