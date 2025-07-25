package com.cpu.web.member.entity;

import com.cpu.web.board.entity.Comment;
import com.cpu.web.board.entity.Post;
import com.cpu.web.scholarship.entity.MemberStudy;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String personName;

    @Column(nullable = false)
    private String nickName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @CreationTimestamp
    private LocalDateTime createDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Post> postList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberStudy> joinedStudyList = new ArrayList<>();

    public enum Role {
        ROLE_GUEST,
        ROLE_MEMBER,
        ROLE_ADMIN;

        public static Role from(String value) {
            try {
                return Role.valueOf(value.toUpperCase());
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid Role: " + value);
            }
        }
    }


}
