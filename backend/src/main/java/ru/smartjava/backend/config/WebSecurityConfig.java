package ru.smartjava.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.smartjava.backend.security.handlers.CustomAuthenticationFailureHandler;
import ru.smartjava.backend.security.handlers.CustomAuthenticationSuccessHandler;
import ru.smartjava.backend.security.handlers.CustomLogoutHandler;
import ru.smartjava.backend.security.filters.CorsFilter;
import ru.smartjava.backend.security.filters.CustomFilter;
import ru.smartjava.backend.security.TokenSecurityContextRepository;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {

    private final AuthenticationManagerBuilder authManagerBuilder;
    private final CustomAuthenticationSuccessHandler customSuccessHandler;
    private final CustomAuthenticationFailureHandler customFailureHandler;
    private final CustomLogoutHandler customLogoutHandler;
    private final CorsFilter corsFilter;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, TokenSecurityContextRepository tokenSecurityContextRepository) throws Exception {

        CustomFilter customFilter = new CustomFilter();
        customFilter.setAuthenticationSuccessHandler(customSuccessHandler);
        customFilter.setAuthenticationFailureHandler(customFailureHandler);
        customFilter.setAuthenticationManager(authManagerBuilder.getOrBuild());

        httpSecurity
                .exceptionHandling(handler -> handler.authenticationEntryPoint(authenticationEntryPoint))
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer ->
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .securityContext(context -> context.securityContextRepository(tokenSecurityContextRepository))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(corsFilter, ChannelProcessingFilter.class)
                .addFilterAt(customFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated())
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(logout -> logout.addLogoutHandler(customLogoutHandler));

        return httpSecurity.build();
    }
}