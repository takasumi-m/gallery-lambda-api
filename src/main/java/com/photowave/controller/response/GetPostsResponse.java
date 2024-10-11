package com.photowave.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetPostsResponse {
    Long postId;
    String caption;
    String location;
    LocalDate postDate;
    LocalDateTime postDatetime;
    List<String> tagList;
    Integer imageOrder;
    String uniqueFilename;
    String originalFilename;
}
