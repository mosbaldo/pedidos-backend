package com.liverpool.pedidos.infrastructure.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.liverpool.infrastructure.rest.dto.OrderSearchResponseDto;
import com.liverpool.pedidos.domain.model.Item;
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
    Order toDomain(OrderDto orderDto);

    /**
     * Mapea el modelo de dominio a un DTO de la capa de API.
     *
     * @param order Modelo de dominio.
     * @return DTO de respuesta la capa de API correspondiente.
     */
    List<OrderSearchResponseDto> toListApiDto(List<Order> orders);

    /**
     * Método auxiliar para convertir cada String del DTO en un objeto Item de dominio con su ID.
     */
    default Item mapStringToItem(String itemId) {
        if (itemId == null) {
            return null;
        }
        return Item.builder()
                .itemId(itemId)
                .build();
    }
}
