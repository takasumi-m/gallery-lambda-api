package com.photowave.service;

import com.photowave.config.PhotowaveProperties;
import com.photowave.controller.request.CreatePostRequest;
import com.photowave.repository.*;
import com.photowave.repository.entity.*;
import com.photowave.service.dto.PostDetails;
import com.photowave.utils.UniqueFilenameGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CreatePostService {

    private static final Logger logger = LoggerFactory.getLogger(CreatePostService.class);

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
    @Autowired
    private S3Service s3Service;

    @Autowired
    private final PhotowaveProperties pwProperties;

    public CreatePostService(PhotowaveProperties pwProperties) {
        this.pwProperties = pwProperties;
    }

    public List<Image> uploadFile(List<MultipartFile> fileList, LocalDateTime uploadDateTime) throws IOException {
        logger.info("uploadFile start");

        // yyyyMMdd形式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        List<Image> imageList = new ArrayList<>();
        for (MultipartFile file : fileList) {
            Image image = Image.builder()
                    .filePath(uploadDateTime.format(formatter))
                    .uniqueFilename(UniqueFilenameGenerator.generateUniqueFilename(file.getOriginalFilename(), uploadDateTime))
                    .originalFilename(file.getOriginalFilename())
                    .build();
            imageList.add(image);

            // S3 upload
            s3Service.uploadFile(pwProperties.getBucketName(), image.getFilePath(), image.getUniqueFilename(), file);
        }

        logger.info("uploadFile end");

        return imageList;
    }

    public PostDetails registerPost(CreatePostRequest request, List<Image> imageList, LocalDateTime uploadDateTime) {

        logger.info("registerPost start");

        PostDetails postDetails = new PostDetails();

        // post table
        Post post = convertRequestToPost(request, uploadDateTime);
        Post savedPost = postRepository.save(post);
        postDetails.setPost(savedPost);

        // image table
        List<Image> savedImageList = imageRepository.saveAll(imageList);
        postDetails.setImageList(savedImageList);

        // post_images table
        List<PostImages> postImagesList = new ArrayList<>();
        for (int i = 0; i < savedImageList.size(); i++) {
            PostImages postImages = PostImages.builder()
                    .postId(savedPost.getPostId())
                    .imageId(savedImageList.get(i).getImageId())
                    .imageOrder(i)
                    .post(savedPost)
                    .image(savedImageList.get(i))
                    .build();
            postImagesList.add(postImages);
        }
        postImagesRepository.saveAll(postImagesList);

        // tag table
        List<Tag> tagList = convertRequestToTagList(request);
        // 重複タグを除外して登録
        List<Tag> uniqueTagList = new ArrayList<>(new HashSet<>(tagList));
        // nameが登録されている場合はそのレコードを取得、登録されていない場合は新規登録\
        List<Tag> savedTagList = uniqueTagList.stream()
                .map(tag -> tagRepository.findByName(tag.getName()).orElseGet(() -> tagRepository.save(tag)))
                .toList();
        postDetails.setTagList(savedTagList);

        // post_tags table
        List<PostTags> postTagsList = new ArrayList<>();
        for (int i = 0; i < savedTagList.size(); i++) {
            PostTags postTags = PostTags.builder()
                    .postId(savedPost.getPostId())
                    .tagId(savedTagList.get(i).getTagId())
                    .tagOrder(i)
                    .post(savedPost)
                    .tag(savedTagList.get(i))
                    .build();
            postTagsList.add(postTags);
        }
        postTagsRepository.saveAll(postTagsList);

        logger.info("registered post: post_id={} image_id={}, tag_id={}",
                savedPost.getPostId(),
                savedImageList.stream().map(Image::getImageId).map(String::valueOf).collect(Collectors.joining(",")),
                savedTagList.stream().map(Tag::getTagId).map(String::valueOf).collect(Collectors.joining(","))
        );

        logger.info("registerPost end");

        return postDetails;
    }

    private Post convertRequestToPost(CreatePostRequest request, LocalDateTime uploadDateTime) {
        return Post.builder()
                .status("enabled")
                .caption(request.getCaption())
                .location(request.getLocation())
                .uploadDate(uploadDateTime.toLocalDate())
                .uploadDatetime(uploadDateTime)
                .deletePassword(request.getDeletePassword())
                .build();
    }

    private List<Tag> convertRequestToTagList(CreatePostRequest request) {
        List<Tag> tagList = new ArrayList<>();
        for (String tagName : request.getTagList()) {
            Tag tag = Tag.builder().name(tagName).build();
            tagList.add(tag);
        }
        return tagList;
    }
}
