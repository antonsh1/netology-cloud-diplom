package ru.smartjava.backend.security.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.smartjava.backend.config.Constants;

import java.io.IOException;

@Component
public class CorsFilter extends OncePerRequestFilter {

    @Value("${cloud-settings.origin-address}")
    private String originAddress;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, originAddress);
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, Constants.allowCredentials);
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, Constants.allowMethodsList);
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, Constants.allowHeadersList);
        if (HttpMethod.OPTIONS.name().equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
