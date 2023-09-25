package ru.smartjava.backend.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface MyUserDetailsService extends UserDetailsService {

    UserDetails loadUserByUsername(String login);
    UserDetails loadUserByToken(String token);
}
