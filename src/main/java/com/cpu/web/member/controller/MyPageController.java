package com.cpu.web.member.controller;

import com.cpu.web.member.dto.CheckDTO;
import com.cpu.web.member.dto.MemberDTO;
import com.cpu.web.member.dto.MyPageEditDTO;
import com.cpu.web.member.dto.NewPasswordDTO;
import com.cpu.web.member.service.MyPageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
@Tag(name = "Member", description = "회원 API")
public class MyPageController {

    private final MyPageService myPageService;
    
    // 내 정보 조회
    @GetMapping()
    @Operation(summary = "내 정보 조회", description = "내 정보 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저에 접근하였습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<MemberDTO> getMyInformation(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<MemberDTO> myInformationDTO = myPageService.getMyInformation(username);
        return myInformationDTO.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());

    }

    // 회원 정보 수정
    @PutMapping()
    public ResponseEntity<MemberDTO> updateMember(@RequestBody MyPageEditDTO myPageEditDTO){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        MemberDTO updatedMemberDTO = myPageService.updateMember(myPageEditDTO, username);
        return ResponseEntity.ok(updatedMemberDTO);
    }

    // 아이디 및 이메일 검증
    @GetMapping("/check")
    public ResponseEntity<?> checkIdAndEmail(@RequestBody CheckDTO checkDTO){
        Boolean isChecked = myPageService.checkIdAndEmail(checkDTO);
        return ResponseEntity.ok(isChecked);
    }
    
    // 비밀번호 찾기
    @PostMapping("/password")
    public ResponseEntity<?> setNewPassword(@RequestBody NewPasswordDTO newPasswordDTO){
        myPageService.setNewPassword(newPasswordDTO);
        return ResponseEntity.ok().build();
    }
    
    // 회원 탈퇴
    @DeleteMapping("/withdraw")
    public ResponseEntity<?> withdraw(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        myPageService.withdraw(username);
        return ResponseEntity.noContent().build();
    }
}
