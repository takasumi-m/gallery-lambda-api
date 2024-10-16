package com.photowave.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    public String zipDirectory(String directory) {
        logger.info("zipDirectory start");
        logger.info("directory:{}", directory);

        File sourceDir = new File(directory);

        // ディレクトリが存在しないかディレクトリでない場合
        if (!sourceDir.isDirectory()) {
            throw new IllegalArgumentException("指定されたパスはディレクトリではありません: " + directory);
        }

        // 出力するZIPファイルのパス（ディレクトリ名 + .zip）
        String zipFilePath = sourceDir.getParent() + "/" + sourceDir.getName() + ".zip";

        // ZIPファイルに圧縮
        try {
            zipDirectory(sourceDir, zipFilePath);
        } catch (IOException e) {
            logger.error("ZIPファイルの作成に失敗しました: " + zipFilePath, e);
            throw new RuntimeException("ZIPファイルの作成に失敗しました: " + zipFilePath);
        }

        logger.info("zipFilePath:{}", zipFilePath);
        logger.info("zipDirectory end");

        // 圧縮が成功した場合、ZIPファイルのパスを返す
        return zipFilePath;
    }

    // ディレクトリをZIP化するメソッド
    private void zipDirectory(File folder, String zipFilePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            zipFile(folder, folder.getName(), zos);
        }
    }

    // ファイルまたはディレクトリをZIPに追加するメソッド
    private void zipFile(File fileToZip, String fileName, ZipOutputStream zos) throws IOException {
        if (fileToZip.isHidden()) {
            return; // 隠しファイルはスキップ
        }

        if (fileToZip.isDirectory()) {
            if (!fileName.endsWith("/")) {
                fileName += "/";
            }
            zos.putNextEntry(new ZipEntry(fileName));
            zos.closeEntry();
            File[] children = fileToZip.listFiles();
            if (children != null) {
                for (File childFile : children) {
                    zipFile(childFile, fileName + childFile.getName(), zos);
                }
            }
            return;
        }

        try (FileInputStream fis = new FileInputStream(fileToZip)) {
            ZipEntry zipEntry = new ZipEntry(fileName);
            zos.putNextEntry(zipEntry);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zos.write(bytes, 0, length);
            }
        }
    }
}