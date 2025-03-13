//package com.cpu.web.board.controller;
//
//import com.cpu.web.board.dto.response.GatheringResponseDTO;
//import com.cpu.web.scholarship.dto.response.StudyResponseDTO;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/gathering")
//@Tag(name = "Gathering", description = "소모임 API")
//public class GatheringController {
//
//    @PostMapping
//    @Operation(summary = "소모임 생성", description = "소모임 생성 API")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudyResponseDTO.class)))
//    })
//    public ResponseEntity<GatheringResponseDTO> createGathering(){
//
//    }
//}
