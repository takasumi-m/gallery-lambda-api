package com.photowave.repository;

import com.photowave.repository.entity.Image;
import com.photowave.repository.entity.PostImages;
import com.photowave.repository.entity.PostImagesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostImagesRepository extends JpaRepository<PostImages, PostImagesId> {

    @Query("SELECT i " +
            "FROM PostImages pi " +
            "JOIN Image i ON pi.imageId = i.imageId " +
            "WHERE pi.imageOrder = 0 AND pi.postId = :postId")
    Image findPrimaryImageS3PathByPostId(@Param("postId") Long postId);

    @Query("SELECT i " +
            "FROM PostImages pi " +
            "JOIN Image i ON pi.imageId = i.imageId " +
            "WHERE pi.imageOrder <> 0 AND pi.postId = :postId")
    List<Image> findImagesS3PathByPostId(@Param("postId") Long postId);

}