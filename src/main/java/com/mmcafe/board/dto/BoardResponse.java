package com.mmcafe.board.dto;
import java.time.LocalDateTime;
public record BoardResponse(Long id, String title, String content, LocalDateTime createdAt) { }
