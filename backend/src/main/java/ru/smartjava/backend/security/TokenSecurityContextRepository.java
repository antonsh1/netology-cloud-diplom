package ru.smartjava.backend.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TokenSecurityContextRepository implements SecurityContextRepository {

    private final MyUserDetailsService userDetailsService;

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        HttpServletRequest request = requestResponseHolder.getRequest();
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new UsernameNotFoundException("");
//            return context;
        }
//            return context;
//        Optional<String> token = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("auth-token")).map(Cookie::getValue).findAny();
        String token = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("auth-token")).map(Cookie::getValue).findAny().orElseThrow(
                () -> new UsernameNotFoundException("auth-token not found")
        );
//        if(token.isEmpty()) {
//            return context;
//        }

        UserDetails userDetails = userDetailsService.loadUserByToken(token);
        if (!userDetails.isEnabled()) {
            throw new UsernameNotFoundException("");
//            return context;
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());
        context.setAuthentication(usernamePasswordAuthenticationToken);

        return context;
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        String token = null;
        Cookie[] allCookie = request.getCookies();
        if (allCookie != null) {
            if (allCookie.length > 0) {
                Optional<Cookie> tmpToken = Arrays.stream(allCookie).filter(cookie -> cookie.getName().equals("auth-token")).findFirst();
                if (tmpToken.isPresent()) {
                    token = tmpToken.get().getValue();
                }
            }
        }
        return token != null;
    }

}
