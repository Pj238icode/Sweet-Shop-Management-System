package com.boot.backend.Sweet.Shop.Management.System.config;

import com.boot.backend.Sweet.Shop.Management.System.security.CustomUserDetailsService;
import com.boot.backend.Sweet.Shop.Management.System.security.JwtAuthFilter;
import com.boot.backend.Sweet.Shop.Management.System.security.JwtAuthEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    private final UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthEntryPoint))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth

                        // PUBLIC
                        .requestMatchers(
                                "/api/auth/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()


                        .requestMatchers(HttpMethod.GET, "/api/sweets/**")
                        .hasAnyRole("USER", "ADMIN")

                        // ADMIN ONLY
                        .requestMatchers(HttpMethod.POST, "/api/sweets")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/api/sweets/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/api/sweets/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/sweets/*/restock")
                        .hasRole("ADMIN")

                        // DASHBOARD (ADMIN ONLY)
                        .requestMatchers(HttpMethod.GET, "/api/dashboard/**")
                        .hasAnyRole("ADMIN","USER")

                        // PURCHASE
                        .requestMatchers(HttpMethod.POST, "/api/sweets/*/purchase")
                        .hasAnyRole("USER", "ADMIN")

                        .anyRequest()
                        .authenticated()
                )
                .authenticationProvider(authenticationProvider(userDetailsService))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration=new CorsConfiguration();


        corsConfiguration.setAllowedOrigins(
                java.util.List.of(
                        "https://sweet-management.netlify.app"
                )

        );

        corsConfiguration.setAllowedMethods(
                java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
        );

        corsConfiguration.setAllowedHeaders(
                java.util.List.of("Authorization", "Content-Type")
        );

        corsConfiguration.setExposedHeaders(
                java.util.List.of("Authorization")
        );


        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;




    }
}
