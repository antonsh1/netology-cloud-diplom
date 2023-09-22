package ru.smartjava.backend.handlers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.smartjava.backend.entity.EUser;
import ru.smartjava.backend.entity.ErrorMessage;
import ru.smartjava.backend.entity.TokenMessage;
import ru.smartjava.backend.repository.EUserRepository;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final EUserRepository eUserRepository;
    private final Gson gson =  new Gson();
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json");
        EUser eUser = eUserRepository.findByLogin(authentication.getName()).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );
        if(eUser.getToken() == null) {
            throw new UsernameNotFoundException("User not found");
        }
        response.getOutputStream().println(gson.toJson(new TokenMessage(eUser.getToken())));
    }

}
