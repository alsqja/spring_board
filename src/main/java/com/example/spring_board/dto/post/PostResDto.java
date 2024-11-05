package com.example.spring_board.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostResDto {
    private Long id;
    private String title;
    private String contents;

    //    @Setter
    private int likes;

    private int comments;
    private String created_at;
    private String updated_at;
}
