package com.liverpool.pedidos.infrastructure.rest.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.liverpool.pedidos.infrastructure.rest.client.dto.OrderDto;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.netty.channel.ChannelOption;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.netty.http.client.HttpClient;
import java.time.Duration;

/**
 * Cliente HTTP mock para llamadas a la API externa de pedidos.
 */
@Component
@Slf4j
public class MockPedidosApiClient {

        private final WebClient pedidosWebClient;

        public MockPedidosApiClient(
                        WebClient.Builder builder,
                        @Value("${app.external-api.pedidos.url}") String pedidosUrl,
                        @Value("${app.external-api.pedidos.timeout}") int pedidosTimeout) {
                this.pedidosWebClient = builder.clone()
                                .baseUrl(pedidosUrl)
                                .clientConnector(new ReactorClientHttpConnector(createHttpClient(pedidosTimeout)))
                                .build();
        }

        /**
         * Obtiene todos los pedidos desde la API externa de /pedidos.
         * Aplica un patrón de Circuit Breaker y Retry para manejar fallas en la
         * comunicación.
         * @return strean de datos reactivo
         */
        @Retry(name = "pedidosApi")
        @CircuitBreaker(name = "pedidosApi", fallbackMethod = "getAllPedidosFallback")
        public Flux<OrderDto> getAllOrders() {
                log.info("Llamando a MockAPI de pedidos");
                return pedidosWebClient.get()
                                .retrieve()
                                .bodyToFlux(OrderDto.class);
        }

        /**
         * Obtiene los pedidos asociados a un userId desde la API externa de /pedidos.
         * Aplica un patrón de Circuit Breaker y Retry para manejar fallas en la
         * comunicación.
         * @param userId
         * @return strean de datos reactivo
         */
        @Retry(name = "pedidosApi")
        @CircuitBreaker(name = "pedidosApi", fallbackMethod = "pedidosFallback")
        public Flux<OrderDto> getOrdersByUserId(String userId) {
                log.info("Llamando a MockAPI de pedidos para el userId: {}", userId);
                return pedidosWebClient.get()
                                .retrieve()
                                .bodyToFlux(OrderDto.class)
                                .filter(order -> userId.equals(order.getUserId()));
        }

        /**
         * Método de fallback para manejar errores en la llamada a la API externa de pedidos.
         * @param timeoutMs
         * @return
         */
        public Flux<OrderDto> getAllPedidosFallback(Throwable ex) {
                log.error("Error al llamar a MockAPI de pedidos");
                return Flux.empty();
        }

        /**
         * Método de fallback para manejar errores en la llamada a la API externa de pedidos.
         * @param timeoutMs
         * @return
         */
        public Flux<OrderDto> pedidosFallback(String userId, Throwable ex) {
                log.error("Error al llamar a MockAPI de pedidos para el userId: {}. Error: {}", userId, ex.getMessage());
                return Flux.empty();
        }

        /**
         * Crea un HttpClient con un tiempo de espera configurado.
         * @param timeoutMs Tiempo de espera en milisegundos.
         * @return
         */
        private HttpClient createHttpClient(int timeoutMs) {
                return HttpClient.create()
                                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeoutMs)
                                .responseTimeout(Duration.ofMillis(timeoutMs));
        }
}
