package com.example.spring_board.service;

import com.example.spring_board.dto.comment.CommentReqDto;
import com.example.spring_board.dto.comment.CommentResDto;
import com.example.spring_board.dto.comment.CreateCommentReqDto;
import com.example.spring_board.dto.post.CreatePostReqDto;
import com.example.spring_board.service.comment.CommentService;
import com.example.spring_board.service.post.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;
    @Autowired
    private PostService postService;

    private Long postId;

    @BeforeEach
    public void createPost() {
        CreatePostReqDto post = new CreatePostReqDto("0000", "test", "testContents");
        this.postId = postService.createPost(post).getId();
    }

    @Test
    public void 댓글생성조회() {
        CreateCommentReqDto createCommentReq = new CreateCommentReqDto("test", "0000", this.postId);
        Long id = commentService.createComment(createCommentReq).getId();

        Long commentId = commentService.findCommentById(id).getId();

        assertThat(id).isEqualTo(commentId);
    }

    @Test
    public void 게시글별댓글전체조회() {
        for (int i = 0; i < 100; i++) {
            CreateCommentReqDto createCommentReq = new CreateCommentReqDto("test" + i, "0000", this.postId);
            commentService.createComment(createCommentReq);
        }

        List<CommentResDto> comments = postService.findAllCommentsByPostId(this.postId);

        assertThat(comments.size()).isEqualTo(100);
        assertThat(comments.get(0).getContents()).isEqualTo("test99");
    }

    @Test
    public void 댓글수정() {
        CreateCommentReqDto createCommentReq = new CreateCommentReqDto("test", "0000", this.postId);
        CommentResDto created = commentService.createComment(createCommentReq);

        CommentReqDto updateReq = new CommentReqDto("patchTestContents", "0000");

        CommentResDto updated = commentService.updateComment(created.getId(), updateReq);

        assertThat(updated.getContents()).isEqualTo("patchTestContents");
    }

    @Test
    public void 댓글삭제() {
        CreateCommentReqDto createCommentReq = new CreateCommentReqDto("test", "0000", this.postId);
        CommentResDto created = commentService.createComment(createCommentReq);

        CommentReqDto deleteReq = new CommentReqDto(null, "0000");

        commentService.deleteComment(created.getId(), deleteReq);

        ResponseStatusException e = assertThrows(ResponseStatusException.class, () -> commentService.findCommentById(created.getId()));

        assertThat(e.getReason()).isEqualTo("no comment having id " + created.getId());
    }

    @Test
    public void 댓글좋아요() {
        CreateCommentReqDto createCommentReq = new CreateCommentReqDto("test", "0000", this.postId);
        CommentResDto created = commentService.createComment(createCommentReq);

        for (int i = 0; i < 100; i++) {
            created = commentService.addCommentLike(created.getId());
        }

        assertThat(created.getLikes()).isEqualTo(100);
    }

    @Test
    public void invalidPassword_update_delete() {
        CreateCommentReqDto createCommentReq = new CreateCommentReqDto("test", "0000", this.postId);
        CommentResDto created = commentService.createComment(createCommentReq);

        CommentReqDto deleteReq = new CommentReqDto(null, "1000");
        CommentReqDto updateReq = new CommentReqDto("patchTestContents", "1000");

        ResponseStatusException updateErr = assertThrows(ResponseStatusException.class, () -> commentService.updateComment(created.getId(), updateReq));
        ResponseStatusException deleteErr = assertThrows(ResponseStatusException.class, () -> commentService.deleteComment(created.getId(), deleteReq));

        assertThat(updateErr.getReason()).isEqualTo("invalid password");
        assertThat(deleteErr.getReason()).isEqualTo("invalid password");
    }

    @Test
    public void 없는id() {
        CreateCommentReqDto createCommentReq = new CreateCommentReqDto("test", "0000", this.postId);
        CommentResDto created = commentService.createComment(createCommentReq);

        ResponseStatusException e = assertThrows(ResponseStatusException.class, () -> commentService.findCommentById(created.getId() + 1));

        assertThat(e.getReason()).isEqualTo("no comment having id " + (created.getId() + 1));
    }
}
