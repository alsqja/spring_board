package com.example.spring_board.repository.comment;

import com.example.spring_board.dto.comment.CommentResDto;
import com.example.spring_board.dto.comment.CreateCommentReqDto;
import com.example.spring_board.entity.comment.Comments;

import java.util.List;

public interface CommentRepo {
    public Long createComment(CreateCommentReqDto dto);

    public CommentResDto findCommentByIdOrElseThrow(Long id);

    public List<CommentResDto> findAllCommentsByPostId(Long id);

    public int updateComment(Comments comments);

    public Comments findCommentByIdOrElseThrowWithPassword(Long id);

    public int deleteComment(Long id);

    public int addCommentLike(Long id, int likes);
}
