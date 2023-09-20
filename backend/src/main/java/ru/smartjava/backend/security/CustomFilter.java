package ru.smartjava.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class CustomFilter extends AbstractAuthenticationProcessingFilter {

    protected CustomFilter() {
        super(new AntPathRequestMatcher("/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String username, password;
        Cookie[] testCookies = request.getCookies();
        String token = null;
        if( testCookies != null || testCookies.length > 0 ) {
            token = Arrays.stream(testCookies).filter(cookie -> cookie.getName().equals("auth-token")).findFirst().get().getValue();
        }
        System.out.println(token);
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        Optional tokenParam = Optional.ofNullable(request.getHeader(AUTHORIZATION)); //Authorization: Bearer TOKEN
        String test = request.getHeader(AUTHORIZATION);
        try {
            Map<String, String> requestMap = new ObjectMapper().readValue(request.getInputStream(), Map.class);
            username = requestMap.get("login");
            password = requestMap.get("password");
        } catch (IOException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

        return getAuthenticationManager()
                .authenticate(authRequest);
    }

}