package com.photowave.service;

import com.photowave.repository.*;
import com.photowave.repository.entity.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CreatePostServiceTest {

    @Mock
    private PostRepository postRepository;
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private PostImagesRepository postImagesRepository;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private PostTagsRepository postTagsRepository;

    @InjectMocks
    private CreatePostService createPostService;

    @Test
    public void registerPost_success() {
        // Given
        Post post = Post.builder()
                .status("active")
                .caption("Test Caption")
                .location("Test Location")
                .uploadDate(LocalDate.now())
                .uploadDatetime(LocalDateTime.now())
                .deletePassword("password")
                .build();

        Image image1 = Image.builder().s3Url("http://example.com/image1").build();
        Image image2 = Image.builder().s3Url("http://example.com/image2").build();
        List<Image> images = Arrays.asList(image1, image2);

        Tag tag1 = Tag.builder().name("tag1").build();
        Tag tag2 = Tag.builder().name("tag2").build();
        List<Tag> tags = Arrays.asList(tag1, tag2);

        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(imageRepository.saveAll(anyList())).thenReturn(images);
        when(tagRepository.saveAll(anyList())).thenReturn(tags);

        // When
        Post savedPost = createPostService.registerPost(post, images, tags);

        // Then
        // Verify post saved
        verify(postRepository, times(1)).save(any(Post.class));
        assertThat(savedPost).isEqualTo(post);

        // Capture and verify images saved
        ArgumentCaptor<List<Image>> imageCaptor = ArgumentCaptor.forClass(List.class);
        verify(imageRepository, times(1)).saveAll(imageCaptor.capture());
        List<Image> savedImages = imageCaptor.getValue();
        assertThat(savedImages).containsExactlyInAnyOrderElementsOf(images);

        // Verify post_images saved
        verify(postImagesRepository, times(images.size())).save(any(PostImages.class));

        // Capture and verify tags saved
        ArgumentCaptor<List<Tag>> tagCaptor = ArgumentCaptor.forClass(List.class);
        verify(tagRepository, times(1)).saveAll(tagCaptor.capture());
        List<Tag> savedTags = tagCaptor.getValue();
        assertThat(savedTags).containsExactlyInAnyOrderElementsOf(tags);

        // Verify post_tags saved
        verify(postTagsRepository, times(tags.size())).save(any(PostTags.class));
    }
}