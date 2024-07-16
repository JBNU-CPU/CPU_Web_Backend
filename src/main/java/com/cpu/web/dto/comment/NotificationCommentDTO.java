package com.cpu.web.dto.comment;

import com.cpu.web.entity.board.Bulletin;
import com.cpu.web.entity.comment.BulletinComment;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class NotificationCommentDTO {

    private int id;
    private int studentNumber;
    private String contents;
    private LocalDateTime postDate;
    private int notificationId;

    @Getter
    @Setter
    public static class BulletinCommentDTO {

        private Long bulletinId;
        private String content;

        public BulletinComment toBulletinCommentEntity(Bulletin bulletin){
            BulletinComment bulletinComment = new BulletinComment();

            bulletinComment.setContents(content);
            bulletinComment.setBulletin(bulletin);

            return bulletinComment;
        }
    }
}
