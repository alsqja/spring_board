package com.example.spring_board.controller.comment;

import com.example.spring_board.dto.comment.CommentReqDto;
import com.example.spring_board.dto.comment.CommentResDto;
import com.example.spring_board.dto.comment.CreateCommentReqDto;
import com.example.spring_board.service.comment.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentResDto> createComment(
            @Valid @RequestBody CreateCommentReqDto dto,
            BindingResult bindingResult
    ) {
        validHandler(bindingResult);

        return new ResponseEntity<>(commentService.createComment(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResDto> findCommentById(@PathVariable Long id) {
        return new ResponseEntity<>(commentService.findCommentById(id), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CommentResDto> updateComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentReqDto dto,
            BindingResult bindingResult
    ) {
        validHandler(bindingResult);

        return new ResponseEntity<>(commentService.updateComment(id, dto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentReqDto dto,
            BindingResult bindingResult
    ) {
        validHandler(bindingResult);

        commentService.deleteComment(id, dto);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/likes")
    public ResponseEntity<CommentResDto> addCommentLike(@PathVariable Long id) {
        return new ResponseEntity<>(commentService.addCommentLike(id), HttpStatus.OK);
    }

    private void validHandler(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> list = bindingResult.getFieldErrors();
            for (FieldError err : list) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, err.getDefaultMessage());
            }
        }
    }
}
