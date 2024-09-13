//package com.cpu.web.scholarship.dto;
//
//import com.cpu.web.scholarship.entity.Study;
//import lombok.Getter;
//import lombok.Setter;
//
//@Getter
//@Setter
//public class StudyDTO {
//    private String title;
//    private String content;
//    private boolean anonymous;
//
//    public StudyDTO() {}
//
//    public StudyDTO(Study study) {
//        this.title = study.getTitle();
//        this.content = study.getContent();
//        this.anonymous = study.getIsAnonymous();
//    }
//
//    public Study toStudyEntity() {
//        Study study = new Study();
//        study.setTitle(title);
//        study.setContent(content);
//        study.setIsAnonymous(anonymous);
//        return study;
//    }
//}