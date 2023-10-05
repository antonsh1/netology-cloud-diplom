package ru.smartjava.backend.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.smartjava.backend.config.Constants;
import ru.smartjava.backend.model.ERole;
import ru.smartjava.backend.model.EUser;
import ru.smartjava.backend.repositories.EUserRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    private final EUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        EUser user = userRepository
                .findByLogin(login)
                .orElseThrow(() ->
                        new AuthenticationServiceException(String.format("%s %s", Constants.userNotFound, login))
                );

        return new org.springframework.security.core.userdetails.User(
                user.getLogin(), user.getPassword(), true,
                true, true,
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
