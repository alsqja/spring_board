package com.example.spring_board.controller.post;

import com.example.spring_board.dto.comment.CommentResDto;
import com.example.spring_board.dto.post.CreatePostReqDto;
import com.example.spring_board.dto.post.PostReqDto;
import com.example.spring_board.dto.post.PostResDto;
import com.example.spring_board.service.post.PostService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostResDto> createPost(
            @Valid @RequestBody CreatePostReqDto dto,
            BindingResult bindingResult
    ) {
        validHandler(bindingResult);

        return new ResponseEntity<>(postService.createPost(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResDto> findPostById(@PathVariable Long id) {
        return new ResponseEntity<>(postService.findPostById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> findAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int offset
    ) {
        Page<PostResDto> posts = postService.findAllPosts(page - 1, offset);

        Map<String, Object> response = new HashMap<>();
        response.put("datas", posts.getContent());
        response.put("totalCount", posts.getTotalElements());
        response.put("totalPage", posts.getTotalPages());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PostResDto> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PostReqDto dto,
            BindingResult bindingResult
    ) {
        validHandler(bindingResult);

        return new ResponseEntity<>(postService.updatePost(id, dto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long id,
            @Valid @RequestBody PostReqDto dto,
            BindingResult bindingResult
    ) {
        validHandler(bindingResult);

        postService.deletePost(id, dto);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/likes")
    public ResponseEntity<PostResDto> addPostLike(@PathVariable Long id) {
        return new ResponseEntity<>(postService.addPostLike(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentResDto>> findAllCommentByPostId(@PathVariable Long id) {
        return new ResponseEntity<>(postService.findAllCommentsByPostId(id), HttpStatus.OK);
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
