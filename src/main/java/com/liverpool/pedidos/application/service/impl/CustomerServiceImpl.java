package com.liverpool.pedidos.application.service.impl;

import com.liverpool.pedidos.application.service.CustomerService;
import com.liverpool.pedidos.domain.exception.CustomerNotFoundException;
import com.liverpool.pedidos.domain.model.Customer;
import com.liverpool.pedidos.infrastructure.mapper.CustomerMapper;
import com.liverpool.pedidos.infrastructure.mapper.OrderMapper;
import com.liverpool.pedidos.infrastructure.persistence.document.CustomerDocument;
import com.liverpool.pedidos.infrastructure.persistence.repository.CustomerMongoRepository;
import com.liverpool.pedidos.infrastructure.rest.client.MockPedidosApiClient;
import com.liverpool.pedidos.infrastructure.rest.client.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

/**
 * Implementación concreta del servicio de clientes.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerMongoRepository customerMongoRepository;
    private final MockPedidosApiClient mockPedidosApiClient;
    private final CustomerMapper customerMapper;
    private final OrderMapper orderMapper;

    @Override
    public Customer createCustomer(Customer customer) {
        log.info("Procesando creación de nuevo cliente con userId: {}", customer.getUserId());

        CustomerDocument savedDocument = customerMongoRepository.save(customerMapper.toDocument(customer));
        log.info("Cliente guardado exitosamente en la base de datos con userId: {}", savedDocument.getUserId());

        return customerMapper.toDomain(savedDocument);
    }

    @Override
    public Customer getCustomerById(String userId) {
        log.info("Buscando cliente con userId: {}", userId);

        CustomerDocument customerDocument = customerMongoRepository.findById(userId)
                .orElseThrow(() -> new CustomerNotFoundException(userId));

        List<OrderDto> updatedOrders = fetchExternalOrderRefs(userId);
        Customer customerDomain = customerMapper.toDomain(customerDocument);
        customerDomain.setOrders(updatedOrders.stream()
                .map(orderMapper::toDomain)
                .toList());
        return customerDomain;
    }

    @Override
    public Customer updateCustomer(String userId, Customer customerUpdate) {
        log.info("Procesando actualización de cliente con userId: {}", userId);

        CustomerDocument existingCustomer = customerMongoRepository.findById(userId)
                .orElseThrow(() -> new CustomerNotFoundException(userId));

        customerMapper.updateDocumentFromDomain(customerUpdate, existingCustomer);

        CustomerDocument savedDocument = customerMongoRepository.save(existingCustomer);
        log.info("Cliente actualizado correctamente en la base de datos");

        return customerMapper.toDomain(savedDocument);
    }

    @Override
    public void deleteCustomer(String userId) {
        log.info("Procesando eliminación física de cliente con userId: {}", userId);

        if (!customerMongoRepository.existsById(userId)) {
            throw new CustomerNotFoundException(userId);
        }

        customerMongoRepository.deleteById(userId);
        log.info("Cliente eliminado exitosamente de la base de datos");
    }

    /**
     * Utilidad privada que realiza el puente síncrono para consumir las órdenes
     * reactivas.
     */
    private List<OrderDto> fetchExternalOrderRefs(String userId) {
        try {
            log.info("Consumiendo MockAPI para recuperar pedidos del usuario: {}", userId);

            return mockPedidosApiClient.getOrdersByUserId(userId)
                    .collectList()
                    .block(Duration.ofSeconds(4));
        } catch (Exception ex) {
            log.error("Falló la actualización de pedidos del cliente {}. Detalle: {}", userId,
                    ex.getMessage());
            return Collections.emptyList();
        }
    }
}
