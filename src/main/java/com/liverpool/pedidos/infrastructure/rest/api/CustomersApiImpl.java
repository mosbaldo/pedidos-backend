package com.liverpool.pedidos.infrastructure.rest.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.liverpool.infrastructure.rest.dto.CustomerRequestDto;
import com.liverpool.infrastructure.rest.dto.CustomerResponseDto;
import com.liverpool.pedidos.application.service.CustomerService;
import com.liverpool.pedidos.domain.model.Customer;
import com.liverpool.pedidos.infrastructure.mapper.CustomerMapper;
import com.liverpool.web.api.CustomersApi;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CustomersApiImpl implements CustomersApi {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    @Override
    public ResponseEntity<CustomerResponseDto> createCustomer(CustomerRequestDto customerDto) {
        Customer customerDomain = customerMapper.toDomain(customerDto);
        Customer createdCustomer = customerService.createCustomer(customerDomain);
        return ResponseEntity.status( HttpStatus.CREATED).body(customerMapper.toApiDto(createdCustomer));
    }

    @Override
    public ResponseEntity<CustomerResponseDto> getCustomerById(String userId) {
        Customer customer = customerService.getCustomerById(userId);
        return ResponseEntity.ok(customerMapper.toApiDto(customer));
    }

    @Override
    public ResponseEntity<CustomerResponseDto> updateCustomer(String userId, CustomerRequestDto customerDto) {
        Customer customerDomain = customerMapper.toDomain(customerDto);
        Customer updatedCustomer = customerService.updateCustomer(userId, customerDomain);
        return ResponseEntity.ok(customerMapper.toApiDto(updatedCustomer));
    }

    @Override
    public ResponseEntity<Void> deleteCustomer(String userId) {
        customerService.deleteCustomer(userId);
        return ResponseEntity.noContent().build();
    }
}
