package com.cpu.web.board.dto.response;

import com.cpu.web.board.entity.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Schema(description = "게시글 응답 데이터")
public class PostResponseDTO {

    @Schema(description = "게시글 ID", example = "1")
    private Long id;

    @Schema(description = "공지 여부", example = "true")
    private Boolean isNotice;

    @Schema(description = "게시글 제목", example = "박관소 개최 안내")
    private String  title;

    @Schema(description = "게시글 내용", example = "2024년 하반기 박관소가 개최 예정입니다.")
    private String content;

    @Schema(description = "작성일", example = "2025-01-22T11:45:33.183+00:00")
    private Timestamp createDate;
    
    @Schema(description = "작성자 닉네임", example = "씨피유미남")
    private String nickName;

    public PostResponseDTO(){}


    // entity => dto
    public PostResponseDTO(Post post) {

        this.id = post.getPostId();
        this.isNotice = post.getIsNotice();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createDate = post.getCreateDate();
        this.nickName = post.getMember().getNickName();
    }
}