package com.photowave.controller;

import com.photowave.service.GetPrimaryImageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetPrimaryImageController {

    private final GetPrimaryImageService getPrimaryImageService;

    public GetPrimaryImageController(GetPrimaryImageService getPrimaryImageService) {
        this.getPrimaryImageService = getPrimaryImageService;
    }

    @GetMapping(value = "/api/posts/{postId}/images/primary")
    public ResponseEntity<Resource> getPrimaryImage(@PathVariable Long postId) {
        Resource image = getPrimaryImageService.getPrimaryImage(postId);

        // imageが存在しない場合は404を返す
        if (image == null || !image.exists() || !image.isReadable()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        try {
            String filename = image.getFilename();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .body(image);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}