package com.photowave.repository;

import com.photowave.repository.entity.PostImages;
import com.photowave.repository.entity.PostImagesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostImagesRepository extends JpaRepository<PostImages, PostImagesId> {
}