package com.liverpool.pedidos.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.liverpool.infrastructure.rest.dto.CustomerOrderDto;
import com.liverpool.infrastructure.rest.dto.CustomerRequestDto;
import com.liverpool.infrastructure.rest.dto.CustomerResponseDto;
import com.liverpool.pedidos.domain.model.Customer;
import com.liverpool.pedidos.domain.model.Order;
import com.liverpool.pedidos.infrastructure.persistence.document.CustomerDocument;

/**
 * Mapper para la traducción entre la capa de API, la capa de Dominio
 * y la capa de Persistencia para la entidad Customer.
 */
@Mapper(componentModel = "spring")
public interface CustomerMapper {

    /**
     * Mapea un DTO de la capa de API a un modelo de dominio.
     *
     * @param customerRequestDto DTO de la capa de API.
     * @return Modelo de dominio correspondiente.
     */
    @Mapping(target = "orders", ignore = true)
    Customer toDomain(CustomerRequestDto customerRequestDto);

    /**
     * Mapea el modelo de dominio a un DTO de la capa de API.
     *
     * @param customer Modelo de dominio.
     * @return DTO de respuesta la capa de API correspondiente.
     */
    CustomerResponseDto toApiDto(Customer customer);

    /**
     * Mapea un modelo de dominio a un documento de persistencia.
     *
     * @param customer Modelo de dominio.
     * @return Documento de persistencia correspondiente.
     */
    CustomerDocument toDocument(Customer customer);

    /**
     * Mapea un documento de persistencia a un modelo de dominio.
     *
     * @param customerDocument Documento de persistencia.
     * @return Modelo de dominio correspondiente.
     */
    @Mapping(target = "orders", ignore = true)
    Customer toDomain(CustomerDocument customerDocument);

    /**
     * Actualiza un documento de persistencia existente con los datos de un modelo de dominio.
     *
     * @param customer Modelo de dominio.
     * @param customerDocument Documento de persistencia.
     */
    @Mapping(target = "userId", ignore = true)
    void updateDocumentFromDomain(Customer customer, @MappingTarget CustomerDocument customerDocument);

}
