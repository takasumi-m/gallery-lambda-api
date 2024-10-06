package com.photowave.repository.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Builder
@Table(name = "post_tags")
@IdClass(PostTagsId.class)
public class PostTags {

    @Id
    @Column(name = "post_id")
    private Long postId;

    @Id
    @Column(name = "tag_id")
    private Long tagId;

    @Column(name = "tag_order", nullable = false)
    private Integer tagOrder;

    @ManyToOne
    @JoinColumn(name = "post_id", insertable = false, updatable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "tag_id", insertable = false, updatable = false)
    private Tag tag;
}