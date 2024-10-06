package com.photowave.repository.entity;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class PostImagesId implements Serializable {

    private Long postId;
    private Long imageId;

    // equalsとhashCodeをLombokではなく手動で実装します
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostImagesId that = (PostImagesId) o;
        return Objects.equals(postId, that.postId) && Objects.equals(imageId, that.imageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, imageId);
    }
}