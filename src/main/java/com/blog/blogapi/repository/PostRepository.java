package com.blog.blogapi.repository;

import com.blog.blogapi.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    //all the CRUD methods are in the jpa repository
}
