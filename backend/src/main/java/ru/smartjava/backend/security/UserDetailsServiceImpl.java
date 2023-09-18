//package ru.smartjava.backend.security;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import ru.smartjava.backend.entity.EUser;
//import ru.smartjava.backend.repository.EUserRepository;
//
//@RequiredArgsConstructor
//public class UserDetailsServiceImpl implements UserDetailsService {
//
//    private final EUserRepository EUserRepository;
//
//    private final UserDetails userDetails;
//
//    @Override
//    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
//        EUser EUser = EUserRepository.findByLogin(login)
//                .orElseThrow(() -> new UsernameNotFoundException("Login Not Found with username: " + login));
//        return userDetails;
//    }
//}
