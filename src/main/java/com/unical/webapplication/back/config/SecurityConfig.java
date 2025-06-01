package com.unical.webapplication.back.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.web.cors.CorsConfiguration;          // import cors
import org.springframework.web.cors.CorsConfigurationSource; // import cors
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // import cors

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unical.webapplication.back.model.Utente;
import com.unical.webapplication.back.service.UtenteService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UtenteService utenteService;
    private final BCryptPasswordEncoder passwordEncoder;

    public SecurityConfig(UtenteService utenteService, BCryptPasswordEncoder passwordEncoder) {
        this.utenteService = utenteService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(); // <----- ABILITA CORS QUI

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/register").permitAll()
                        .requestMatchers("/api/users/login").permitAll()
                        .requestMatchers("/api/documents/**").authenticated()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/api/users/login") // Disabilita la pagina di login predefinita
                        .loginProcessingUrl("/api/users/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler((request, response, authentication) -> {
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            Utente user = (Utente) authentication.getPrincipal();
                            // Rimuovi la password dalla risposta per sicurezza
                            user.setPassword(null);
                            new ObjectMapper().writeValue(response.getWriter(), user);
                        })
                        .failureHandler((request, response, exception) -> {
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.getWriter().write(
                                    "{\"error\":\"Login failed\",\"message\":\"" + exception.getMessage() + "\"}");
                        }))
                .logout(logout -> logout
                        .logoutUrl("/api/users/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.getWriter().write("{\"message\":\"Logout successful\"}");
                        })
                        .deleteCookies("JSESSIONID"))
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.getWriter()
                                    .write("{\"error\":\"Unauthorized\",\"message\":\"Authentication required\"}");
                        }))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        return http.build();
    }

    // ---- QUESTO È IL NUOVO METODO PER CORS ----
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:4200");  // dominio Angular
        configuration.addAllowedMethod("*"); // tutti i metodi (GET, POST...)
        configuration.addAllowedHeader("*"); // tutte le intestazioni
        configuration.setAllowCredentials(true); // cookie / sessione

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration); // API protette
        return source;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(utenteService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

}