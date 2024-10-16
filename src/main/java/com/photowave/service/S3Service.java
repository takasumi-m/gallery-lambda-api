package com.photowave.service;


import com.photowave.config.S3Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class S3Service {

    private final S3Config s3Config;

    private static final Logger logger = LoggerFactory.getLogger(CreatePostService.class);

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

    public Resource getFile(String bucketName, String s3Filepath , String s3filename) throws IOException {

            logger.info("getFile start");
            logger.info("s3Filepath:{} s3filename:{}", s3Filepath, s3filename);

            // S3からファイルを取得するリクエストの作成
            GetObjectRequest objectRequest = GetObjectRequest.builder()
                    .key(s3Filepath + "/" + s3filename)
                    .bucket(bucketName)
                    .build();

            // S3からファイルを取得
            ResponseInputStream<GetObjectResponse> response = s3Config.s3Client().getObject(objectRequest);

            // ResponseInputStream を Spring の Resource 型に変換
            Resource resource = new InputStreamResource(response);

            logger.info("getFile end");

            return resource;
    }

    public void downloadFile(String bucketName, String s3Filepath , String s3filename, String localPath) throws IOException {

        logger.info("downloadFile start");
        logger.info("s3Filepath:{} s3filename:{}", s3Filepath, s3filename);

        Resource resource = getFile(bucketName, s3Filepath, s3filename);

        // ローカルファイルに保存するパスを指定
        File localFile = new File(localPath + "/" + s3filename);

        // ローカルファイルに書き込む
        try (InputStream inputStream = resource.getInputStream();
             FileOutputStream fos = new FileOutputStream(localFile)) {

            byte[] readBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(readBuffer)) != -1) {
                fos.write(readBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            logger.error("ファイルのダウンロードに失敗しました: " + e.getMessage());
            throw e;
        }
        logger.info("downloadFile end");
    }

    public void deleteFile(String bucketName, String s3Filepath , String s3filename) {
        logger.info("deleteFile start");
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Filepath + "/" + s3filename)
                .build();
        s3Config.s3Client().deleteObject(deleteObjectRequest);
        logger.info("deleteFile end");
    }
}