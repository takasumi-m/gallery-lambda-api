package com.photowave.service;


import com.photowave.config.S3Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
public class S3Service {

    private final S3Config s3Config;

    private static final Logger logger = LoggerFactory.getLogger(CreatePostService.class);

    @Autowired
    public S3Service(S3Config s3Config) {
        this.s3Config = s3Config;
    }

    public void uploadFile(String bucketName, String s3Filepath , String s3filename, MultipartFile file) throws IOException {

        logger.info("uploadFile start");

        // ファイルをアップロード
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Filepath + "/" + s3filename)
                .acl(ObjectCannedACL.PRIVATE)
                .build();

        // MultipartFileからInputStreamを取得し、RequestBodyを作成
        RequestBody requestBody = RequestBody.fromInputStream(file.getInputStream(), file.getSize());

        s3Config.s3Client().putObject(putObjectRequest, requestBody);

        logger.info("uploadFile end");
    }
}