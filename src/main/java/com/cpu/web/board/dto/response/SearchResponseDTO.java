package com.cpu.web.board.dto.response;

import java.time.LocalDateTime;

public record SearchResponseDTO(Long id, Boolean isNotice, String title, LocalDateTime createTime) {
}
