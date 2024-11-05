package com.example.spring_board.service.comment;

import com.example.spring_board.dto.comment.CommentReqDto;
import com.example.spring_board.dto.comment.CommentResDto;
import com.example.spring_board.dto.comment.CreateCommentReqDto;
import com.example.spring_board.entity.comment.Comments;
import com.example.spring_board.repository.comment.CommentRepo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Transactional
@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepo commentRepo;

    public CommentServiceImpl(CommentRepo commentRepo) {
        this.commentRepo = commentRepo;
    }

    @Override
    public CommentResDto createComment(CreateCommentReqDto dto) {
        Long key = commentRepo.createComment(dto);

        return commentRepo.findCommentByIdOrElseThrow(key);
    }

    @Override
    public CommentResDto findCommentById(Long id) {
        return commentRepo.findCommentByIdOrElseThrow(id);
    }

    @Override
    public CommentResDto updateComment(Long id, CommentReqDto dto) {

        Comments comments = commentRepo.findCommentByIdOrElseThrowWithPassword(id);

        comments.patchByDto(dto);

        if (!comments.getPassword().equals(dto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid password");
        }

        int updatedRow = commentRepo.updateComment(comments);

        if (updatedRow > 0) {
            return commentRepo.findCommentByIdOrElseThrow(id);
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "something went wrong");
        }
    }

    @Override
    public void deleteComment(Long id, CommentReqDto dto) {
        Comments comments = commentRepo.findCommentByIdOrElseThrowWithPassword(id);

        if (!comments.getPassword().equals(dto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid password");
        }

        int deletedRow = commentRepo.deleteComment(id);

        if (deletedRow == 0) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "something went wrong");
        }
    }

    @Override
    public CommentResDto addCommentLike(Long id) {
        CommentResDto comment = commentRepo.findCommentByIdOrElseThrow(id);

        int updatedRow = commentRepo.addCommentLike(id, comment.getLikes());

        if (updatedRow > 0) {
            return commentRepo.findCommentByIdOrElseThrow(id);
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "something went wrong");
        }
    }
}
