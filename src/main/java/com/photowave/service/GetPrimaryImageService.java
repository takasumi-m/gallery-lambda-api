package com.photowave.service;

import com.photowave.config.PhotowaveProperties;
import com.photowave.repository.PostImagesRepository;
import com.photowave.repository.entity.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GetPrimaryImageService {

    private static final Logger logger = LoggerFactory.getLogger(GetPrimaryImageService.class);
    private final PostImagesRepository postImagesRepository;
    private final S3Service s3Service;
    private final PhotowaveProperties pwProperties;

    public GetPrimaryImageService(PostImagesRepository postImagesRepository, S3Service s3Service, PhotowaveProperties pwProperties) {
        this.postImagesRepository = postImagesRepository;
        this.s3Service = s3Service;
        this.pwProperties = pwProperties;
    }

    public Resource getPrimaryImage(Long postId) {

        logger.info("getPrimaryImage start");

        // S3のパスを取得
        Image Image = postImagesRepository.findPrimaryImageS3PathByPostId(postId);

        Resource image = null;

        try {
            image = s3Service.getFile(pwProperties.getBucketName(), Image.getFilePath(), Image.getUniqueFilename());
        } catch (IOException e) {
            logger.error("ファイルの取得に失敗しました。", e);
            throw new RuntimeException("ファイルの取得に失敗しました。", e);
        }

        logger.info("getPrimaryImage end");

        return image;
    }
}
