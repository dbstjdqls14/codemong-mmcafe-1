package com.mmcafe.board.controller;
import com.mmcafe.board.dto.*;
import com.mmcafe.board.service.BoardService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/boards")
public class BoardController {
    private final BoardService service;
    public BoardController(BoardService service) { this.service = service; }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BoardResponse create(@RequestBody BoardRequest request) { return service.createBoard(request); }
    @GetMapping("/{id}")
    public BoardResponse get(@PathVariable long id) { return service.getBoard(id); }

    @GetMapping
    public PageResponse<BoardResponse> list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return service.getBoards(page, size);
    }


    @PutMapping("/{id}")
    public BoardResponse update(@PathVariable long id, @RequestBody BoardRequest request) { return service.updateBoard(id, request); }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) { service.deleteBoard(id); }


    @PostMapping("/{boardId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse createComment(@PathVariable long boardId, @RequestBody CommentRequest request) { return service.createComment(boardId, request); }
    @GetMapping("/{boardId}/comments")
    public java.util.List<CommentResponse> comments(@PathVariable long boardId) { return service.getComments(boardId); }
    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable long commentId) { service.deleteComment(commentId); }

}
