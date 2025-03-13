package com.cpu.web.board.service;

import com.cpu.web.board.dto.request.GatheringRequestDTO;
import com.cpu.web.board.dto.response.GatheringResponseDTO;
import com.cpu.web.board.entity.Gathering;
import com.cpu.web.board.repository.GatheringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GatheringService {

    private final GatheringRepository gatheringRepository;

    // 소모임 개설
    public Gathering createGathering() {

    }

    public Page<GatheringResponseDTO> getAllStudies(int page, int size) {
        Page<Gathering> gatherings = gatheringRepository.findAll(PageRequest.of(page, size, Sort.by("gatheringId").descending()));
        return gatherings.map(GatheringResponseDTO::new)
    }
}
