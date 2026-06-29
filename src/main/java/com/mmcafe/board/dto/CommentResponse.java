package com.mmcafe.board.dto;
import java.time.LocalDateTime;
public record CommentResponse(Long id, Long boardId, String content, LocalDateTime createdAt) { }
