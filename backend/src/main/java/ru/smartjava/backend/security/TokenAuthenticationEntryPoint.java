package ru.smartjava.backend.security;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import ru.smartjava.backend.entity.ResponseMessage;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

@Component
public class TokenAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private final Gson gson = new Gson();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        ResponseMessage responseMessage = new ResponseMessage(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
        response.getWriter().println(gson.toJson(responseMessage));
        response.getWriter().flush();

    }
}
