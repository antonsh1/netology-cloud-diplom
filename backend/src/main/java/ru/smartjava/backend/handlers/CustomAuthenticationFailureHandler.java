package ru.smartjava.backend.handlers;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Service;
import ru.smartjava.backend.config.Constants;
import ru.smartjava.backend.entity.ErrorMessage;

import java.io.IOException;

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
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setCharacterEncoding("UTF8");
        ErrorMessage errorMessage = new ErrorMessage(HttpServletResponse.SC_BAD_REQUEST,  Constants.userNotAuthorized);
        response.getWriter().println(gson.toJson(errorMessage));
        response.getWriter().flush();

    }
}


