package com.cpu.web.board.service;

import com.cpu.web.board.dto.request.GatheringRequestDTO;
import com.cpu.web.board.dto.response.GatheringResponseDTO;
import com.cpu.web.board.entity.Gathering;
import com.cpu.web.board.entity.MemberGathering;
import com.cpu.web.board.entity.Post;
import com.cpu.web.board.repository.GatheringRepository;
import com.cpu.web.board.repository.MemberGatheringRepository;
import com.cpu.web.exception.CustomException;
import com.cpu.web.member.entity.Member;
import com.cpu.web.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
    public Gathering createGathering(GatheringRequestDTO gatheringRequestDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("로그인한 사용자만 접근 가능합니다.", HttpStatus.FORBIDDEN));

        Gathering gathering = gatheringRequestDTO.toGatheringEntity(member);
        return gatheringRepository.save(gathering);
    }

    // 페에지네이션된 소모임 전체 조회
    public Page<GatheringResponseDTO> getAllStudies(int page, int size) {
        Page<Gathering> gatherings = gatheringRepository.findAll(PageRequest.of(page, size, Sort.by("gatheringId").descending()));
        return gatherings.map(GatheringResponseDTO::new);
    }

    // 특정 소모임 조회
//    public Optional<GatheringResponseDTO> getGatheringById(Long id) {
//        Gathering gathering = gatheringRepository.findById(id).orElseThrow(() -> new CustomException("소모임이 존재하지 않습니다: " + id, HttpStatus.NOT_FOUND));
//
//        List<MemberGathering> memberGatherings = memberGatheringRepository.fi
//
//    }
}
