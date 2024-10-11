package com.photowave.repository;

import com.photowave.repository.entity.SearchPostsEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface GetPostsRepository {
    List<SearchPostsEntity> searchPosts(String caption, String location,
                                        LocalDate postDate, LocalDateTime postDatetime,
                                        List<String> tagList);
}
