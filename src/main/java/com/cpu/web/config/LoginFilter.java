package com.cpu.web.config;

import com.cpu.web.member.dto.LoginDTO;
import com.cpu.web.member.entity.Member;
import com.cpu.web.member.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final MemberRepository memberRepository;

    public LoginFilter(AuthenticationManager authenticationManager, MemberRepository memberRepository) {

        this.authenticationManager = authenticationManager;
        this.memberRepository = memberRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        System.out.println(request);
        System.out.println("adsfasdfadsfasdfdsfsdfasdf ------");
        
        LoginDTO loginDTO = new LoginDTO();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            loginDTO = objectMapper.readValue(messageBody, LoginDTO.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(loginDTO.getUsername());

        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

        // 로그인 성공 메시지 로그에 출력
        System.out.println("login success");
        System.out.println(authentication.getName());

        // SecurityContext 설정
        // 새 SecurityContext를 생성하고 인증 정보를 설정
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        // HttpSessionSecurityContextRepository를 통해 보안 컨텍스트를 세션에 저장한다.
        HttpSessionSecurityContextRepository secRepo = new HttpSessionSecurityContextRepository();
        secRepo.saveContext(context, request, response);

        String username = authentication.getName();
        Optional<Member> member = memberRepository.findByUsername(username);
        if (member.isEmpty()) {
            throw new RuntimeException("Member not found for username: " + username);
        }

        Long userId = member.get().getMemberId();
        Member.Role role = member.get().getRole();

        // 클라이언트에게 JSON 응답 준비
        // Content-Type을 JSON으로 설정하고 상태 코드를 200(OK)로 설정한다.
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);

        try {
            // JSON 응답 작성
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(Map.of(
                    "message", "Login successful",
                    "userId", userId,
                    "role", role
            ));

            response.getWriter().write(jsonResponse);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to write response", e);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {

        System.out.println("login fail");
        
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}
