package com.fiap.finpath.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AdminAuthenticationFilter extends OncePerRequestFilter {

    private static final String ADMIN_TOKEN_HEADER = "X-Admin-Token";
    
    @Value("${app.admin.token:admin-secure-token-2024-finpath}")
    private String adminToken;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String requestPath = request.getRequestURI();
        
        // Endpoints públicos que não precisam de autenticação
        boolean isPublicEndpoint = isPublicEndpoint(requestPath, request.getMethod());
        
        if (!isPublicEndpoint) {
            // Verificar se o header de admin token está presente e é válido
            String requestToken = request.getHeader(ADMIN_TOKEN_HEADER);
            
            if (requestToken == null || !requestToken.equals(adminToken)) {
                // Token inválido ou ausente
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Unauthorized\",\"message\":\"Invalid or missing admin token\"}");
                return;
            }
        }
        
        filterChain.doFilter(request, response);
    }
    
    private boolean isPublicEndpoint(String path, String method) {
        // Endpoints públicos que não precisam de autenticação
        return path.equals("/api/users/login") 
            || path.equals("/api/users/register")
            || path.equals("/api/users/admin-token")
            || (path.equals("/error") && "GET".equals(method));
    }
}

