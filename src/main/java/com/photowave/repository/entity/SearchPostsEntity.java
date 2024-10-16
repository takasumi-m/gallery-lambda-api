package com.photowave.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchPostsEntity {
    Long postId;
    String caption;
    String location;
    LocalDate postDate;
    LocalDateTime postDatetime;
}
