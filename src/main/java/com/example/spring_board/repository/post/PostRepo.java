package com.example.spring_board.repository.post;

import com.example.spring_board.dto.post.CreatePostReqDto;
import com.example.spring_board.dto.post.PostResDto;
import com.example.spring_board.entity.post.Posts;

public interface PostRepo {
    public Long createPost(CreatePostReqDto dto);

    public PostResDto findPostByIdOrElseThrow(Long id);

    public int updatePost(Posts post);

    public Posts findPostByIdOrElseThrowIncludePassword(Long id);

    public int deletePost(Long id);

    public int addPostLike(Long id, int likes);
}
