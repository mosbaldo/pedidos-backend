package com.liverpool.pedidos.application.service.impl;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.liverpool.pedidos.application.service.OrderSearchService;
import com.liverpool.pedidos.domain.model.Item;
import com.liverpool.pedidos.domain.model.Order;
import com.liverpool.pedidos.domain.util.LevenshteinDistance;
import com.liverpool.pedidos.domain.util.StringNormalizer;
import com.liverpool.pedidos.infrastructure.mapper.ItemMapper;
import com.liverpool.pedidos.infrastructure.mapper.OrderMapper;
import com.liverpool.pedidos.infrastructure.rest.client.MockItemsApiClient;
import com.liverpool.pedidos.infrastructure.rest.client.MockPedidosApiClient;
import com.liverpool.pedidos.infrastructure.rest.client.dto.OrderDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación concreta del servicio de búsqeuda de pedidos.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderSearchServiceImpl implements OrderSearchService {

    private final MockPedidosApiClient mockPedidosApiClient;
    private final MockItemsApiClient mockItemsApiClient;
    private final OrderMapper orderMapper;
    private final ItemMapper itemMapper;

    @Override
    public List<Order> searchOrders(String nomalizedQuery) {
        List<Order> orders = fetchAllExternalOrders();
        List<Item> catalogItems = fetchAllExternalItems();
        Map<String, Item> itemsMap = catalogItems.stream()
                .collect(Collectors.toMap(Item::getItemId, item -> item, (a, b) -> a));
        return orders.stream()
                .peek(order -> enrichOrderItems(order, itemsMap))
                .filter(order -> matchesPredictiveFilter(order, nomalizedQuery))
                .toList();
    }

    /**
     * Utilidad privada que realiza el puente síncrono para consumir los pedidos
     * reactivos.
     */
    private List<Order> fetchAllExternalOrders() {
        try {
            log.info("Consumiendo MockAPI para recuperar todos los pedidos");

            List<OrderDto> orderDtos = mockPedidosApiClient.getAllOrders()
                    .collectList()
                    .block(Duration.ofSeconds(8));

            return orderDtos.stream()
                    .map(orderMapper::toDomain)
                    .toList();
        } catch (Exception ex) {
            log.error("Falló la obtención de pedidos",
                    ex.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Utilidad privada que realiza el puente síncrono para consumir los productos
     * reactivos.
     */
    private List<Item> fetchAllExternalItems() {
        try {
            log.info("Consumiendo MockAPI para recuperar todos los items");

            return mockItemsApiClient.getAllItems()
                    .collectList()
                    .block(Duration.ofSeconds(8)).stream()
                    .map(itemMapper::toDomain)
                    .toList();
        } catch (Exception ex) {
            log.error("Falló la obtención de items",
                    ex.getMessage());
            return Collections.emptyList();
        }
    }

    private void enrichOrderItems(Order order, Map<String, Item> itemsMap) {
        if (order.getItems() != null) {
            List<Item> enriched = order.getItems().stream()
                    .map(item -> {
                        Item catalogItem = itemsMap.get(item.getItemId());
                        if (catalogItem != null) {
                            item.setId(catalogItem.getId());
                            item.setSkuId(catalogItem.getSkuId());
                            item.setDisplayName(catalogItem.getDisplayName());
                            item.setQuantity(catalogItem.getQuantity());
                            item.setDeliveryStatus(catalogItem.getDeliveryStatus());
                        } else {
                            log.warn("El artículo con ID {} asociado al pedido {} no se encontró en el catálogo de /items",
                                    item.getItemId(), order.getOrderRef());
                        }
                        return item;
                    })
                    .filter(Objects::nonNull)
                    .toList();
            order.setItems(enriched);
        }
    }

    private boolean matchesPredictiveFilter(Order order, String query) {
        if (order.getOrderRef() != null && StringNormalizer.normalize(order.getOrderRef()).contains(query)) {
            return true;
        }

        if (order.getStoreName() != null
                && LevenshteinDistance.isSimilar(query, StringNormalizer.normalize(order.getStoreName()))) {
            return true;
        }

        if (order.getOrderStatus() != null
                && LevenshteinDistance.isSimilar(query, StringNormalizer.normalize(order.getOrderStatus()))) {
            return true;
        }

        if (order.getItems() != null) {
            for (Item item : order.getItems()) {
                if (item.getDisplayName() != null &&
                        LevenshteinDistance.isSimilar(query, StringNormalizer.normalize(item.getDisplayName()))) {
                    return true;
                }
            }
        }

        return false;
    }

}
