package com.example.spring_board.service.post;

import com.example.spring_board.dto.comment.CommentResDto;
import com.example.spring_board.dto.post.CreatePostReqDto;
import com.example.spring_board.dto.post.PostReqDto;
import com.example.spring_board.dto.post.PostResDto;
import com.example.spring_board.entity.post.Posts;
import com.example.spring_board.repository.comment.CommentRepo;
import com.example.spring_board.repository.post.PagePostRepo;
import com.example.spring_board.repository.post.PostRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepo postRepository;
    private final PagePostRepo pagePostRepo;
    private final CommentRepo commentRepo;

    public PostServiceImpl(PostRepo postRepository, PagePostRepo pagePostRepo, CommentRepo commentRepo) {
        this.postRepository = postRepository;
        this.pagePostRepo = pagePostRepo;
        this.commentRepo = commentRepo;
    }

    @Transactional
    @Override
    public PostResDto createPost(CreatePostReqDto dto) {
        Long id = postRepository.createPost(dto);

        return postRepository.findPostByIdOrElseThrow(id);
    }

    @Override
    public PostResDto findPostById(Long id) {
        return postRepository.findPostByIdOrElseThrow(id);
    }

    @Override
    public Page<PostResDto> findAllPosts(int page, int offset) {
        Pageable pageable = PageRequest.of(page, offset, Sort.by("created_at").descending());

        Page<Posts> posts = pagePostRepo.findAll(pageable);

        return posts.map(post -> new PostResDto(
                post.getId(),
                post.getTitle(),
                post.getContents(),
                post.getLikes(),
                post.getComments(),
                post.getCreated_at(),
                post.getUpdated_at()
        ));
    }

    @Transactional
    @Override
    public PostResDto updatePost(Long id, PostReqDto dto) {
        Posts post = postRepository.findPostByIdOrElseThrowIncludePassword(id);

        if (!post.getPassword().equals(dto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid password");
        }

        post.patchPostByDto(id, dto);

        int updatedRow = postRepository.updatePost(post);

        if (updatedRow > 0) {
            return postRepository.findPostByIdOrElseThrow(id);
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "something went wrong");
        }
    }

    @Transactional
    @Override
    public void deletePost(Long id, PostReqDto dto) {
        Posts post = postRepository.findPostByIdOrElseThrowIncludePassword(id);

        if (!post.getPassword().equals(dto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid password");
        }

        int deletedRow = postRepository.deletePost(id);

        if (deletedRow == 0) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "something went wrong");
        }
    }

    @Transactional
    @Override
    public PostResDto addPostLike(Long id) {
        PostResDto post = postRepository.findPostByIdOrElseThrow(id);

        int updatedRow = postRepository.addPostLike(id, post.getLikes() + 1);

        if (updatedRow > 0) {
//            post.setLikes(post.getLikes() + 1);
            return postRepository.findPostByIdOrElseThrow(id);
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "something went wrong");
        }
    }

    @Override
    public List<CommentResDto> findAllCommentsByPostId(Long id) {
        postRepository.findPostByIdOrElseThrow(id);
        
        return commentRepo.findAllCommentsByPostId(id);
    }
}
