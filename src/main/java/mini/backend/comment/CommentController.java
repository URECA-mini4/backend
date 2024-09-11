package mini.backend.comment;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성 완료
    @PostMapping("/comments")
    public String createComment(HttpServletRequest request) {
        Long postId = Long.parseLong(request.getParameter("postId"));
        String content = request.getParameter("content");

        Long userId = 6L; //일단 하드코딩 해놨음
        CommentDtoReq commentDtoReq = new CommentDtoReq(content);
        commentService.create(commentDtoReq, postId, userId);
        return "redirect:/api/v1/posts/" + postId;  // 게시물 상세 페이지로 리다이렉트
    }
}
