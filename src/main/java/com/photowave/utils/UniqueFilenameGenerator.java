package com.photowave.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class UniqueFilenameGenerator {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    public static String generateUniqueFilename(String originalFilename) {

        // UUIDを生成
        String uuid = UUID.randomUUID().toString();

        // 現在のタイムスタンプを取得
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String timestamp = LocalDateTime.now().format(formatter);

        // 元のファイル名の拡張子を取得
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

        // UUIDとタイムスタンプを組み合わせて新しいファイル名を生成
        return String.format("%s_%s%s", uuid, timestamp, extension);
    }
}