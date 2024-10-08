package com.photowave.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.photowave.controller.request.CreatePostRequest;
import com.photowave.controller.response.CreatePostResponse;
import com.photowave.repository.entity.Image;
import com.photowave.service.CreatePostService;
import com.photowave.service.dto.PostDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CreatePostController {

    private static final Logger logger = LoggerFactory.getLogger(CreatePostController.class);

    @Autowired
    private CreatePostService createPostService;

    @PostMapping(value = "/api/post", produces = "application/json")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<CreatePostResponse> createPost(@RequestPart String requestJson,
                                                        @RequestPart List<MultipartFile> fileList) throws Exception {

        // JSONデータをパースする
        ObjectMapper objectMapper = new ObjectMapper();
        CreatePostRequest request = objectMapper.readValue(requestJson, CreatePostRequest.class);

        // 現在日時を取得
        LocalDateTime postDateTime = LocalDateTime.now();

        // アップロード済みのファイル情報
        List<Image> uploadedImageList = new ArrayList<>();
        // 投稿情報を保持
        PostDetails postDetail = new PostDetails();
        try {

            // S3へアップロード（例外時にも値を返却して欲しいため、引数としてuploadedImageList渡す）
            createPostService.uploadFile(uploadedImageList, fileList, postDateTime);

            // DBへ登録
            postDetail = createPostService.registerPost(request, uploadedImageList, postDateTime);

        } catch (Exception e) {
            logger.error("Failed to create post", e);

            // uploadFileでアップロードしたファイルを削除
            createPostService.deleteFile(uploadedImageList);

            return new ResponseEntity<>(new CreatePostResponse(null), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new CreatePostResponse(postDetail.getPost().getPostId()), HttpStatus.CREATED);
    }
}