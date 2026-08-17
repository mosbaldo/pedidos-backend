package com.liverpool.pedidos.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.liverpool.pedidos.domain.model.Order;
import com.liverpool.pedidos.infrastructure.rest.client.dto.OrderDto;

/**
 * Mapper para la traducción de DTOs del cliente externo de pedidos
 * a modelos de dominio.
 */
@Mapper(componentModel = "spring")
public interface OrderMapper {

    /**
     * Mapea respuesta del cliente MockPedidosApi a un modelo de dominio.
     *
     * @param orderDto DTO de la capa de API.
     * @return Modelo de dominio correspondiente.
     */
    @Mapping(target = "items", ignore = true)
    Order toDomain(OrderDto orderDto);
}
