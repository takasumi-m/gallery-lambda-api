package com.photowave.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostRequest {
    private String caption;
    private String location;
    private String deletePassword;
    private List<String> tagList;
}