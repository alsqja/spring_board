package com.example.spring_board.dto.post;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PostReqDto {
    private String title;

    @NotNull(message = "password cannot be null")
    private String password;
    private String contents;
}
