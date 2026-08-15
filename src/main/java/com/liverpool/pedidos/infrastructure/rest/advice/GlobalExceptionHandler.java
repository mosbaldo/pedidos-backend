package com.liverpool.pedidos.infrastructure.rest.advice;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.liverpool.pedidos.domain.exception.CustomerNotFoundException;

import lombok.extern.slf4j.Slf4j;

/**
 * Manejador global de excepciones para la aplicación.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Intercepta y maneja las excepciones de tipo CustomerNotFoundException
     * lanzadas en cualquier parte de la aplicación.
     */
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Object> handleCustomerNotFoundException(
            CustomerNotFoundException ex, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        log.warn("Cliente no encontrado en {}: {}", path, ex.getMessage());
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now(ZoneId.systemDefault()));
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());
        body.put("path", path);
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}
