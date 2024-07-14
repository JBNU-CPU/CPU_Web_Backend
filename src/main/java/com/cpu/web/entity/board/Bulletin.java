package com.cpu.web.entity.board;

import com.cpu.web.entity.comment.BulletinComment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Bulletin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bulletin_id", nullable = false, unique = true)
    private Long bulletinId;

    private String title;

    private String content;

    private boolean isAnonymous;

    @CreationTimestamp
    private Timestamp createDate;

    @UpdateTimestamp
    private Timestamp updateDate;

    @OneToMany(mappedBy = "bulletin", cascade = CascadeType.ALL)
    private List<BulletinComment> bulletinCommentList = new ArrayList<>();
    //@ManyToOne
    //private UserEntity userEntity;
}
