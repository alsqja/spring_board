package com.example.spring_board.service;

import com.example.spring_board.dto.post.CreatePostReqDto;
import com.example.spring_board.dto.post.PostReqDto;
import com.example.spring_board.dto.post.PostResDto;
import com.example.spring_board.repository.post.PostRepo;
import com.example.spring_board.service.post.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class PostServiceTest {

    @Autowired
    PostService postService;
    @Autowired
    PostRepo postRepo;

    @Test
    public void 게시글생성조회() {
        CreatePostReqDto post = new CreatePostReqDto("0000", "test", "testContents");

        Long saveId = postService.createPost(post).getId();

        PostResDto findPost = postService.findPostById(saveId);
        assertThat(post.getContents()).isEqualTo(findPost.getContents());
    }

    @Test
    public void 게시글전체조회() {
        for (int i = 0; i < 100; i++) {
            CreatePostReqDto post = new CreatePostReqDto("0000", "test" + i, "testCon" + i);
            postService.createPost(post);
        }

        Page<PostResDto> pagePosts = postService.findAllPosts(0, 10);

        assertThat(pagePosts.getTotalElements()).isEqualTo(100L);
        assertThat(pagePosts.getTotalPages()).isEqualTo(10);

        //  게시글 항목 조회 type
        List<PostResDto> postList = pagePosts.getContent();
        PostResDto firstPost = postList.get(0); // 첫 번째 게시글 가져오기

        assertThat(firstPost.getTitle()).isEqualTo("test99"); // 첫 번째 글의 제목 검증
        assertThat(firstPost.getContents()).isEqualTo("testCon99"); // 첫 번째 글의 내용 검증
    }

    @Test
    public void 게시글수정() {
        CreatePostReqDto post = new CreatePostReqDto("0000", "test", "testContents");
        PostResDto created = postService.createPost(post);

        PostReqDto updatePostReq = new PostReqDto("patchTest", "0000", "patchTestContents");
        PostResDto updatedPost = postService.updatePost(created.getId(), updatePostReq);

        assertThat(updatedPost.getTitle()).isEqualTo("patchTest");
        assertThat(updatedPost.getContents()).isEqualTo("patchTestContents");
    }

    @Test
    public void patchAndDeleteException_invalidPassword() {
        CreatePostReqDto post = new CreatePostReqDto("0000", "test", "testContents");
        PostResDto created = postService.createPost(post);

        PostReqDto updatePostReq = new PostReqDto("patchTest", "1000", "patchTestContents");
        PostReqDto deletePostReq = new PostReqDto(null, "1000", null);

        ResponseStatusException updateErr = assertThrows(ResponseStatusException.class, () -> postService.updatePost(created.getId(), updatePostReq));
        ResponseStatusException deleteErr = assertThrows(ResponseStatusException.class, () -> postService.deletePost(created.getId(), deletePostReq));

        assertThat(updateErr.getReason()).isEqualTo("invalid password");
        assertThat(deleteErr.getReason()).isEqualTo("invalid password");
    }

    @Test
    public void 게시글삭제() {
        CreatePostReqDto post = new CreatePostReqDto("0000", "test", "testContents");
        PostResDto created = postService.createPost(post);

        PostReqDto deletePostReq = new PostReqDto(null, "0000", null);

        postService.deletePost(created.getId(), deletePostReq);

        ResponseStatusException e = assertThrows(ResponseStatusException.class, () -> postService.findPostById(created.getId()));

        assertThat(e.getReason()).isEqualTo("no post having id " + created.getId());
    }

    @Test
    public void 게시글좋아요() {
        CreatePostReqDto newPost = new CreatePostReqDto("0000", "test", "testContents");
        PostResDto post = postService.createPost(newPost);


        for (int i = 0; i < 100; i++) {
            post = postService.addPostLike(post.getId());
        }

        assertThat(post.getLikes()).isEqualTo(100);
    }

    @Test
    public void 없는id_get_update_delete() {
        CreatePostReqDto newPost = new CreatePostReqDto("0000", "test", "testContents");
        PostResDto post = postService.createPost(newPost);

        PostReqDto updatePostReq = new PostReqDto("patchTest", "0000", "patchTestContents");

        ResponseStatusException getErr = assertThrows(ResponseStatusException.class, () -> postService.findPostById(post.getId() + 1));
        ResponseStatusException updateErr = assertThrows(ResponseStatusException.class, () -> postService.updatePost(post.getId() + 1, updatePostReq));
        ResponseStatusException deleteErr = assertThrows(ResponseStatusException.class, () -> postService.deletePost(post.getId() + 1, updatePostReq));

        assertThat(getErr.getReason()).isEqualTo("no post having id " + (post.getId() + 1));
        assertThat(updateErr.getReason()).isEqualTo("no post having id " + (post.getId() + 1));
        assertThat(deleteErr.getReason()).isEqualTo("no post having id " + (post.getId() + 1));
    }
}
