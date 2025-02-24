package com.cpu.web.config;

import com.cpu.web.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final MemberRepository memberRepository;

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

                .requestMatchers("/", "/login", "/loginProc", "/signup", "/signupProc", "/auth/**", "swagger-ui/**", "/v3/api-docs/**", "/study", "/post", "/event", "/api/**").permitAll()  // 이메일 인증 경로 허용
                .requestMatchers("/study/**", "/post/**", "/comment", "/comment/**").hasAnyRole("MEMBER", "ADMIN")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
        );

        http.formLogin(login -> login.disable());

        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
        );

        http.securityContext(securityContext -> securityContext
                .securityContextRepository(new HttpSessionSecurityContextRepository()) // 세션을 통해 SecurityContext 유지
        );

        LoginFilter loginFilter = new LoginFilter(authenticationManager(authenticationConfiguration), memberRepository);
        loginFilter.setFilterProcessesUrl("/loginProc");
        http.addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class);

        http.logout((logout) -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/"));

        return http.build();
    }
}
