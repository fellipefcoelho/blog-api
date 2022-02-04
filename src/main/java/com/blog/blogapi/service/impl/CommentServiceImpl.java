package com.blog.blogapi.service.impl;

import com.blog.blogapi.entity.Comment;
import com.blog.blogapi.entity.Post;
import com.blog.blogapi.exception.BlogAPIException;
import com.blog.blogapi.exception.ResourceNotFoundException;
import com.blog.blogapi.payload.CommentDto;
import com.blog.blogapi.repository.CommentRepository;
import com.blog.blogapi.repository.PostRepository;
import com.blog.blogapi.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    public CommentDto createComment(Long postId, CommentDto commentDto) {
        //Transform to entity
        Comment comment = mapToEntity(commentDto);
        //Find the assigned post
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        //assign the comment to the respective post
        comment.setPost(post);
        //save to the database
        Comment newComment = commentRepository.save(comment);

        return mapToDto(newComment);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(Long postId) {
        //retrieve comment by postId
        List<Comment> comments = commentRepository.findByPostId(postId);
        //convert list of comments to comment DTO
        return comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {
        Post post = getPost(postId);

        Comment comment = getComment(commentId);

        commentBelongToPost(post, comment);

        return mapToDto(comment);
    }



    @Override
    public CommentDto updateComment(Long postId, Long commentId, CommentDto commentRequest) {
        Post post = getPost(postId);

        Comment comment = getComment(commentId);

        commentBelongToPost(post, comment);

        comment.setName(commentRequest.getName());
        comment.setBody(commentRequest.getBody());
        comment.setEmail(commentRequest.getEmail());

        commentRepository.save(comment);

        return mapToDto(comment);
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        Post post = getPost(postId);

        Comment comment = getComment(commentId);

        commentBelongToPost(post, comment);
        
        commentRepository.delete(comment);
    }

    private void commentBelongToPost(Post post, Comment comment) {
        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }
    }

    private Post getPost(Long postId) {
        Post post = postRepository
                .findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        return post;
    }

    private Comment getComment(Long commentId) {
        Comment comment = commentRepository
                .findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", commentId));
        return comment;
    }

    private CommentDto mapToDto(Comment comment){
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setName(comment.getName());
        commentDto.setEmail(comment.getEmail());
        commentDto.setBody(comment.getBody());

        return commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto){
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());

        return comment;
    }


}
