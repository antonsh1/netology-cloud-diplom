package ru.smartjava.backend.security;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import ru.smartjava.backend.entity.ErrorMessage;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

@Component
public class TokenAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private final Gson gson =  new Gson();
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF8");
        ErrorMessage errorMessage = new ErrorMessage(HttpServletResponse.SC_UNAUTHORIZED,authException.getMessage());
        response.getWriter().println(gson.toJson(errorMessage));
        response.getWriter().flush();
    }
}
