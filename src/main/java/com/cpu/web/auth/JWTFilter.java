package com.cpu.web.auth;

import com.cpu.web.member.dto.response.CustomMemberDetails;
import com.cpu.web.member.entity.Member;
import com.cpu.web.member.entity.Member.Role;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 헤더에서 Authorization키에 담긴 토큰을 꺼냄
        String accessToken = request.getHeader("Authorization");

        // 토큰이 없거나 잘못된 형식이면 다음 필터로 넘김
        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 접두어 제거
        accessToken = accessToken.substring(7);

        // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader("Access-Token-Expired", "true");
            return;
        }

        // 토큰이 access인지 확인
        String category = jwtUtil.getCategory(accessToken);

        if (!category.equals("access")) {

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader("Invalid-Access-Token", "true");
            return;
        }

        // username, role 값을 획득
        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);

        Member member = new Member();
        member.setUsername(username);
        member.setRole(Role.from(role));
        CustomMemberDetails customMemberDetails = new CustomMemberDetails(member);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customMemberDetails, null, customMemberDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
