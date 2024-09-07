package com.cpu.web.service.member;

import com.cpu.web.dto.member.CheckDTO;
import com.cpu.web.dto.member.MemberDTO;
import com.cpu.web.dto.member.MyPageEditDTO;
import com.cpu.web.dto.member.NewPasswordDTO;
import com.cpu.web.entity.member.Member;
import com.cpu.web.repository.member.MemberRepository;
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
    public Optional<MemberDTO> getMyInformation(String username) {
        return memberRepository.findByUsername(username).map(MemberDTO::new);
    }

    public MemberDTO updateMember(MyPageEditDTO myPageEditDTO, String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        member.setNickName(myPageEditDTO.getNickName());
        member.setPassword(bCryptPasswordEncoder.encode(myPageEditDTO.getPassword()));
        member.setPersonName(myPageEditDTO.getPersonName());
        member.setEmail(myPageEditDTO.getEmail());
        Member updatedMember = memberRepository.save(member);
        return new MemberDTO(updatedMember);
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
