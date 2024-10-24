package com.photowave.service;

import com.photowave.config.PhotowaveProperties;
import com.photowave.repository.PostImagesRepository;
import com.photowave.repository.entity.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class GetImageService {

    private static final Logger logger = LoggerFactory.getLogger(GetImageService.class);
    private final PostImagesRepository postImagesRepository;
    private final S3Service s3Service;
    private final FileService fileService;
    private final PhotowaveProperties pwProperties;

    public GetImageService(PostImagesRepository postImagesRepository, S3Service s3Service, PhotowaveProperties pwProperties, FileService fileService) {
        this.postImagesRepository = postImagesRepository;
        this.s3Service = s3Service;
        this.pwProperties = pwProperties;
        this.fileService = fileService;
    }

    public File getImagesZipFile(Long postId) {

        logger.info("getPrimaryImage start");

        // S3のパスを取得
        List<Image> ImageList = postImagesRepository.findImagesS3PathByPostId(postId);

        // ファイルが存在しない場合
        if (ImageList.isEmpty()) {
            logger.error("ファイルが存在しません。");
            return null;
        }

        // ローカルのダウンロード先ディレクトリ
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddssSSS");
        String LocalDirectory = pwProperties.getLocalDownloadPath() + "/" + formatter.format(LocalDateTime.now());
        File dir = new File(LocalDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // ファイルのダウンロード
        try {
            for (Image Image : ImageList) {
                s3Service.downloadFile(pwProperties.getBucketName(), Image.getFilePath(), Image.getUniqueFilename(), LocalDirectory);
            }
        } catch (IOException e) {
            logger.error("ファイルの取得に失敗しました。", e);
            throw new RuntimeException("ファイルの取得に失敗しました。", e);
        }

        // zipファイルの作成
        String zipFilePath = fileService.zipDirectory(LocalDirectory);

        return new File(zipFilePath);
    }
}
