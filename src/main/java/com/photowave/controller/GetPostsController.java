package com.photowave.controller;

import com.photowave.controller.response.GetPostsResponse;
import com.photowave.service.GetPostsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
public class GetPostsController {

    GetPostsService getPostsService;

    public GetPostsController(GetPostsService getPostsService) {
        this.getPostsService = getPostsService;
    }

    @GetMapping(value = "/api/posts", produces = "application/json")
    public ResponseEntity<List<GetPostsResponse>> getPosts(
            @RequestParam(required = false) String caption,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) LocalDate postDate,
            @RequestParam(required = false) LocalDateTime postDatetime,
            @RequestParam(required = false) List<String> tagList) {

        // postDatetimeからpostDateを取得
        if(Objects.isNull(postDate) && Objects.nonNull(postDatetime)) {
            postDate = postDatetime.toLocalDate();
        }

        // response
        List<GetPostsResponse> response = getPostsService.getPosts(caption, location, postDate, postDatetime, tagList);

        return ResponseEntity.status(HttpStatus.OK)
                        .body(response);
    }
}
