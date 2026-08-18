package com.liverpool.pedidos.infrastructure.rest.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.liverpool.infrastructure.rest.dto.OrderSearchResponseDto;
import com.liverpool.pedidos.application.service.OrderSearchService;
import com.liverpool.pedidos.domain.util.StringNormalizer;
import com.liverpool.pedidos.infrastructure.mapper.OrderMapper;
import com.liverpool.web.api.OrdersApi;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OrdersApiImpl implements OrdersApi {


    private final OrderSearchService orderSearchService;
    private final OrderMapper orderMapper;

    @Override
    public ResponseEntity<List<OrderSearchResponseDto>> searchOrders(@NotNull @Valid String query) throws Exception {
        String normalizedQuery = StringNormalizer.normalize(query);

        if (normalizedQuery.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }

        // Llamar a aplicación con la query ya limpia
        var domainOrders = orderSearchService.searchOrders(normalizedQuery);
        return ResponseEntity.ok(orderMapper.toListApiDto(domainOrders));

    }

}
