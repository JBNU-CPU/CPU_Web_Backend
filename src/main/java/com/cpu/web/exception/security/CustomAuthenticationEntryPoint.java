package com.cpu.web.exception.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        // 세션이 만료되었는지 확인
        boolean isSessionExpired = request.getRequestedSessionId() != null && !request.isRequestedSessionIdValid();

        // 항상 sessionExpired 헤더를 설정
        response.setHeader("sessionExpired", String.valueOf(isSessionExpired));

        // 응답 설정
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        // 응답 메시지 설정
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", isSessionExpired
                ? "세션이 만료되었습니다. 다시 로그인해주세요."
                : "로그인이 필요합니다.");
        errorResponse.put("status", HttpStatus.UNAUTHORIZED.value());

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
