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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class CreatePostController {

    private static final Logger logger = LoggerFactory.getLogger(CreatePostController.class);

    @Autowired
    private CreatePostService createPostService;

    @PostMapping("/api/post")
    @Transactional(rollbackFor = Exception.class)
    public CreatePostResponse createPost(@RequestPart String requestJson,
                                         @RequestPart List<MultipartFile> fileList) throws Exception {

        // JSONデータをパースする
        ObjectMapper objectMapper = new ObjectMapper();
        CreatePostRequest request = objectMapper.readValue(requestJson, CreatePostRequest.class);

        // 現在日時を取得
        LocalDateTime postDateTime = LocalDateTime.now();

        try {

            // S3へアップロード
            List<Image> uploadedImageList = createPostService.uploadFile(fileList, postDateTime);

            // DBへ登録
            PostDetails postDetail = createPostService.registerPost(request, uploadedImageList, postDateTime);

        } catch (Exception e) {
            // TODO ploadedImageListのファイルを削除
            logger.error("Failed to create post", e);
            throw e;
        }

        return null;
    }
}