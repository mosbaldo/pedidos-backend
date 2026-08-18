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
     * Identificador numérico del producto.
     */
    @EqualsAndHashCode.Include
    @JsonProperty("id")
    private long id;

    /**
     * Identificador del producto.
     */
    @JsonProperty("itemId")
    private String itemId;

    /**
     * Identificador del SKU del producto.
     */
    @JsonProperty("skuId")
    private String skuId;

    /**
     * Nombre del producto.
     */
    @JsonProperty("displayName")
    private String displayName;

    /**
     * Cantidad del producto.
     */
    @JsonProperty("quantity")
    private int quantity;

    /**
     * Estatus de la entrega del producto.
     */
    @JsonProperty("deliveryStatus")
    private String deliveryStatus;

}
