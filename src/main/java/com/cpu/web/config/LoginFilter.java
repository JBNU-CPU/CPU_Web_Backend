package com.cpu.web.config;

import com.cpu.web.member.dto.LoginDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public LoginFilter(AuthenticationManager authenticationManager) {

        this.authenticationManager = authenticationManager;
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

        System.out.println("login success");
        System.out.println(authentication.getName());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 강제로 쿠키 생성
        Cookie customCookie = new Cookie("JSESSIONID", request.getSession().getId());

        // 쿠키 설정
        customCookie.setPath("/"); // 모든 경로에서 쿠키를 사용할 수 있도록 설정
        customCookie.setHttpOnly(true); // 자바스크립트에서 접근 불가
        customCookie.setSecure(true); // HTTPS에서만 전송
        customCookie.setMaxAge(60 * 60); // 쿠키 유효 기간: 1시간 (초 단위)
        customCookie.setDomain("jbnucpu.co.kr");

        // 응답에 쿠키 추가
        response.addCookie(customCookie);

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {

        System.out.println("login fail");
        
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}
