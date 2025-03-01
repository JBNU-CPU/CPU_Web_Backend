package com.cpu.web.exception.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());

        // 현재 로그인한 사용자의 권한 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String message = "권한이 없습니다."; // 기본 메시지

        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_GUEST"))) {
                message = "CPU 부원만 접근 가능한 요청입니다.";
            } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MEMBER"))) {
                message = "관리자 권한이 필요합니다.";
            }
        }

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", message);
        errorResponse.put("status", HttpStatus.FORBIDDEN.value());

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
