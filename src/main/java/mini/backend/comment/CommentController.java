package mini.backend.comment;

import mini.backend.post.PostDtoReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    // 댓글 작성 완료
    @PostMapping("/comments")
    public String createComment(@RequestParam Long postId, Long userId, String content) {
        //Long userId = (Long) session.getAttribute("userId"); -> 세션에서 가져오는 경우
        //Long userId = Long.parseLong(principal.getName()); -> spring security 사용하는 경우
        userId = 6L; //일단 하드코딩 해놨음
        CommentDtoReq commentDtoReq = new CommentDtoReq(content);
        commentService.create(commentDtoReq, postId, userId);
        return "redirect:/posts/" +postId;  // 게시물 목록 페이지로 리다이렉트
    }

}
