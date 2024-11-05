package com.example.spring_board.repository.post;

import com.example.spring_board.dto.post.CreatePostReqDto;
import com.example.spring_board.dto.post.PostResDto;
import com.example.spring_board.entity.post.Posts;
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
import java.util.Map;

@Repository
public class JdbcPostRepo implements PostRepo {

    private final JdbcTemplate jdbcTemplate;

    public JdbcPostRepo(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Long createPost(CreatePostReqDto dto) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("posts").usingGeneratedKeyColumns("id")
                .usingColumns("password", "title", "contents");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", dto.getTitle());
        parameters.put("contents", dto.getContents());
        parameters.put("password", dto.getPassword());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        return key.longValue();
    }

    @Override
    public PostResDto findPostByIdOrElseThrow(Long id) {
        return jdbcTemplate.query("SELECT * FROM posts WHERE id = ?", postRowMapper(), id).stream().findAny()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "no post having id " + id));
    }

    @Override
    public int updatePost(Posts post) {
        return jdbcTemplate.update("UPDATE posts SET title = ?, contents = ? WHERE id = ?", post.getTitle(), post.getContents(), post.getId());
    }

    @Override
    public Posts findPostByIdOrElseThrowIncludePassword(Long id) {
        return jdbcTemplate.query("SELECT * FROM posts WHERE id = ?", postsRowMapperWithPassword(), id).stream().findAny()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "no post having id " + id));
    }

    @Override
    public int deletePost(Long id) {
        return jdbcTemplate.update("DELETE FROM posts WHERE id = ?", id);
    }

    @Override
    public int addPostLike(Long id, int likes) {
        return jdbcTemplate.update("UPDATE posts SET likes = ? WHERE id = ?", likes, id);
    }

    private RowMapper<PostResDto> postRowMapper() {
        return new RowMapper<PostResDto>() {
            @Override
            public PostResDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new PostResDto(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("contents"),
                        rs.getInt("likes"),
                        rs.getInt("comments"),
                        rs.getString("created_at"),
                        rs.getString("updated_at")
                );
            }
        };
    }

    private RowMapper<Posts> postsRowMapperWithPassword() {
        return new RowMapper<Posts>() {
            @Override
            public Posts mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Posts(
                        rs.getLong("id"),
                        rs.getString("password"),
                        rs.getString("title"),
                        rs.getString("contents"),
                        rs.getInt("likes"),
                        rs.getInt("comments"),
                        rs.getString("created_at"),
                        rs.getString("updated_at")
                );
            }
        };
    }
}
