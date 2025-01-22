package com.cpu.web.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(cors -> cors.configurationSource(corsConfigurationSource));


        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/", "/login", "/loginProc", "/signup", "/signupProc", "/auth/**", "swagger-ui/**", "/v3/api-docs/**", "/post").permitAll()  // 이메일 인증 경로 허용
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
        );

        http.formLogin(login -> login.disable());

        LoginFilter loginFilter = new LoginFilter(authenticationManager(authenticationConfiguration));
        loginFilter.setFilterProcessesUrl("/loginProc");
        http.addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class);

        http.logout((logout) -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/"));

        return http.build();
    }
}

