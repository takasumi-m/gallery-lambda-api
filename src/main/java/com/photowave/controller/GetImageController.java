package com.photowave.controller;

import com.photowave.service.GetImageService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
public class GetImageController {

    private final GetImageService getImageService;

    public GetImageController(GetImageService getImageService) {
        this.getImageService = getImageService;
    }

    @GetMapping(value = "/api/posts/{postId}/images")
    public ResponseEntity<Resource> getImagesZipFile(@PathVariable Long postId) {
        // ZIPファイルを取得
        File zipFile = getImageService.getImagesZipFile(postId);

        // ZIPファイルが存在しない場合の処理
        if (zipFile == null || !zipFile.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);  // 404 Not Found を返す
        }

        // ファイルを Resource に変換
        Resource resource = new FileSystemResource(zipFile);

        try {
            // レスポンスヘッダーを設定し、ZIPファイルを返す
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + zipFile.getName() + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}