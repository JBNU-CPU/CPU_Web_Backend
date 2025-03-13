package com.cpu.web.board.dto.response;


import com.cpu.web.board.entity.Gathering;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "소모임 응답 데이터")
public class GatheringResponseDTO {

    public GatheringResponseDTO(){}

    public GatheringResponseDTO(Gathering gathering){

    }
}
