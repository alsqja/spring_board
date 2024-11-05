package com.example.spring_board.service.post;

import com.example.spring_board.dto.comment.CommentResDto;
import com.example.spring_board.dto.post.CreatePostReqDto;
import com.example.spring_board.dto.post.PostReqDto;
import com.example.spring_board.dto.post.PostResDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {
    public PostResDto createPost(CreatePostReqDto dto);

    public PostResDto findPostById(Long id);

    public Page<PostResDto> findAllPosts(int page, int offset);

    public PostResDto updatePost(Long id, PostReqDto dto);

    public void deletePost(Long id, PostReqDto dto);

    public PostResDto addPostLike(Long id);

    public List<CommentResDto> findAllCommentsByPostId(Long id);
}