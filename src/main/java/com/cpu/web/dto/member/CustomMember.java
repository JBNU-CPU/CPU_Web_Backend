package com.cpu.web.dto.member;

import com.cpu.web.entity.member.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomMember implements UserDetails {

    private final Member member;

    public CustomMember(Member member) {
        this.member = member;
    }

    // 사용자 권한 반환 (Role enum을 사용하여 권한 처리)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        // Role enum을 String 형태로 변환하여 SimpleGrantedAuthority로 반환
        authorities.add(new SimpleGrantedAuthority(member.getRole().name()));

        return authorities;
    }

    // 사용자 비밀번호 반환
    @Override
    public String getPassword() {
        return member.getPassword();
    }

    // 사용자 아이디 반환
    @Override
    public String getUsername() {
        return member.getUsername();
    }

    // 계정이 만료되지 않았는지 여부
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정이 잠겨있지 않은지 여부
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 자격 증명이 만료되지 않았는지 여부
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정이 활성화되었는지 여부 (기본적으로 활성화 상태)
    @Override
    public boolean isEnabled() {
        return true;  // 기본적으로 계정 활성화 상태
    }
}
