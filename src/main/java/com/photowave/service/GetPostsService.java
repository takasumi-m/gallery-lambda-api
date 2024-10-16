package com.photowave.service;

import com.photowave.config.PhotowaveProperties;
import com.photowave.controller.response.GetPostsResponse;
import com.photowave.repository.*;
import com.photowave.repository.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class GetPostsService {

    private static final Logger logger = LoggerFactory.getLogger(GetPostsService.class);

    @Autowired
    private GetPostsRepository getPostsRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private final PhotowaveProperties pwProperties;

    public GetPostsService(PhotowaveProperties pwProperties) {
        this.pwProperties = pwProperties;
    }

    public List<GetPostsResponse> getPosts(String caption, String location, LocalDate postDate,
                                                LocalDateTime postDatetime, List<String> tagList) {

        logger.info("getPosts start");
        logger.info("caption:{} location:{} postDate:{} postDatetime:{} tagList:{}", caption, location, postDate, postDatetime, tagList);

        List<GetPostsResponse> responseList = new ArrayList<>();

        List<SearchPostsEntity> postList = getPostsRepository.searchPosts(caption, location, postDate, postDatetime, tagList);

        for (SearchPostsEntity post : postList) {
            GetPostsResponse response = new GetPostsResponse();
            response.setPostId(post.getPostId());
            response.setCaption(post.getCaption());
            response.setLocation(post.getLocation());
            response.setPostDate(post.getPostDate());
            response.setPostDatetime(post.getPostDatetime());

            // tagの取得
            response.setTagList(tagRepository.findTagNamesByPostId(post.getPostId()));

            responseList.add(response);
        }

        logger.info("getPosts end");

        return responseList;
    }
}
