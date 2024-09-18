package com.cpu.web.scholarship.dto;

import com.cpu.web.scholarship.entity.Study;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudyDTO {
    private Long memberId;
    private String studyName;
    private Study.StudyType studyType;
    private int maxMembers;
    private String studyDescription;

    public StudyDTO() {}//기본생성자

    public Study toStudyEntity(){
        Study study = new Study();
        study.setMemberId(this.memberId);
        study.setStudyName(this.studyName);
        study.setStudyType(this.studyType);
        study.setMaxMembers(this.maxMembers);
        study.setStudyDescription(this.studyDescription);
        return study;
    }
}
