package com.example.cargo.repository;

import com.example.cargo.model.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    @Query("SELECT p FROM BlogPost p WHERE "
            + "(COALESCE(:title, '') = '' OR LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND "
            + "(COALESCE(:publicationDate, '') = '' OR LOWER(CAST(p.publicationDate AS string)) LIKE LOWER(CONCAT('%', :publicationDate, '%'))) AND "
            + "(COALESCE(:author, '') = '' OR LOWER(p.author.userName) LIKE LOWER(CONCAT('%', :author, '%'))) AND "
            + "(COALESCE(:content, '') = '' OR LOWER(p.content) LIKE LOWER(CONCAT('%', :content, '%'))) AND "
            + "(COALESCE(:specialist, '') = '' OR LOWER(p.specialist) LIKE LOWER(CONCAT('%', :specialist, '%')))")
    List<BlogPost> searchByQuery(@Param("title") String title,
                                 @Param("publicationDate") String publicationDate,
                                 @Param("author") String author,
                                 @Param("content") String content,
                                 @Param("specialist") String specialist);
}
