package com.cpu.web.member.service;

import com.cpu.web.member.dto.CheckDTO;
import com.cpu.web.member.dto.MyPageEditDTO;
import com.cpu.web.member.dto.NewPasswordDTO;
import com.cpu.web.member.dto.response.MemberResponseDTO;
import com.cpu.web.member.entity.Member;
import com.cpu.web.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 내 정보 조회
    public Optional<MemberResponseDTO> getMyInformation(String username) {
        return memberRepository.findByUsername(username).map(MemberResponseDTO::new);
    }

    public MemberResponseDTO updateMember(MyPageEditDTO myPageEditDTO, String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        member.setNickName(myPageEditDTO.getNickName());
        member.setPassword(bCryptPasswordEncoder.encode(myPageEditDTO.getPassword()));
        member.setPersonName(myPageEditDTO.getPersonName());
        member.setEmail(myPageEditDTO.getEmail());
        Member updatedMember = memberRepository.save(member);
        return new MemberResponseDTO(updatedMember);
    }

    // 비밀번호 찾기를 위한 아이디 및 이메일 검증
    public boolean checkIdAndEmail(CheckDTO checkDTO) {
        Optional<Member> member = memberRepository.findByUsername(checkDTO.getUsername());
        if(member.isEmpty()){
            return false;
        }else{
            return Objects.equals(member.get().getEmail(), checkDTO.getEmail());
        }
    }

    public void setNewPassword(NewPasswordDTO newPasswordDTO) {
        Member member = memberRepository.findByUsername(newPasswordDTO.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다: " + newPasswordDTO.getUsername()));
        member.setPassword(bCryptPasswordEncoder.encode(newPasswordDTO.getPassword()));
        memberRepository.save(member);
    }

    public void withdraw(String username) {
        if(!memberRepository.existsByUsername(username)){
            throw new IllegalArgumentException("존재하지 않는 아이디입니다: " + username);
        }
        memberRepository.deleteByUsername(username);
    }
}
