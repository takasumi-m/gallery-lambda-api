package com.photowave.repository;

import com.photowave.repository.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    @Query("SELECT t FROM Tag t WHERE t.name = :name")
    Optional<Tag> findByName(@Param("name") String name);

    @Query("SELECT t.name FROM PostTags pt INNER JOIN pt.tag t WHERE pt.postId = :postId ORDER BY pt.tagOrder ASC")
    List<String> findTagNamesByPostId(Long postId);
}