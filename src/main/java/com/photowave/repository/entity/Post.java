package com.photowave.repository.entity;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "caption", columnDefinition = "TEXT", nullable = false)
    private String caption;

    @Column(name = "location")
    private String location;

    @Column(name = "post_date", nullable = false)
    private LocalDate postDate;

    @Column(name = "post_datetime", nullable = false)
    private LocalDateTime postDatetime;

    @Column(name = "delete_password", nullable = false)
    private String deletePassword;
}