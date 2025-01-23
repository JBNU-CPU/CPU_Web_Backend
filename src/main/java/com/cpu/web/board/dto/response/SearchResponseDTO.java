package com.cpu.web.board.dto.response;

import java.sql.Timestamp;

public record SearchResponseDTO(Long id, Boolean isNotice, String title, Timestamp createTime) {
}
