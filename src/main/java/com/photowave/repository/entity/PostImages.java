package com.photowave.repository.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Builder
@Table(name = "post_images")
@IdClass(PostImagesId.class)
public class PostImages {

    @Id
    @Column(name = "post_id")
    private Long postId;

    @Id
    @Column(name = "image_id")
    private Long imageId;

    @Column(name = "image_order", nullable = false)
    private Integer imageOrder;

    @ManyToOne
    @JoinColumn(name = "post_id", insertable = false, updatable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "image_id", insertable = false, updatable = false)
    private Image image;
}