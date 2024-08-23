package com.cpu.web.controller.board;

import com.cpu.web.dto.board.NotificationDTO;
import com.cpu.web.service.board.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    // 페이징된 전체 글 조회
    @GetMapping
    public String getAllNotifications(@RequestParam(defaultValue = "0") int page, Model model) {
        int size = 10; // 한 페이지에 10개의 게시글
        Page<NotificationDTO> notificationPage = notificationService.getAllNotifications(page, size);
        model.addAttribute("notifications", notificationPage.getContent());
        return "notifications"; // notifications.html 템플릿 반환
    }

    // 특정 글 조회
    @GetMapping("/{id}")
    public String getNotificationById(@PathVariable Long id, Model model) {
        NotificationDTO notificationDTO = notificationService.getNotificationById(id).orElse(null);
        model.addAttribute("notification", notificationDTO);
        return "notificationDetail"; // notificationDetail.html 템플릿 반환
    }

    // 글 저장
    @PostMapping
    public String createNotification(@ModelAttribute NotificationDTO notificationDTO) {
        notificationService.createNotification(notificationDTO);
        return "redirect:/notification";
    }

    // 글 수정
    @PostMapping("/{id}")
    public String updateNotification(@PathVariable Long id, @ModelAttribute NotificationDTO notificationDTO) {
        notificationService.updateNotification(id, notificationDTO);
        return "redirect:/notification/" + id;
    }

    // 글 삭제
    @PostMapping("/{id}/delete")
    public String deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return "redirect:/notification";
    }

    // 게시글 등록 페이지
    @GetMapping("/new")
    public String newNotification() {
        return "newNotification"; // newNotification.html 템플릿 반환
    }

    // 게시글 수정 페이지
    @GetMapping("/{id}/edit")
    public String editNotification(@PathVariable Long id, Model model) {
        NotificationDTO notificationDTO = notificationService.getNotificationById(id).orElse(null);
        model.addAttribute("notification", notificationDTO);
        return "editNotification"; // editNotification.html 템플릿 반환
    }
}
