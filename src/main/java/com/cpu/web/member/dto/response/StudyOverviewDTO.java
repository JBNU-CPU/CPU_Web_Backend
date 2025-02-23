package com.cpu.web.member.dto.response;

import com.cpu.web.scholarship.entity.Study;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudyOverviewDTO {
    private Long id;
    private String name;
    private String studyType;

    public StudyOverviewDTO(Study study) {
        this.id = study.getStudyId();
        this.name = study.getStudyName();
        this.studyType = study.getStudyType().name();
    }
}
