package com.cpu.web.exception.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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


        boolean isSessionExpired = false;
        boolean isAuthenticated = false;

        // 세션 ID와 세션 유효성 검사
        String sessionId = request.getRequestedSessionId();
        boolean valid = request.isRequestedSessionIdValid();
        HttpSession session = request.getSession(false);

        // 세션이 만료된 경우 or 세션이 존재하지 않는 경우 처리
        if (sessionId == null || !valid || session == null) {
            isSessionExpired = true;
        } else {
            // 세션이 존재하면, 인증된 사용자인지 확인
            isAuthenticated = session.getAttribute("SPRING_SECURITY_CONTEXT") != null;
        }

        // 디버깅용 로그 추가
        System.out.println("sessionId = " + sessionId);
        System.out.println("isRequestedSessionIdValid = " + valid);
        System.out.println("session = " + (session != null ? session.getId() : "null"));
        System.out.println("==> sessionExpired = " + isSessionExpired);
        System.out.println("==> isAuthenticated = " + isAuthenticated);

        // 세션 만료 여부를 응답 헤더에 추가
        response.setHeader("sessionExpired", String.valueOf(isSessionExpired));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        // 응답에 세션 만료 또는 로그인 필요 메시지 추가
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", isSessionExpired
                ? "세션이 만료되었습니다. 다시 로그인해주세요."
                : (isAuthenticated ? "권한이 부족합니다." : "로그인이 필요합니다."));
        errorResponse.put("status", HttpStatus.UNAUTHORIZED.value());

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
