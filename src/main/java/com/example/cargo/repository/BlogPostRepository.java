package com.example.cargo.repository;

import com.example.cargo.model.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    List<BlogPost> findAllByOrderByPublishDateDesc();

    @Query("SELECT b FROM BlogPost b WHERE " +
            "(:keyword IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.content) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.author.username) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "(:publishDate IS NULL OR b.publishDate = :publishDate) " +
            "ORDER BY b.publishDate DESC")
    List<BlogPost> searchBlogPosts(
            @Param("keyword") String keyword,
            @Param("publishDate") LocalDate publishDate
    );
}
