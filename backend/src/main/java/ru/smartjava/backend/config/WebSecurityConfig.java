package ru.smartjava.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.smartjava.backend.handlers.CustomAuthenticationFailureHandler;
import ru.smartjava.backend.handlers.CustomAuthenticationSuccessHandler;
import ru.smartjava.backend.handlers.CustomLogoutHandler;
import ru.smartjava.backend.security.CorsFilter;
import ru.smartjava.backend.security.CustomFilter;
import ru.smartjava.backend.security.TokenSecurityContextRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.web.cors.CorsConfiguration.ALL;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {

    private final UserDetailsService userDetailsService;

    private final AuthenticationManagerBuilder authManagerBuilder;

    private final CustomAuthenticationSuccessHandler customSuccessHandler;

    private final CustomAuthenticationFailureHandler customFailureHandler;
    private final CustomLogoutHandler customLogoutHandler;
    private final CorsFilter corsFilter;
    private final AuthenticationEntryPoint authenticationEntryPoint;

//    public ObjectMapper configureJson() {
//        return new Jackson2ObjectMapperBuilder()
//                .indentOutput(true)
//                .propertyNamingStrategy(PropertyNamingStrategies.UPPER_CAMEL_CASE)
//                .build();
//    }
//
//    @Bean
//    public Jackson2ObjectMapperBuilderCustomizer customizeJson() {
//        return builder -> {
//
//            builder.indentOutput(true);
//            builder.propertyNamingStrategy(PropertyNamingStrategies.UPPER_CAMEL_CASE);
//        };
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
//        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, TokenSecurityContextRepository tokenSecurityContextRepository) throws Exception {

        CustomFilter customFilter = new CustomFilter();
        customFilter.setAuthenticationSuccessHandler(customSuccessHandler);
        customFilter.setAuthenticationFailureHandler(customFailureHandler);
        customFilter.setAuthenticationManager(authManagerBuilder.getOrBuild());
        http
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .exceptionHandling(handler -> handler.authenticationEntryPoint(authenticationEntryPoint))
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer ->
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .securityContext(context -> context.securityContextRepository(tokenSecurityContextRepository))
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

//                .cors(Customizer.withDefaults())
//                .cors(AbstractHttpConfigurer::disable)
//                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(corsFilter, ChannelProcessingFilter.class)
                .addFilterAt(customFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((authorize) -> authorize
//                        .anyRequest().permitAll())
                                .anyRequest().authenticated()
                )
//                .authorizeHttpRequests((requests) -> requests
//                        .requestMatchers("/test/check").authenticated()
//                        .requestMatchers("/login").permitAll()
//                        .anyRequest().authenticated())
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(logout -> logout.addLogoutHandler(customLogoutHandler));
//                .logout(LogoutConfigurer::permitAll);
//                .userDetailsService(userDetailsService);
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(List.of("https://localhost:8080"));
        configuration.setAllowedMethods(List.of(
                        HttpMethod.GET.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.DELETE.name()
                )
        );
        configuration.setAllowedHeaders(
                List.of(
                        HttpHeaders.AUTHORIZATION,
                        HttpHeaders.CONTENT_TYPE,
                        HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS,
                        HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
                        HttpHeaders.ORIGIN
                )
        );
        configuration.setAllowCredentials(true);
//        configuration.setAllowedOriginPatterns(List.of("*"));
//        configuration.setAllowedOrigins(List.of("*"));
//        configuration.setAllowedOrigins(List.of("http://localhost:8080"));
        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:8080"));
//        configuration.addAllowedOrigin("http://localhost:8080");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
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