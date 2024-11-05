package com.example.spring_board.entity.post;

import com.example.spring_board.dto.post.PostReqDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Posts {
    private Long id;
    private String password;
    private String title;
    private String contents;
    private int likes;
    private int comments;
    private String created_at;
    private String updated_at;

    public void patchPostByDto(Long id, PostReqDto dto) {
        this.id = id;
        if (dto.getContents() != null) {
            this.contents = dto.getContents();
        }
        if (dto.getTitle() != null) {
            this.title = dto.getTitle();
        }
    }
}
