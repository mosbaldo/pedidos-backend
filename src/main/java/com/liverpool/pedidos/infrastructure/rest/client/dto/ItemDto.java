package com.liverpool.pedidos.infrastructure.rest.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class ItemDto {

    /**
     * Identificador único del item.
     */
    @EqualsAndHashCode.Include
    @JsonProperty("id")
    private long id;

    /**
     * Identificador del item.
     */
    @JsonProperty("itemId")
    private long itemId;

    /**
     * Identificador del SKU del item.
     */
    @JsonProperty("skuId")
    private String skuId;

    /**
     * Nombre del producto.
     */
    @JsonProperty("displayName")
    private String displayName;

    /**
     * Cantidad del pedido.
     */
    @JsonProperty("quantity")
    private int quantity;

    /**
     * Descripción del producto.
     */
    @JsonProperty("deliveryStatus")
    private String deliveryStatus;

}
