package ru.smartjava.backend.security;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ru.smartjava.backend.entity.EUser;
import ru.smartjava.backend.entity.TokenMessage;
import ru.smartjava.backend.repository.EUserRepository;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final EUserRepository eUserRepository;
    private final Gson gson =  new Gson();
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
//        System.out.println(authentication.getName() + " : " + authentication.getAuthorities());
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Optional<EUser> eUserOptional = eUserRepository.findByLogin(authentication.getName());
        TokenMessage tokenMessage = eUserOptional.map(eUser -> new TokenMessage(eUser.getToken())).orElseGet(() -> new TokenMessage(""));
        out.print(gson.toJson(tokenMessage));
        out.flush();
    }
}
