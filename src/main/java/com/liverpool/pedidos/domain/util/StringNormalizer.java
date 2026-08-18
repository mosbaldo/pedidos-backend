package com.liverpool.pedidos.domain.util;

import java.text.Normalizer;
import java.util.regex.Pattern;


public class StringNormalizer {
    private StringNormalizer() {}

    private static final Pattern DIACRITICS_PATTERN = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

    /**
     * Normaliza un texto eliminando acentos, comas, mayúsculas y espacios
     * duplicados.
     */
    public static String normalize(String input) {
        if (input == null) {
            return "";
        }
        // 1. Convertir a minúsculas
        String normalized = input.toLowerCase().trim();

        normalized = Normalizer.normalize(normalized, Normalizer.Form.NFD);

        // 3. Remover los acentos mediante expresión regular
        normalized = DIACRITICS_PATTERN.matcher(normalized).replaceAll("");

        // 4. Remover caracteres especiales no deseados (comas, puntos, guiones)
        normalized = normalized.replaceAll("[,.\\-_#]", " ");

        // 5. Normalizar múltiples espacios en blanco a uno sólo
        return normalized.replaceAll("\\s+", " ").trim();
    }

}
