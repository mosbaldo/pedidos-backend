package com.liverpool.pedidos.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuración centralizada de WebClient para llamadas HTTP externas.
 */
@Configuration
public class WebClientConfig {

    /**
     * Define un constructor base de WebClient.
     */
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
