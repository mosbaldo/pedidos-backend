package com.liverpool.pedidos.infrastructure.rest.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liverpool.infrastructure.rest.dto.CustomerRequestDto;
import com.liverpool.infrastructure.rest.dto.CustomerResponseDto;
import com.liverpool.pedidos.application.service.CustomerService;
import com.liverpool.pedidos.domain.exception.CustomerNotFoundException;
import com.liverpool.pedidos.domain.model.Customer;
import com.liverpool.pedidos.infrastructure.rest.advice.GlobalExceptionHandler;
import com.liverpool.pedidos.infrastructure.rest.client.dto.OrderDto;
import com.liverpool.pedidos.infrastructure.mapper.CustomerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomersApiImpl.class)
@Import(GlobalExceptionHandler.class)
class CustomerApiImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CustomerService customerService;

    @MockitoBean
    private CustomerMapper customerMapper;

    private String mockUserId;
    private Customer customerDomain;
    private CustomerRequestDto customerRequestDto;
    private CustomerResponseDto customerResponseDto;

    @BeforeEach
    void setUp() {
        mockUserId = "75c97531-abf5-4524-8107-90aa48d08efc";

        customerRequestDto = new CustomerRequestDto();
        customerRequestDto.setUserId(mockUserId);
        customerRequestDto.setFirstName("Miguel Osbaldo");
        customerRequestDto.setLastName("Gallardo");
        customerRequestDto.setSecondLastName("Toledo");
        customerRequestDto.setEmail("miguelgallardo@gmail.com");

        customerDomain = new Customer();
        customerDomain.setUserId(mockUserId);

        customerResponseDto = new CustomerResponseDto();
        customerResponseDto.setUserId(mockUserId);
        customerResponseDto.setFirstName("Miguel Osbaldo");
        customerResponseDto.setEmail("miguelgallardo@gmail.com");
        customerResponseDto.setOrders(Collections.emptyList());
    }

    @Test
    @DisplayName("POST /customers - Debería retornar 201 Created y el JSON del cliente persistido")
    void createCustomer_Success() throws Exception {
        // --- ARRANGE ---
        when(customerMapper.toDomain(any(CustomerRequestDto.class))).thenReturn(customerDomain);
        when(customerService.createCustomer(any(Customer.class))).thenReturn(customerDomain);
        when(customerMapper.toApiDto(any(Customer.class))).thenReturn(customerResponseDto);

        // --- ACT & ASSERT ---
        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(mockUserId))
                .andExpect(jsonPath("$.firstName").value("Miguel Osbaldo"))
                .andExpect(jsonPath("$.email").value("miguelgallardo@gmail.com"));

        verify(customerService, times(1)).createCustomer(any(Customer.class));
    }

    @Test
    @DisplayName("GET /customers/{userId} - Debería retornar 200 OK cuando el cliente existe")
    void getCustomerById_Success() throws Exception {
        when(customerService.getCustomerById(mockUserId)).thenReturn(customerDomain);
        when(customerMapper.toApiDto(customerDomain)).thenReturn(customerResponseDto);

        // --- ACT & ASSERT ---
        mockMvc.perform(get("/customers/{userId}", mockUserId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(mockUserId))
                .andExpect(jsonPath("$.firstName").value("Miguel Osbaldo"))
                .andExpect(jsonPath("$.email").value("miguelgallardo@gmail.com"));
    }

    @Test
    @DisplayName("GET /customers/{userId} - Debería retornar 404 Not Found a través del GlobalExceptionHandler")
    void getCustomerById_NotFound() throws Exception {
        // --- ARRANGE ---
        when(customerService.getCustomerById(mockUserId)).thenThrow(new CustomerNotFoundException(mockUserId));

        // --- ACT & ASSERT ---
        mockMvc.perform(get("/customers/{userId}", mockUserId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /customers/{id} - Debería actualizar un cliente y retornar 200 OK")
    void updateCustomer_Success() throws Exception {
        // --- ARRANGE ---
        when(customerMapper.toDomain(any(CustomerRequestDto.class))).thenReturn(customerDomain);
        when(customerService.updateCustomer(eq(mockUserId), any(Customer.class))).thenReturn(customerDomain);
        when(customerMapper.toApiDto(any(Customer.class))).thenReturn(customerResponseDto);

        // --- ACT & ASSERT ---
        mockMvc.perform(put("/customers/{userId}", mockUserId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(mockUserId))
                .andExpect(jsonPath("$.firstName").value("Miguel Osbaldo"))
                .andExpect(jsonPath("$.email").value("miguelgallardo@gmail.com"));

        verify(customerService, times(1)).updateCustomer(eq(mockUserId), any(Customer.class));
    }

    @Test
    @DisplayName("PUT /customers/{id} - Debería retornar 404 si el cliente no existe")
    void updateCustomer_NotFound() throws Exception {
        // --- ARRANGE ---
        when(customerMapper.toDomain(any(CustomerRequestDto.class))).thenReturn(customerDomain);

        // Forzamos al servicio a lanzar la excepción de negocio
        when(customerService.updateCustomer(eq(mockUserId), any(Customer.class)))
                .thenThrow(new CustomerNotFoundException(mockUserId));

        // --- ACT & ASSERT ---
        mockMvc.perform(put("/customers/{userId}", mockUserId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerRequestDto)))
                .andExpect(status().isNotFound());

        verify(customerService, times(1)).updateCustomer(eq(mockUserId), any(Customer.class));
    }

    @Test
    @DisplayName("DELETE /customers/{id} - Debería retornar 204 No Content")
    void deleteCustomer_Success() throws Exception {
        // --- ARRANGE ---
        doNothing().when(customerService).deleteCustomer(mockUserId);

        // --- ACT & ASSERT ---
        mockMvc.perform(delete("/customers/{userId}", mockUserId))
                .andExpect(status().isNoContent());

        verify(customerService, times(1)).deleteCustomer(mockUserId);
    }

    @Test
    @DisplayName("DELETE /customers/{id} - Debería retornar 404 si el cliente no existe")
    void deleteCustomer_NotFound() throws Exception {
        // --- ARRANGE ---
        // Forzamos al método void a lanzar la excepción de negocio al ser invocado
        doThrow(new CustomerNotFoundException(mockUserId))
                .when(customerService).deleteCustomer(mockUserId);

        // --- ACT & ASSERT ---
        mockMvc.perform(delete("/customers/{userId}", mockUserId))
                .andExpect(status().isNotFound());

        verify(customerService, times(1)).deleteCustomer(mockUserId);
    }

}
