package com.Cristofer.SoftComerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.Cristofer.SoftComerce.jwt.JwtAuthenticationFilter;
import com.Cristofer.SoftComerce.repository.IUser;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth

                // Endpoints públicos
                .requestMatchers(
                    "/api/v1/public/users/register",
                    "/api/v1/public/users/login",
                    "/api/v1/products/**",
                    "/api/v1/category/**",
                    "/api/v1/review/**",
                    "/api/recovery-requests/create",
                    "/api/v1/products/filter",
                    "/api/v1/category/filter",
                    "/api/v1/review/filter",
                    "/password/reset"
                ).permitAll()

                // Endpoints accesibles por USUARIO, SUPERVISOR y ADMIN
                .requestMatchers(
                    "/api/users/profile"
                ).hasAnyRole("USUARIO", "SUPERVISOR", "ADMIN")

                // Endpoints accesibles por USUARIO, SUPERVISOR, VENDEDOR y ADMIN
                .requestMatchers(
                    "/api/users/{id}",
                                "/password/change"
                ).hasAnyRole("USUARIO", "SUPERVISOR", "VENDEDOR", "ADMIN")

                // Endpoints accesibles por VENDEDOR, SUPERVISOR y ADMIN
                .requestMatchers(
                    "/api/users/",
                    "/api/v1/products/",
                    "/api/v1/products/{id}",
                    "/api/v1/products/{id}/status",
                    "/api/v1/products/{id}/reactivate",
                    "/api/v1/category/",
                    "/api/v1/category/{id}",
                    "/api/v1/order/",
                    "/api/v1/order/{id}",
                    "/api/v1/order/filter",
                    "/api/v1/shipping/",
                    "/api/v1/shipping/{id}",
                    "/api/v1/shipping/deactivate/{id}",
                    "/api/v1/shipping/reactivate/{id}",
                    "/api/v1/payment/",
                    "/api/v1/payment/{id}",
                    "/api/v1/payment/filter"
                ).hasAnyRole("VENDEDOR", "SUPERVISOR", "ADMIN")

                // Endpoints accesibles por SUPERVISOR y ADMIN
                .requestMatchers(
                    "/api/v1/products/filter",
                    "/api/v1/category/filter"
                ).hasAnyRole("SUPERVISOR", "ADMIN")

                // Endpoints accesibles por USUARIO y ADMIN
                .requestMatchers(
                    "/api/recovery-requests/user/**",
                    "/api/recovery-requests/{id}"
                ).hasAnyRole("USUARIO", "ADMIN")

                // Endpoints accesibles por USUARIO, VENDEDOR, SUPERVISOR y ADMIN
                .requestMatchers(
                    "/api/v1/order/**",
                    "/api/v1/shipping/**",
                    "/api/v1/payment/**",
                    "/api/v1/review/",
                    "/api/v1/review/{id}",
                    "/api/v1/review/filter"
                ).hasAnyRole("USUARIO", "VENDEDOR", "SUPERVISOR", "ADMIN")

                // Endpoints exclusivos para USUARIO
                .requestMatchers(
                    "/api/users/*"
                ).hasRole("USUARIO")

                // Endpoints exclusivos para ADMIN
                .requestMatchers(
                    "/api/users/**",
                    "/api/roles/**",
                    "/api/permission-roles/**",
                    "/api/pages/**",
                    "/api/page-roles/**",
                    "/api/v1/products/**",
                    "/api/v1/category/**",
                    "/api/v1/order/**",
                    "/api/v1/shipping/**",
                    "/api/v1/payment/**",
                    "/api/v1/paymentorder/**",
                    "/api/v1/orderproduct/**",
                    "/api/v1/review/**",
                    "/api/recovery-requests/**"
                ).hasRole("ADMIN")

                .anyRequest().authenticated()
            );
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
                                                        PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(IUser userRepository) {
        return username -> userRepository.findByEmail(username)
            .orElseThrow(() -> new org.springframework.security.core.userdetails.UsernameNotFoundException("Usuario no encontrado"));
    }
}