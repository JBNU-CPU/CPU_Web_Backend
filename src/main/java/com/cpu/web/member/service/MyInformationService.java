package com.cpu.web.member.service;

import com.cpu.web.member.dto.request.CheckDTO;
import com.cpu.web.member.dto.request.MyPageEditDTO;
import com.cpu.web.member.dto.request.NewPasswordDTO;
import com.cpu.web.member.dto.response.MemberResponseDTO;
import com.cpu.web.member.dto.response.StudyOverviewDTO;
import com.cpu.web.member.entity.Member;
import com.cpu.web.member.repository.MemberRepository;
import com.cpu.web.scholarship.entity.MemberStudy;
import com.cpu.web.scholarship.entity.Study;
import com.cpu.web.scholarship.repository.MemberStudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyInformationService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberStudyRepository memberStudyRepository;

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

    // 내가 참여한 스터디 목록 조회
    public List<StudyOverviewDTO> getMyJoinedStudies(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        return memberStudyRepository.findByMemberAndIsLeaderFalse(member).stream()
                .map(ms -> {
                    Study study = ms.getStudy();
                    Long currentCount = memberStudyRepository.countByStudy(study); // 여기서 스터디 인원 수 조회
                    return new StudyOverviewDTO(study, currentCount); // DTO 생성자에 넘겨줌
                })
                .collect(Collectors.toList());
    }

    // 내가 개설한 스터디 목록 조회
    public List<StudyOverviewDTO> getMyLedStudies(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        return memberStudyRepository.findByMemberAndIsLeaderTrue(member).stream()
                .map(ms -> {
                    Study study = ms.getStudy();
                    Long currentCount = memberStudyRepository.countByStudy(study); // 여기서 스터디 인원 수 조회
                    return new StudyOverviewDTO(study, currentCount); // DTO 생성자에 넘겨줌
                })
                .collect(Collectors.toList());
    }

}
