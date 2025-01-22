package com.cpu.web.board.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "페이징 응답 데이터")
public class PageResponseDTO<T> {
    @Schema(description = "현재 페이지 번호", example = "0")
    private int currentPage;

    @Schema(description = "페이지 당 항목 수", example = "10")
    private int pageSize;

    @Schema(description = "전체 항목 수", example = "100")
    private long totalElements;

    @Schema(description = "전체 페이지 수", example = "10")
    private int totalPages;

    @Schema(description = "항목 리스트")
    private List<T> content;

}
