package com.Cristofer.SoftComerce.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Permitir solicitudes desde múltiples orígenes
        config.setAllowedOrigins(Arrays.asList(
            "http://127.0.0.1:5500",
            "http://localhost:8080",
            "http://example.com"
        ));

        // Permitir solicitudes con estos métodos HTTP
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        // Permitir ciertos encabezados en las solicitudes
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        // Permitir envío de credenciales (cookies/headers de autorización)
        config.setAllowCredentials(true);

        // Registrar la configuración para todas las rutas
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}