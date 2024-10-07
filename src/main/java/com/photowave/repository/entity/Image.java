package com.photowave.repository.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "unique_filename", nullable = false)
    private String uniqueFilename;

    @Column(name = "original_filename", nullable = false)
    private String originalFilename;

}