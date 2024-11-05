package com.example.spring_board.repository.post;

import com.example.spring_board.entity.post.Posts;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

@NonNullApi
public interface PagePostRepo extends PagingAndSortingRepository<Posts, Long> {

    Page<Posts> findAll(Pageable pageable);
}
