package com.liverpool.pedidos.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.liverpool.pedidos.application.service.impl.CustomerServiceImpl;
import com.liverpool.pedidos.domain.exception.CustomerNotFoundException;
import com.liverpool.pedidos.domain.model.Customer;
import com.liverpool.pedidos.domain.model.Order;
import com.liverpool.pedidos.infrastructure.mapper.CustomerMapper;
import com.liverpool.pedidos.infrastructure.mapper.OrderMapper;
import com.liverpool.pedidos.infrastructure.persistence.document.CustomerDocument;
import com.liverpool.pedidos.infrastructure.persistence.repository.CustomerMongoRepository;
import com.liverpool.pedidos.infrastructure.rest.client.MockPedidosApiClient;
import com.liverpool.pedidos.infrastructure.rest.client.dto.OrderDto;

import reactor.core.publisher.Flux;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerMongoRepository customerMongoRepository;

    @Mock
    private MockPedidosApiClient mockPedidosApiClient;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customerDomain;
    private CustomerDocument customerDocument;
    private OrderDto orderDto1;
    private OrderDto orderDto2;
    private Order orderDomain1;
    private Order orderDomain2;
    private String mockUserId;

    @BeforeEach
    void setUp() {
        mockUserId = "75c97531-abf5-4524-8107-90aa48d08efc";
        customerDomain = new Customer();
        customerDomain.setUserId(mockUserId);
        customerDomain.setFirstName("Miguel");
        customerDomain.setLastName("Gallardo");
        customerDomain.setSecondLastName("Toledo");
        customerDomain.setEmail("miguelgallardo@gmail.com");

        customerDocument = new CustomerDocument();
        customerDocument.setUserId(mockUserId);
        customerDocument.setFirstName("Miguel");
        customerDocument.setLastName("Gallardo");
        customerDocument.setSecondLastName("Toledo");
        customerDocument.setEmail("miguelgallardo@gmail.com");

        orderDto1 = new OrderDto();
        orderDto1.setOrderRef("3010091676");
        orderDto1.setUserId("75c97531-abf5-4524-8107-90aa48d08efc");

        orderDto2 = new OrderDto();
        orderDto2.setOrderRef("30100916760987");
        orderDto1.setUserId("75c97531-abf5-4524-8107-90aa48d08efc");
    }

    @Test
    @DisplayName("Debería crear satisfactoriamente un cliente en la base de datos")
    void createCustomer_Success() {
        // --- ARRANGE ---
        when(customerMapper.toDocument(any(Customer.class))).thenReturn(customerDocument);
        when(customerMongoRepository.save(any(CustomerDocument.class))).thenReturn(customerDocument);
        when(customerMapper.toDomain(any(CustomerDocument.class))).thenReturn(customerDomain);

        // --- ACT ---
        Customer result = customerService.createCustomer(customerDomain);

        // --- ASSERT ---
        assertNotNull(result);
        assertEquals(mockUserId, result.getUserId());
        verify(customerMongoRepository, times(1)).save(any(CustomerDocument.class));
    }

    @Test
    @DisplayName("Debería retornar un cliente cuando existe en la base de datos")
    void getCustomerById_Success() {
        // --- ARRANGE ---
        when(customerMongoRepository.findById(mockUserId)).thenReturn(Optional.of(customerDocument));
        when(mockPedidosApiClient.getOrdersByUserId(mockUserId)).thenReturn(Flux.just(orderDto1, orderDto2));
        when(orderMapper.toDomain(orderDto1)).thenReturn(orderDomain1);
        when(orderMapper.toDomain(orderDto2)).thenReturn(orderDomain2);
        when(customerMapper.toDomain(customerDocument)).thenReturn(customerDomain);

        // --- ACT ---
        Customer result = customerService.getCustomerById(mockUserId);

        // --- ASSERT ---
        assertNotNull(result);
        assertEquals(mockUserId, result.getUserId());
        verify(customerMongoRepository, times(1)).findById(mockUserId);
    }

    @Test
    @DisplayName("Debería lanzar CustomerNotFoundException cuando el usuario no existe en la base de datos")
    void getCustomerById_NotFound_ThrowsException() {
        // --- ARRANGE ---
        when(customerMongoRepository.findById(mockUserId)).thenReturn(Optional.empty());

        // --- ACT & ASSERT---
        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(mockUserId));
        verify(customerMongoRepository, times(1)).findById(mockUserId);
        verifyNoInteractions(customerMapper);
    }

    @Test
    @DisplayName("Debería actualizar los datos de un cliente existente con éxito")
    void updateCustomer_Success() {
        // --- ARRANGE ---
        Customer customerInput = new Customer();
        customerInput.setUserId(mockUserId);
        customerInput.setFirstName("Miguel Actualizado");
        customerInput.setLastName("Gallardo");
        customerInput.setSecondLastName("Toledo");
        customerInput.setEmail("actualizado@gmail.com");

        when(customerMongoRepository.findById(mockUserId)).thenReturn(Optional.of(customerDocument));
        when(customerMongoRepository.save(any(CustomerDocument.class))).thenReturn(customerDocument);
        when(customerMapper.toDomain(any(CustomerDocument.class))).thenReturn(customerDomain);

        // --- ACT ---
        Customer result = customerService.updateCustomer(mockUserId, customerInput);

        // --- ASSERT ---
        assertNotNull(result);
        verify(customerMongoRepository, times(1)).findById(mockUserId);
        verify(customerMapper, times(1)).updateDocumentFromDomain(customerInput, customerDocument);
        verify(customerMongoRepository, times(1)).save(any(CustomerDocument.class));
    }

    @Test
    @DisplayName("Debería lanzar CustomerNotFoundException al intentar actualizar un cliente que no existe")
    void updateCustomer_NotFound_ThrowsException() {
        // --- ARRANGE ---
        Customer customerInput = new Customer();

        when(customerMongoRepository.findById(mockUserId)).thenReturn(Optional.empty());

        // --- ACT & ASSERT ---
        assertThrows(CustomerNotFoundException.class, () -> customerService.updateCustomer(mockUserId, customerInput));

        verify(customerMongoRepository, times(1)).findById(mockUserId);
        verify(customerMongoRepository, never()).save(any(CustomerDocument.class));
        verifyNoInteractions(customerMapper);
    }

    @Test
    @DisplayName("Debería eliminar un cliente existente con éxito")
    void deleteCustomer_Success() {
        // --- ARRANGE ---
        when(customerMongoRepository.existsById(mockUserId)).thenReturn(true);
        // Simulamos que el método void delete no hace nada al ser ejecutado
        doNothing().when(customerMongoRepository).deleteById(mockUserId);

        // --- ACT ---
        customerService.deleteCustomer(mockUserId);

        // --- ASSERT ---
        verify(customerMongoRepository, times(1)).existsById(mockUserId);
        verify(customerMongoRepository, times(1)).deleteById(mockUserId);
    }

    @Test
    @DisplayName("Debería lanzar CustomerNotFoundException al intentar eliminar un cliente que no existe")
    void deleteCustomer_NotFound_ThrowsException() {
        // --- ARRANGE ---
        when(customerMongoRepository.existsById(mockUserId)).thenReturn(false);

        // --- ACT & ASSERT ---
        assertThrows(CustomerNotFoundException.class, () -> customerService.deleteCustomer(mockUserId));

        verify(customerMongoRepository, times(1)).existsById(mockUserId);
        verify(customerMongoRepository, never()).deleteById(mockUserId);
    }
}
