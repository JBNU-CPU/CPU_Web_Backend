package com.cpu.web.board.service;

import com.cpu.web.board.dto.request.GatheringRequestDTO;
import com.cpu.web.board.dto.response.GatheringResponseDTO;
import com.cpu.web.board.entity.Gathering;
import com.cpu.web.board.entity.MemberGathering;
import com.cpu.web.board.repository.GatheringRepository;
import com.cpu.web.board.repository.MemberGatheringRepository;
import com.cpu.web.exception.CustomException;
import com.cpu.web.member.entity.Member;
import com.cpu.web.member.repository.MemberRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class GatheringService {

    private final GatheringRepository gatheringRepository;
    private final MemberRepository memberRepository;
    private final MemberGatheringRepository memberGatheringRepository;

    // 소모임 개설
    public Gathering createGathering(@Valid GatheringRequestDTO gatheringRequestDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("로그인한 사용자만 접근 가능합니다.", HttpStatus.FORBIDDEN));

        // 소모임 생성 및 저장
        Gathering gathering = gatheringRequestDTO.toGatheringEntity(member);
        Gathering savedGathering = gatheringRepository.save(gathering);

        // 매핑 테이블에 팀장 정보 추가
        MemberGathering memberGathering = new MemberGathering();
        memberGathering.setMember(member);
        memberGathering.setGathering(gathering);
        memberGathering.setIsLeader(true);

        memberGatheringRepository.save(memberGathering);

        return savedGathering;
    }

    // 페에지네이션된 소모임 전체 조회
    public Page<GatheringResponseDTO> getAllGatherings(int page, int size) {
        Page<Gathering> gatherings = gatheringRepository.findAll(PageRequest.of(page, size, Sort.by("gatheringId").descending()));

        Page<GatheringResponseDTO> result = gatherings.map(gathering -> {
            Long currentCount = memberGatheringRepository.countByGathering(gathering);
            return new GatheringResponseDTO(gathering, currentCount);
        });

        return result;
    }

    // 특정 소모임 조회
    public Optional<GatheringResponseDTO> getGatheringById(Long id) {
        Gathering gathering = gatheringRepository.findById(id).orElseThrow(() -> new CustomException("소모임이 존재하지 않습니다: " + id, HttpStatus.NOT_FOUND));
        List<MemberGathering> memberGatherings = memberGatheringRepository.findByGathering_GatheringId(id);
        Long currentCount = memberGatheringRepository.countByGathering(gathering);

        return Optional.of(new GatheringResponseDTO(gathering, memberGatherings, currentCount));
    }

    // 소모임 수정
    public GatheringResponseDTO updateGathering(Long id, @Valid GatheringRequestDTO gatheringRequestDTO) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("로그인한 사용자만 접근 가능합니다.", HttpStatus.FORBIDDEN));

        // 소모임 찾기
        Gathering gathering = gatheringRepository.findById(id)
                .orElseThrow(() -> new CustomException("소모임이 존재하지 않습니다: " + id, HttpStatus.NOT_FOUND));

        // 소모임 개설자인지 확인
        if (!gathering.getLeader().equals(member)) {
            throw new CustomException("팀장이 아니므로 수정 권한이 없습니다: " + member.getPersonName(), HttpStatus.FORBIDDEN);
        }
        gatheringRequestDTO.updateGatheringEntity(gathering);

        return new GatheringResponseDTO(gatheringRepository.save(gathering));
    }
    
    // 소모임 삭제
    public void deleteGathering(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("로그인한 사용자만 접근 가능합니다.", HttpStatus.UNAUTHORIZED));

        Gathering gathering = gatheringRepository.findById(id)
                .orElseThrow(() -> new CustomException("소모임이 존재하지 않습니다: " + id, HttpStatus.NOT_FOUND));

        // 현재 참여 인원 확인 및 관리자가 아닌 경우 검사
        Long currentCount = memberGatheringRepository.countByGathering(gathering);
        if (!isAdmin && currentCount >= 2) {
            throw new CustomException("소모임 참여 인원이 2명 이상이므로 삭제할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        // 관리자이거나 소모임 개설자인 경우 삭제 가능
        if (!(isAdmin || member.equals(gathering.getLeader()))) {
            throw new CustomException("삭제 권한이 없는 유저입니다.", HttpStatus.FORBIDDEN);
        }

        memberGatheringRepository.deleteByGathering(gathering);

        gatheringRepository.delete(gathering);
    }
}
