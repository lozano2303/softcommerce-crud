package com.Cristofer.SoftComerce.jwt;

import java.io.IOException;
import java.util.Optional;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.Cristofer.SoftComerce.model.User;
import com.Cristofer.SoftComerce.repository.IUser;
import com.Cristofer.SoftComerce.service.jwt.JwtService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * Filtro JWT unificado. 
 * - Usa roles/authorities de la entidad User como antes.
 * - Permite autenticación tanto desde repositorio como UserDetailsService.
 * - Extrae el userID desde el token y lo expone como atributo en la request.
 * - Manejo de expiración e invalidez del token.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final IUser userRepository;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String jwt = null;
        String username = null;
        Integer userId = null;

        // Extracción segura del token y usuario
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                username = jwtService.extractUsername(jwt);
                userId = jwtService.extractUserId(jwt); // extrae el userId del token
            } catch (ExpiredJwtException e) {
                logger.warn("JWT expired: " + e.getMessage());
            } catch (JwtException e) {
                logger.warn("JWT invalid: " + e.getMessage());
            }
        }

        // Exponer el userId del token como atributo en la request (si existe)
        if (userId != null) {
            request.setAttribute("userID", userId);
        }

        // Solo procesar si hay username y no hay autenticación previa
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Primero intenta con repositorio propio
            Optional<User> optionalUser = userRepository.findByEmail(username);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                UserDetails userDetails = user; // authorities/roles vienen desde aquí
                if (jwtService.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities() // roles se mantienen igual
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    filterChain.doFilter(request, response);
                    return;
                }
            } else {
                // Si no está en el repositorio, intenta cargarlo usando UserDetailsService estándar
                try {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    if (jwtService.validateToken(jwt, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities()
                                );
                        authToken.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request)
                        );
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                } catch (UsernameNotFoundException ex) {
                    logger.warn("User not found in UserDetailsService: " + ex.getMessage());
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}