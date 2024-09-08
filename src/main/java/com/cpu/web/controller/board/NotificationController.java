package com.cpu.web.controller.board;

import com.cpu.web.dto.board.NotificationDTO;
import com.cpu.web.entity.board.Notification;
import com.cpu.web.service.board.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

// 공지 게시판 컨트롤러
@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
@Tag(name = "Notification", description = "공지사항 API")
public class NotificationController {

    private final NotificationService notificationService;

    // 글 작성
    @PostMapping
    @Operation(summary = "공지 글 작성", description = "공지 글 작성 API")
    @ApiResponse(responseCode = "201", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    @Parameters({
            @Parameter(name = "title", description = "제목", schema = @Schema(type = "String", minLength = 1, maxLength = 20, example = "박관소 개최 안내")),
            @Parameter(name = "content", description = "제목", schema = @Schema(type = "String", minLength = 1, maxLength = 100, example = "2024년 하반기 박관소가 개최 예정입니다."))
    })
    public ResponseEntity<NotificationDTO> createNotification(@RequestBody NotificationDTO notificationDTO) {
        Notification notification = notificationService.createNotification(notificationDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/notification/{id}")
                .buildAndExpand(notification.getNotificationId())
                .toUri();
        return ResponseEntity.created(location).body(notificationDTO);
    }

    // 페이징 처리된 공지 글 목록 조회
    @GetMapping
    @Operation(summary = "공지 글 목록 조회", description = "공지 글 목록 조회 API")
    @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    public Page<NotificationDTO> getAllNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return notificationService.getAllNotifications(page, size);
    }

    // 특정 글 조회
    @GetMapping("/{id}")
    @Operation(summary = "공지 글 상세 조회", description = "공지 글 상세 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 글에 접근하였습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<NotificationDTO> getNotificationById(@PathVariable Long id) {
        Optional<NotificationDTO> notificationDTO = notificationService.getNotificationById(id);
        return notificationDTO.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // 글 수정
    @PutMapping("/{id}")
    @Operation(summary = "공지 글 수정", description = "공지 글 수정 API")
    @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    @Parameters({
            @Parameter(name = "title", description = "제목", schema = @Schema(type = "String", minLength = 1, maxLength = 20, example = "박관소 개최 안내")),
            @Parameter(name = "content", description = "제목", schema = @Schema(type = "String", minLength = 1, maxLength = 100, example = "2024년 하반기 박관소가 개최 예정입니다."))
    })
    public ResponseEntity<NotificationDTO> updateNotification(@PathVariable Long id, @RequestBody NotificationDTO notificationDTO) {
        NotificationDTO updatedNotificationDTO = notificationService.updateNotification(id, notificationDTO);
        return ResponseEntity.ok(updatedNotificationDTO);
    }

    // 글 삭제
    @DeleteMapping("/{id}")
    @Operation(summary = "공지 글 삭제", description = "공지 글 삭제 API")
    @ApiResponse(responseCode = "204", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    public ResponseEntity<?> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

}