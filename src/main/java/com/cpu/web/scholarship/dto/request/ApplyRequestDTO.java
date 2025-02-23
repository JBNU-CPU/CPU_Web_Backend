package com.cpu.web.scholarship.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplyRequestDTO {
    private Long studyId;
    private Long memberId;
}
