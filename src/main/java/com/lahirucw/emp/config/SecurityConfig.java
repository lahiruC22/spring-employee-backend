package com.lahirucw.emp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final String[] SWAGGER_WHITELIST = {
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/swagger-resources/**",
        "/webjars/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/.well-known/jwks.json").permitAll()
                .requestMatchers(SWAGGER_WHITELIST).permitAll()
                .requestMatchers(HttpMethod.GET, "/api/employees", "/api/employees/**")
                .hasAnyAuthority("SCOPE_employee:read", "ROLE_ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/employees")
                .hasAnyAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/employees/**")
                .hasAnyAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/employees/**")
                .hasAnyAuthority("ROLE_ADMIN")
                .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                .jwtAuthenticationConverter(
                        jwtAuthenticationConverter())));

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

}
