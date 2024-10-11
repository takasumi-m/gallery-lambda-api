package com.photowave.service.dto;

import com.photowave.repository.entity.Post;
import com.photowave.repository.entity.Tag;
import com.photowave.repository.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDetails {
    private Post post;
    private List<Tag> tagList;
    private List<Image> imageList;
}