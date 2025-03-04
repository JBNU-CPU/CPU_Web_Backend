package com.cpu.web.board.repository;

import com.cpu.web.board.dto.response.SearchResponseDTO;
import com.cpu.web.board.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    
    Page<Post> findAll(Pageable pageable);
    List<Post> findByTitleContaining(String title);


    @Query("SELECT new com.cpu.web.board.dto.response.SearchResponseDTO(" +
            "p.postId, p.isNotice, p.title, p.member.nickName, p.createDate) " +
            "FROM Post p " +
            "WHERE p.title LIKE %:title% ")
    List<SearchResponseDTO> findPostTitle(@Param("title") String title);
    
}
