package ru.smartjava.backend.handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import ru.smartjava.backend.config.Constants;
import ru.smartjava.backend.entity.EUser;
import ru.smartjava.backend.repository.EUserRepository;

@Service
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final EUserRepository eUserRepository;

    @SneakyThrows
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
                       Authentication authentication) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        if (authentication != null) {
            EUser eUser = eUserRepository.findByLogin(authentication.getName()).orElseThrow(
                    () -> new UsernameNotFoundException(Constants.userNotFound)
            );
            eUserRepository.clearTokenForUserById(eUser.getId());
            response.setStatus(HttpServletResponse.SC_OK);

        }
        response.getOutputStream().flush();
    }
}