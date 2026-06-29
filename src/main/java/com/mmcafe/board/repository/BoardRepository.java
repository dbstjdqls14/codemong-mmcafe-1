package com.mmcafe.board.repository;
import com.mmcafe.board.dto.BoardResponse;
import com.mmcafe.board.dto.CommentResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public class BoardRepository {
    private final JdbcTemplate jdbc;
    private final RowMapper<BoardResponse> boardMapper = (rs, rowNum) -> new BoardResponse(rs.getLong("id"), rs.getString("title"), rs.getString("content"), rs.getTimestamp("created_at").toLocalDateTime());
    public BoardRepository(JdbcTemplate jdbc) { this.jdbc = jdbc; }
    public BoardResponse save(String title, String content) {
        LocalDateTime now = LocalDateTime.now();
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("insert into boards(title, content, created_at) values (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, title);
            ps.setString(2, content);
            ps.setTimestamp(3, Timestamp.valueOf(now));
            return ps;
        }, keyHolder);
        return new BoardResponse(keyHolder.getKey().longValue(), title, content, now);
    }
    public Optional<BoardResponse> findById(long id) {
        return jdbc.query("select id, title, content, created_at from boards where id = ?", boardMapper, id).stream().findFirst();
    }

    public List<BoardResponse> findAll(int page, int size) {
        return jdbc.query("select id, title, content, created_at from boards order by created_at desc, id desc limit ? offset ?", boardMapper, size, page * size);
    }
    public long count() {
        Long count = jdbc.queryForObject("select count(*) from boards", Long.class);
        return count == null ? 0 : count;
    }


    public boolean update(long id, String title, String content) {
        return jdbc.update("update boards set title = ?, content = ? where id = ?", title, content, id) > 0;
    }
    public boolean delete(long id) {
        return jdbc.update("delete from boards where id = ?", id) > 0;
    }


    public CommentResponse saveComment(long boardId, String content) {
        LocalDateTime now = LocalDateTime.now();
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("insert into comments(board_id, content, created_at) values (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, boardId);
            ps.setString(2, content);
            ps.setTimestamp(3, Timestamp.valueOf(now));
            return ps;
        }, keyHolder);
        return new CommentResponse(keyHolder.getKey().longValue(), boardId, content, now);
    }
    public List<CommentResponse> findComments(long boardId) {
        return jdbc.query("select id, board_id, content, created_at from comments where board_id = ? order by created_at asc, id asc",
                (rs, rowNum) -> new CommentResponse(rs.getLong("id"), rs.getLong("board_id"), rs.getString("content"), rs.getTimestamp("created_at").toLocalDateTime()), boardId);
    }
    public boolean deleteComment(long commentId) {
        return jdbc.update("delete from comments where id = ?", commentId) > 0;
    }

}
