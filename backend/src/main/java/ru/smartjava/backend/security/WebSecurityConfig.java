package ru.smartjava.backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig  {

    private final UserDetailsService userDetailsService;

    private final AuthenticationManagerBuilder authManagerBuilder;

    private final CustomAuthenticationSuccessHandler customSuccessHandler;

    private final CustomAuthenticationFailureHandler customFailureHandler;

    private final CorsFilter corsFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
//        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CustomFilter mupaf = new CustomFilter();
        mupaf.setAuthenticationSuccessHandler(customSuccessHandler);
        mupaf.setAuthenticationFailureHandler(customFailureHandler);
        mupaf.setAuthenticationManager(authManagerBuilder.getOrBuild());
        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(corsFilter, ChannelProcessingFilter.class)
                .addFilterAt(mupaf, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated())
//                .authorizeHttpRequests((requests) -> requests
//                        .requestMatchers("/test/check").authenticated()
//                        .requestMatchers("/login").permitAll()
//                        .anyRequest().authenticated())
//                .formLogin(withDefaults())
                .logout(LogoutConfigurer::permitAll);
        return http.build();
    }

//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(List.of("http://localhost:8080"));
//        configuration.setAllowedMethods(Arrays.asList("GET","POST"));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }


//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests((requests) -> requests
//                        .requestMatchers("/persons/by-city").permitAll()
//                        .requestMatchers("/persons/by-age-less").hasAnyRole("AGE","ALL")
//                        .requestMatchers("/persons/by-name-surename").hasRole("ALL")
//                        .anyRequest().authenticated()
//                )
//                .formLogin(withDefaults()
//                )
//                .logout(LogoutConfigurer::permitAll);
//
//        return http.build();
//    }

}