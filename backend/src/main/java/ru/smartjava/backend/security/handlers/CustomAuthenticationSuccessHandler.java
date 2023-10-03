package ru.smartjava.backend.security.handlers;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import ru.smartjava.backend.config.Constants;
import ru.smartjava.backend.model.EUser;
import ru.smartjava.backend.entity.TokenMessage;
import ru.smartjava.backend.repositories.EUserRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final EUserRepository eUserRepository;
    private final Gson gson = new Gson();

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {
//        response.setContentType("application/json");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        EUser eUser = eUserRepository
                .findByLogin(authentication.getName())
                .orElseThrow(() -> new AuthenticationServiceException(Constants.userNotFound));
        if (eUser.getToken() == null) {
            throw new AuthenticationServiceException(Constants.userNotFound);
        }
        response.getWriter().println(gson.toJson(new TokenMessage(eUser.getToken())));
        response.getWriter().flush();
    }
}
