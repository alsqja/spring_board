package com.example.spring_board.repository.comment;

import com.example.spring_board.dto.comment.CommentResDto;
import com.example.spring_board.dto.comment.CreateCommentReqDto;
import com.example.spring_board.entity.comment.Comments;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcCommentRepo implements CommentRepo {

    private JdbcTemplate jdbcTemplate;

    public JdbcCommentRepo(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Long createComment(CreateCommentReqDto dto) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("comments").usingGeneratedKeyColumns("id")
                .usingColumns("contents", "password", "post_id");

        Map<String, Object> parameters = new HashMap<>();

        parameters.put("contents", dto.getContents());
        parameters.put("password", dto.getPassword());
        parameters.put("post_id", dto.getPostId());

        return jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters)).longValue();
    }

    @Override
    public CommentResDto findCommentByIdOrElseThrow(Long id) {
        return jdbcTemplate.query("SELECT * FROM comments WHERE id = ?", commentRowMapper(), id).stream().findAny()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "no comment having id " + id));
    }

    @Override
    public List<CommentResDto> findAllCommentsByPostId(Long id) {
        return jdbcTemplate.query("SELECT * FROM comments WHERE post_id = ?", commentRowMapper(), id);
    }

    @Override
    public int updateComment(Comments comments) {
        return jdbcTemplate.update("UPDATE comments SET contents = ? WHERE id = ?", comments.getContents(), comments.getId());
    }

    @Override
    public Comments findCommentByIdOrElseThrowWithPassword(Long id) {
        return jdbcTemplate.query("SELECT * FROM comments WHERE id = ?", commentRowMapperWithPassword(), id).stream().findAny()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "no comment having id " + id));
    }

    @Override
    public int deleteComment(Long id) {
        return jdbcTemplate.update("DELETE FROM comments WHERE id = ?", id);
    }

    @Override
    public int addCommentLike(Long id, int likes) {
        return jdbcTemplate.update("UPDATE comments SET likes = ? WHERE id = ?", likes + 1, id);
    }

    private RowMapper<CommentResDto> commentRowMapper() {
        return new RowMapper<CommentResDto>() {
            @Override
            public CommentResDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new CommentResDto(
                        rs.getLong("id"),
                        rs.getString("contents"),
                        rs.getInt("likes"),
                        rs.getLong("post_id"),
                        rs.getString("created_at"),
                        rs.getString("updated_at")
                );
            }
        };
    }

    private RowMapper<Comments> commentRowMapperWithPassword() {
        return new RowMapper<Comments>() {
            @Override
            public Comments mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Comments(
                        rs.getLong("id"),
                        rs.getString("password"),
                        rs.getString("contents"),
                        rs.getInt("likes"),
                        rs.getLong("post_id"),
                        rs.getString("created_at"),
                        rs.getString("updated_at")
                );
            }
        };
    }
}
