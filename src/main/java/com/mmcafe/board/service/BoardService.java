package com.mmcafe.board.service;
    import com.mmcafe.board.dto.*;
    import com.mmcafe.board.repository.BoardRepository;
    import com.mmcafe.common.exception.ResourceNotFoundException;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;
    import java.util.List;
@Service
    public class BoardService {
        private final BoardRepository repository;
        public BoardService(BoardRepository repository) { this.repository = repository; }
        @Transactional
        public BoardResponse createBoard(BoardRequest request) { return repository.save(request.title(), request.content()); }
        public BoardResponse getBoard(long id) { return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Board not found: " + id)); }

        public PageResponse<BoardResponse> getBoards(int page, int size) {
            int normalizedPage = Math.max(page, 0);
            int normalizedSize = Math.max(size, 1);
            long total = repository.count();
            int totalPages = (int) Math.ceil((double) total / normalizedSize);
            return new PageResponse<>(repository.findAll(normalizedPage, normalizedSize), normalizedPage, normalizedSize, total, totalPages);
        }


        @Transactional
        public BoardResponse updateBoard(long id, BoardRequest request) {
            if (!repository.update(id, request.title(), request.content())) throw new ResourceNotFoundException("Board not found: " + id);
            return getBoard(id);
        }
        @Transactional
        public void deleteBoard(long id) {
            if (!repository.delete(id)) throw new ResourceNotFoundException("Board not found: " + id);
        }


        @Transactional
        public CommentResponse createComment(long boardId, CommentRequest request) {
            getBoard(boardId);
            return repository.saveComment(boardId, request.content());
        }
        public List<CommentResponse> getComments(long boardId) {
            getBoard(boardId);
            return repository.findComments(boardId);
        }
        @Transactional
        public void deleteComment(long commentId) {
            if (!repository.deleteComment(commentId)) throw new ResourceNotFoundException("Comment not found: " + commentId);
        }

    }
