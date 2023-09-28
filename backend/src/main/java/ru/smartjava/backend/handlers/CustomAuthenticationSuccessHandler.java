package ru.smartjava.backend.handlers;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import ru.smartjava.backend.config.Constants;
import ru.smartjava.backend.entity.EUser;
import ru.smartjava.backend.entity.ErrorMessage;
import ru.smartjava.backend.entity.TokenMessage;
import ru.smartjava.backend.repository.EUserRepository;

import java.io.IOException;

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
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF8");
        EUser eUser = eUserRepository.findByLogin(authentication.getName()).orElseThrow(
                () -> new UsernameNotFoundException(Constants.userNotFound)
        );
        if (eUser.getToken() == null) {
            throw new UsernameNotFoundException(Constants.userNotFound);
        }
        response.getWriter().println(gson.toJson(new TokenMessage(eUser.getToken())));
        response.getWriter().flush();
    }
}
