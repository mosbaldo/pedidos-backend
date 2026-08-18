package com.liverpool.pedidos.infrastructure.mapper;

import org.mapstruct.Mapper;

import com.liverpool.pedidos.domain.model.Item;
import com.liverpool.pedidos.infrastructure.rest.client.dto.ItemDto;

/**
 * Mapper para la traducción de DTOs del cliente externo de items
 * a modelo de dominio.
 */
@Mapper(componentModel = "spring")
public interface ItemMapper {

    /**
     * Mapea respuesta del cliente MockItemsApi a un modelo de dominio.
     *
     * @param itemDto DTO de la capa de API.
     * @return Modelo de dominio correspondiente.
     */
    Item toDomain(ItemDto itemDto);
}
