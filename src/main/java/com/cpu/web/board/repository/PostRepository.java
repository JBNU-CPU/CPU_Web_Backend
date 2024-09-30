package com.cpu.web.board.repository;

import com.cpu.web.board.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM post WHERE MATCH(title) AGAINST(?1 IN BOOLEAN MODE)", nativeQuery = true)
    Page<Post> fullTextSearchByTitle(Pageable pageable, String text);

}
