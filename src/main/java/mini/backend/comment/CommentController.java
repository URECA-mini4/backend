package mini.backend.comment;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import mini.backend.auth.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final AuthenticationFacade authenticationFacade;


    // 댓글 작성 완료
    @PostMapping("/comments")
    public ResponseEntity<String> createComment(@RequestBody CommentDtoReq commentDtoReq) {
        Long postId = commentDtoReq.getPostId();
        String Id = authenticationFacade.getAuthentication();
        commentService.create(commentDtoReq, postId, Id);
        return ResponseEntity.ok("댓글 작성 완료");
    }

    // 댓글 수정
    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<CommentDtoRes> updateComment(@PathVariable("commentId") Long commentId, @RequestBody CommentDtoReq commentDtoReq) {
        String Id = authenticationFacade.getAuthentication();
        CommentDtoRes commentDtoRes = commentService.update(commentDtoReq, commentId, Id);
        return ResponseEntity.ok(commentDtoRes);
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable("commentId") Long commentId) {
        String Id = authenticationFacade.getAuthentication();
        commentService.deleteOne(commentId, Id);
        return ResponseEntity.ok("댓글 삭제 완료");
    }
}
