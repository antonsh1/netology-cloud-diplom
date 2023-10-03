package ru.smartjava.backend.security.handlers;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Service;
import ru.smartjava.backend.config.Constants;
import ru.smartjava.backend.entity.ResponseMessage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler
        implements AuthenticationFailureHandler {
    private final Gson gson =  new Gson();
    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception)
            throws IOException {
//        response.setContentType("application/json");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        ResponseMessage responseMessage = new ResponseMessage(HttpServletResponse.SC_BAD_REQUEST,  Constants.userNotAuthorized);
        response.getWriter().println(gson.toJson(responseMessage));
        response.getWriter().flush();

    }
}


