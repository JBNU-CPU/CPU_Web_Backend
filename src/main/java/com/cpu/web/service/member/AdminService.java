package com.cpu.web.service.member;

import com.cpu.web.dto.member.MemberDTO;
import com.cpu.web.entity.member.Member;
import com.cpu.web.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final MemberRepository memberRepository;

    // 전체 유저 조회
    public List<MemberDTO> getAllUser() {
        return memberRepository.findAll().stream()
                .map(MemberDTO::new)
                .collect(Collectors.toList());
    }

    // 특정 유저 권한 수정
    public MemberDTO updateRole(Long id, String role) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다: " + id));
        member.setRole(role);
        Member updatedMember = memberRepository.save(member);
        return new MemberDTO(updatedMember);
    }

    public void deleteUser(Long id) {
        if (!memberRepository.existsById(id)){
            throw new IllegalArgumentException("유저가 존재하지 않습니다.: " + id);
        }
        memberRepository.deleteById(id);
    }
}
