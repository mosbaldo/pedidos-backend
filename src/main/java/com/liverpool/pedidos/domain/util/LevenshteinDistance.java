package com.liverpool.pedidos.domain.util;

public class LevenshteinDistance {
    private LevenshteinDistance() {}

    /**
     * Calcula la distancia de edición entre dos cadenas normalizadas.
     */
    public static int calculate(String s1, String s2) {
        if (s1 == null || s2 == null) {
            return Integer.MAX_VALUE;
        }

        int len1 = s1.length();
        int len2 = s2.length();

        int[][] dp = new int[len1 + 1][len2 + 1];

        for (int i = 0; i <= len1; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= len2; j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(
                            dp[i - 1][j - 1], // Sustitución
                            Math.min(
                                    dp[i - 1][j], // Eliminación
                                    dp[i][j - 1] // Inserción
                            ));
                }
            }
        }
        return dp[len1][len2];
    }

    /**
     * Determina si dos palabras son similares bajo un umbral adaptativo.
     */
    public static boolean isSimilar(String term, String target) {
        if (term.isEmpty() || target.isEmpty()) {
            return false;
        }
        // Si el término de búsqueda está contenido exactamente, es coincidencia directa
        if (target.contains(term)) {
            return true;
        }

        // Umbral adaptativo según el tamaño de la palabra buscada
        int threshold = term.length() <= 4 ? 1 : 2; // Máximo 1 error para cortas, 2 para largas.

        // Soporte para búsqueda difusa por sub-palabras (tokenización)
        String[] targetTokens = target.split(" ");
        for (String token : targetTokens) {
            if (calculate(term, token) <= threshold) {
                return true;
            }
        }
        return false;
    }
}
