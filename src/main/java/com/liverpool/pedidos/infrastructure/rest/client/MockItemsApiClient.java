package com.liverpool.pedidos.infrastructure.rest.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.liverpool.pedidos.infrastructure.rest.client.dto.ItemDto;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.netty.channel.ChannelOption;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.netty.http.client.HttpClient;
import java.time.Duration;

/**
 * Cliente HTTP mock para llamadas a la API externa de items.
 */
@Component
@Slf4j
public class MockItemsApiClient {

        private final WebClient itemsWebClient;

        public MockItemsApiClient(
                        WebClient.Builder builder,
                        @Value("${app.external-api.items.url}") String itemsUrl,
                        @Value("${app.external-api.items.timeout}") int itemsTimeout) {
                this.itemsWebClient = builder.clone()
                                .baseUrl(itemsUrl)
                                .clientConnector(new ReactorClientHttpConnector(createHttpClient(itemsTimeout)))
                                .build();
        }

        /**
         * Obtiene los items asociados a un pedido desde la API externa de /items.
         * Aplica un patrón de Circuit Breaker y Retry para manejar fallas en la
         * comunicación.
         * @param itemId
         * @return strean de datos reactivo
         */
        @Retry(name = "itemsApi")
        @CircuitBreaker(name = "itemsApi", fallbackMethod = "itemsFallback")
        public Flux<ItemDto> getAllItems() {
                log.info("Llamando a MockAPI de items");
                return itemsWebClient.get()
                                .retrieve()
                                .bodyToFlux(ItemDto.class);
        }

        /**
         * Método de fallback para manejar errores en la llamada a la API externa de pedidos.
         * @param timeoutMs
         * @return
         */
        public Flux<ItemDto> itemsFallback(Throwable ex) {
                log.error("Error al llamar a MockAPI de items");
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
