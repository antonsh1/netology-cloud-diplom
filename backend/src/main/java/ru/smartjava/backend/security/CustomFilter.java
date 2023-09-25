package ru.smartjava.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.smartjava.backend.config.Constants;
import ru.smartjava.backend.entity.LoginEntity;

import java.io.IOException;

public class CustomFilter extends AbstractAuthenticationProcessingFilter {

    public CustomFilter() {
        super(new AntPathRequestMatcher(Constants.loginPattern, Constants.loginMethod));
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        LoginEntity loginEntity;
        try {
            loginEntity = new ObjectMapper().readValue(request.getInputStream(), LoginEntity.class);
        } catch (IOException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(loginEntity.getLogin(), loginEntity.getPassword());

        return getAuthenticationManager()
                .authenticate(authRequest);
    }

}