package com.cpu.web.controller.member;

import com.cpu.web.dto.member.CustomMember;
import com.cpu.web.dto.member.MemberDTO;
import com.cpu.web.repository.member.MemberRepository;
import com.cpu.web.service.member.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    public String admin(Authentication authentication) {
        CustomMember customMember = (CustomMember) authentication.getPrincipal();
        System.out.println("로그인 아이디 : " + customMember.getUsername());
        return "admin";
    }

    // 전체 유저 조회
    @GetMapping("/user")
    public List<MemberDTO> getAllUsers() {
        return adminService.getAllUser();
    }

    // 유저 권한 변경
    @PutMapping("/user/{id}")
    public ResponseEntity<MemberDTO> updateRole(@PathVariable Long id, @RequestParam String role) {
        if(role.equals("ROLE_ADMIN")||role.equals("ROLE_USER")){
            MemberDTO updateMemberDTO = adminService.updateRole(id, role);
            return ResponseEntity.ok(updateMemberDTO);
        }
        throw new IllegalArgumentException("유효하지 않은 권한을 부여하였습니다.");
    }

    // 유저 삭제
    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    //TODO: 스터디 신청자 조회, 변경, 삭제
}