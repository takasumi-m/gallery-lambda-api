package com.photowave.repository;

import com.photowave.repository.entity.PostTags;
import com.photowave.repository.entity.PostTagsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostTagsRepository extends JpaRepository<PostTags, PostTagsId> {
}