package ru.smartjava.backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.smartjava.backend.entity.ERole;
import ru.smartjava.backend.entity.EUser;
//import ru.smartjava.backend.repository.ERoleRepository;
import ru.smartjava.backend.repository.EUserRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final EUserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<EUser> tmpUser = userRepository.findByLogin(login);
        if (!tmpUser.isPresent()) {
            return new org.springframework.security.core.userdetails.User(
                    " ", " ", true, true, true, true,
                    getAuthorities(Collections.EMPTY_SET));
        }
        EUser user = tmpUser.get();
        Collection<ERole> test = user.getRoles();
        String encryptedPassword = passwordEncoder.encode("password");
        return new org.springframework.security.core.userdetails.User(
                user.getLogin(), user.getPassword(), true, true, true,
                true, getAuthorities(user.getRoles()));


    }

    private Collection<? extends GrantedAuthority> getAuthorities(
            Collection<ERole> roles) {

        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(Collection<ERole> roles) {

        List<String> privileges = new ArrayList<>();
        for (ERole role : roles) {
            privileges.add(role.getName());
        }
        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {

            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}
