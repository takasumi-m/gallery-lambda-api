package com.photowave.service.dto;

import com.photowave.repository.entity.Post;
import com.photowave.repository.entity.Tag;
import com.photowave.repository.entity.Image;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostDetails {
    private Post post;
    private List<Tag> tagList;
    private List<Image> imageList;
}