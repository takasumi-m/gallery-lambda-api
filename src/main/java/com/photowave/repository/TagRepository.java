package com.photowave.repository;

import com.photowave.repository.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    @Query("SELECT t FROM Tag t WHERE t.name = :name")
    Optional<Tag> findByName(@Param("name") String name);
}