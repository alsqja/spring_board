package com.example.spring_board.entity.comment;

import com.example.spring_board.dto.comment.CommentReqDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Comments {
    private Long id;
    private String password;
    private String contents;
    private int likes;
    private Long post_id;
    private String created_at;
    private String updated_at;

    public void patchByDto(CommentReqDto dto) {
        if (dto.getContents() != null) {
            this.contents = dto.getContents();
        }
    }
}
