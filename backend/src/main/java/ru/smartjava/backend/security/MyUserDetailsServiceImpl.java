package ru.smartjava.backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.smartjava.backend.entity.ERole;
import ru.smartjava.backend.entity.EUser;
import ru.smartjava.backend.repository.EUserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyUserDetailsServiceImpl implements MyUserDetailsService {

    private final EUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        EUser user = userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("User not found " + login));;
//        if (tmpUser.isEmpty()) {
//            throw new UsernameNotFoundException(login);
////            return org.springframework.security.core.userdetails.User.builder().build();
////            return org.springframework.security.core.userdetails.User
////                    .builder()
////                    .username(" ")
////                    .password(" ")
////                    .authorities("")
////                    .accountExpired(false)
////                    .credentialsExpired(false)
////                    .accountLocked(false)
////                    .build();
//        }
//        EUser user = tmpUser.get();
        user.setToken(UUID.randomUUID().toString());
        userRepository.save(user);
//        org.springframework.security.core.userdetails.User
//                .builder()
//                .username(user.getLogin())
//                .password(user.getPassword())
////                .roles(user.getRoles().stream().map(ERole::getName).collect(Collectors.toList()))
//                .authorities("")
//                .accountExpired(false)
//                .credentialsExpired(false)
//                .accountLocked(false)
//                .build();
        return new org.springframework.security.core.userdetails.User(
                user.getLogin(), user.getPassword(), true,
                true, true,
                true, getAuthorities(user.getRoles()));
    }

    @Override
    public UserDetails loadUserByToken(String token) throws UsernameNotFoundException {
        EUser user = userRepository.findByToken(token).orElseThrow(() -> new UsernameNotFoundException("Token not found " + token));
//        if (tmpUser.isEmpty()) {
//            throw new UsernameNotFoundException(token);
////            return new org.springframework.security.core.userdetails.User(
////                    " ", " ", false, true, true,
////                    true, getAuthorities( Collections.emptyList()));
////            return org.springframework.security.core.userdetails.User
////                    .builder()
////                    .disabled(true)
////                    .username(" ")
////                    .password(" ")
////                    .authorities(" ")
////                    .accountExpired(false)
////                    .credentialsExpired(false)
////                    .accountLocked(false)
////                    .build();
//        }
        return new org.springframework.security.core.userdetails.User(
                user.getLogin(), user.getPassword(), true, true, true,
                true, getAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(
            Collection<ERole> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(Collection<ERole> roles) {
//
//        List<String> privileges = new ArrayList<>();
//        for (ERole role : roles) {
//            privileges.add(role.getName());
//        }
//        return privileges;
        return roles.stream().map(ERole::getName).collect(Collectors.toList());
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
//        List<GrantedAuthority> test = privileges.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        for (String privilege : privileges) {
//            authorities.add(new SimpleGrantedAuthority(privilege));
//        }
//        return authorities;
        return privileges.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
