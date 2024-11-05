package com.example.spring_board.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentResDto {
    private Long id;
    private String contents;
    private int likes;
    private Long post_id;
    private String created_at;
    private String updated_at;
}
