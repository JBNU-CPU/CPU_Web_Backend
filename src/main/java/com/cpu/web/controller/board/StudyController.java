package com.cpu.web.controller.board;

import com.cpu.web.dto.board.StudyDTO;
import com.cpu.web.service.board.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/study")
public class StudyController {

    private final StudyService studyService;

    // 페이징된 전체 글 조회
    @GetMapping
    public String getAllStudies(@RequestParam(defaultValue = "0") int page, Model model) {
        int size = 10; // 한 페이지에 10개의 게시글
        Page<StudyDTO> studyPage = studyService.getAllStudies(page, size);
        model.addAttribute("studies", studyPage.getContent());
        return "studies"; // studies.html 템플릿 반환
    }

    // 특정 글 조회
    @GetMapping("/{id}")
    public String getStudyById(@PathVariable Long id, Model model) {
        StudyDTO studyDTO = studyService.getStudyById(id);
        model.addAttribute("study", studyDTO);
        return "studyDetail"; // studyDetail.html 템플릿 반환
    }

    // 글 저장
    @PostMapping
    public String createStudy(@ModelAttribute StudyDTO studyDTO) {
        studyService.createStudy(studyDTO);
        return "redirect:/study";
    }

    // 글 수정
    @PostMapping("/{id}")
    public String updateStudy(@PathVariable Long id, @ModelAttribute StudyDTO studyDTO) {
        studyService.updateStudy(id, studyDTO);
        return "redirect:/study/" + id;
    }

    // 글 삭제
    @PostMapping("/{id}/delete")
    public String deleteStudy(@PathVariable Long id) {
        studyService.deleteStudy(id);
        return "redirect:/study";
    }

    // 게시글 등록 페이지
    @GetMapping("/new")
    public String newStudy() {
        return "newStudy"; // newStudy.html 템플릿 반환
    }

    // 게시글 수정 페이지
    @GetMapping("/{id}/edit")
    public String editStudy(@PathVariable Long id, Model model) {
        StudyDTO studyDTO = studyService.getStudyById(id);
        model.addAttribute("study", studyDTO);
        return "editStudy"; // editStudy.html 템플릿 반환
    }
}
