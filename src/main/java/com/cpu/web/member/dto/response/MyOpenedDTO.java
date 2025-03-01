package com.cpu.web.member.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyOpenedDTO {
    private Long id;
    private String name;
    private String studyType;
    private String isAcceptted;
}
