package com.photowave.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class UniqueFilenameGenerator {

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    public static String generateUniqueFilename(String originalFilename) {
        return generateUniqueFilename(originalFilename, LocalDateTime.now());
    }

    public static String generateUniqueFilename(String originalFilename, LocalDateTime localDateTime) {

        // UUIDを生成
        String uuid = UUID.randomUUID().toString();

        // 現在のタイムスタンプを取得
        String timestamp = localDateTime.format(formatter);

        // 元のファイル名の拡張子を取得
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

        // UUIDとタイムスタンプを組み合わせて新しいファイル名を生成
        return String.format("%s_%s%s", timestamp, uuid, extension);
    }
}