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

    @Autowired
    private AuthenticationFacade authenticationFacade;


    // 댓글 작성 완료
    @PostMapping("/comments")
    //@PostMapping("/posts/{postId}/comments")
    //@PostMapping("/comments/{postId}")
    public ResponseEntity<String> createComment(@RequestBody CommentDtoReq commentDtoReq) {
        //본인확인 넣어야함
        Long postId = commentDtoReq.getPostId();
        String Id = authenticationFacade.getAuthentication(); //일단 하드코딩 해놨음
        commentService.create(commentDtoReq, postId, Id);
        return ResponseEntity.ok("댓글 작  성 완료");
    }

    // 댓글 수정
    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<CommentDtoRes> updateComment(@PathVariable("commentId") Long commentId, @RequestBody CommentDtoReq commentDtoReq) {
        //본인확인 넣어야 함
        String Id = authenticationFacade.getAuthentication(); //일단 하드코딩 해놨음
        CommentDtoRes commentDtoRes = commentService.update(commentDtoReq, commentId, Id);
        return ResponseEntity.ok(commentDtoRes);

    }

    // 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable("commentId") Long commentId) {
        //본인확인넣어야함
        String Id = authenticationFacade.getAuthentication(); //일단 하드코딩 해놨음
        commentService.deleteOne(commentId, Id);
        return ResponseEntity.ok("댓글 삭제 완료");
    }
}
