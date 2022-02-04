package com.blog.blogapi.service;

import com.blog.blogapi.payload.PostDto;
import com.blog.blogapi.payload.PostResponse;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto postDto);

    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);

    PostDto getPostById(Long id);

    PostDto updatePostById(Long id, PostDto postDto);

    void deletePost(Long id);
}
