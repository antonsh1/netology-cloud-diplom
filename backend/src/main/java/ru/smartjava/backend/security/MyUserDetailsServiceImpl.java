package ru.smartjava.backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.smartjava.backend.config.Constants;
import ru.smartjava.backend.entity.ERole;
import ru.smartjava.backend.entity.EUser;
import ru.smartjava.backend.repositories.EUserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyUserDetailsServiceImpl implements MyUserDetailsService {

    private final EUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        EUser user = userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException(String.format("%s %s", Constants.userNotFound ,login)));
        user.setToken(UUID.randomUUID().toString());
        userRepository.save(user);
        return new org.springframework.security.core.userdetails.User(
                user.getLogin(), user.getPassword(), true,
                true, true,
                true, getAuthorities(user.getRoles()));
    }

    @Override
    public UserDetails loadUserByToken(String token) throws UsernameNotFoundException {
        EUser user = userRepository.findByToken(token).orElseThrow(() ->
                new UsernameNotFoundException(String.format("%s %s",Constants.tokenNotFound,token))
        );
        return new org.springframework.security.core.userdetails.User(
                user.getLogin(), user.getPassword(), true, true, true,
                true, getAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(
            Collection<ERole> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(Collection<ERole> roles) {
        return roles.stream().map(ERole::getName).collect(Collectors.toList());
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        return privileges.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
