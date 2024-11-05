package com.example.spring_board.dto.post;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CreatePostReqDto {
    @NotNull(message = "password cannot be null")
    private String password;

    @NotNull(message = "title cannot be null")
    @Size(min = 1, max = 20, message = "title.length is min 1 & max 20")
    private String title;

    @NotNull(message = "contents cannot be null")
    @Size(min = 1, max = 200, message = "contents.length is min 1 & max 20")
    private String contents;
}
