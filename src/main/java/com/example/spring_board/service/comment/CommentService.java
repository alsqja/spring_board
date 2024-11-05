package com.example.spring_board.service.comment;

import com.example.spring_board.dto.comment.CommentReqDto;
import com.example.spring_board.dto.comment.CommentResDto;
import com.example.spring_board.dto.comment.CreateCommentReqDto;

public interface CommentService {
    public CommentResDto createComment(CreateCommentReqDto dto);

    public CommentResDto findCommentById(Long id);

    public CommentResDto updateComment(Long id, CommentReqDto dto);

    public void deleteComment(Long id, CommentReqDto dto);

    public CommentResDto addCommentLike(Long id);
}
