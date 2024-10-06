package com.photowave.service;

import com.photowave.repository.*;
import com.photowave.repository.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class CreatePostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private PostImagesRepository postImagesRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private PostTagsRepository postTagsRepository;

    @Transactional(rollbackFor = Exception.class)
    public Post registerPost(Post post, List<Image> images, List<Tag> tags) {

        // post table
        Post savedPost = postRepository.save(post);

        // image table
        List<Image> savedImages = imageRepository.saveAll(images);

        // post_images table
        for (int i = 0; i < savedImages.size(); i++) {
            postImagesRepository.save(PostImages.builder().post(savedPost).image(images.get(i)).imageOrder(i).build());
        }

        // tag table
        List<Tag> uniqueTags = new ArrayList<>(new HashSet<>(tags));
        List<Tag> savedTags = tagRepository.saveAll(uniqueTags);

        // post_tags table
        for (int i = 0; i < savedTags.size(); i++) {
            postTagsRepository.save(PostTags.builder().post(savedPost).tag(savedTags.get(i)).tagOrder(i).build());
        }

        return savedPost;
    }
}
