package com.example.spring_board.dto.comment;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateCommentReqDto {
    @NotNull(message = "contents cannot be null")
    private String contents;

    @NotNull(message = "password cannot be null")
    private String password;

    @NotNull(message = "postId cannot be null")
    private Long postId;
}
