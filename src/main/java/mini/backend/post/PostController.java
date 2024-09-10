package mini.backend.post;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mini.backend.domain.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Tag(name = "게시물", description = "게시물 관련 API")
public class PostController {
    @Autowired
    private PostService postService;

    // 게시물 생성
    @PostMapping("/posts")
    public String createPost(Long userId, @RequestParam("title") String title, @RequestParam("content") String content) {
        //Long userId = (Long) session.getAttribute("userId"); -> 세션에서 가져오는 경우
        //Long userId = Long.parseLong(principal.getName()); -> spring security 사용하는 경우
        userId = 6L; //일단 하드코딩 해놨음
        PostDtoReq postDtoReq = new PostDtoReq(title, content);
        postService.create(userId, postDtoReq);
        return "redirect:/posts";  // 게시물 목록 페이지로 리다이렉트
    }

    @GetMapping("/posts")
    public String getPostList(Model model) {
        List<PostBaseDtoRes> postList = postService.getPostList();
        model.addAttribute("posts", postList);
        return "post-list";  // Thymeleaf 템플릿 파일 이름 (post-list.html)
    }

    @Operation(summary = "게시물 상세 조회", description = "게시물 상세 목록을 조회합니다.")
    @GetMapping("/posts/{postId}")
    public String getPost(@PathVariable("postId") Long postId, Model model) {
        PostDetailDtoRes postDetailDtoRes = postService.getPost(postId);
        model.addAttribute("post", postDetailDtoRes);
        model.addAttribute("comments", postDetailDtoRes.getComments());
        return "post-detail";
    }

    @Operation(summary = "게시물 수정", description = "게시물을 수정합니다.")
    @PatchMapping("/posts/{postId}")
    public ResponseEntity<PostDetailDtoRes> updatePost(Long userId, @PathVariable("postId") Long postId
    , @RequestParam("title") String title, @RequestParam("content") String content){
        PostDetailDtoRes post = postService.getPost(postId);
        if(!post.getUserInfo().getUserId().equals(userId)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정 권한이 없습니다.");
        }

        PostDtoReq updatePostDtoReq = new PostDtoReq(title, content);
        postService.update(userId, postId, updatePostDtoReq);

        PostDetailDtoRes updatePost = postService.getPost(postId);

        return ResponseEntity.ok(updatePost);
    }


    @Operation(summary = "게시물 삭제", description = "게시물을 삭제합니다.")
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<String> postDelete(@PathVariable("postId") Long postId) {
        postService.delete(postId);

        return ResponseEntity.ok("게시물이 삭제되었습니다.");
    }

}
