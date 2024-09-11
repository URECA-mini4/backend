package mini.backend.post;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "게시물", description = "게시물 관련 API")
@RequestMapping("/api/v1/")
public class PostController {

    private final PostService postService;

    //게시물 생성
    @Operation(summary = "게시물 생성", description = "사용자가 게시물을 생성합니다.")
    @PostMapping("/posts")
    public String createPost(Long userId, @RequestBody PostDtoReq postDtoReq) {
        // Long userId = (Long) session.getAttribute("userId"); -> 세션에서 가져오는 경우
        // Long userId = Long.parseLong(principal.getName()); -> spring security 사용하는 경우
        userId = 5L; // 일단 하드코딩 해놨음
        Long createdPostId = postService.create(userId, postDtoReq);
        PostDetailDtoRes createdPost = postService.getPost(createdPostId);

        return "redirect:/api/v1/posts/" + createdPostId;

    }

    //게시물 목록 조회
    @Operation(summary = "게시물 목록 조회", description = "전체 게시물 목록을 조회합니다.")
    @GetMapping("/posts")
    public String getPostList(@RequestParam(defaultValue = "1") int page, Model model) {
        int pageSize = 10;

        int pageNumber = Math.max(page - 1, 0);

        Page<PostBaseDtoRes> postPage = postService.getPostList(pageNumber,pageSize);
        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("currentPage", pageNumber + 1);
        model.addAttribute("totalPages", postPage.getTotalPages());
        model.addAttribute("hasNext", postPage.hasNext());
        model.addAttribute("hasPrevious", postPage.hasPrevious());

        return "post-list";
    }

    //게시물 상세 조회
    @Operation(summary = "게시물 상세 조회", description = "게시물 상세 목록을 조회합니다.")
    @GetMapping("/posts/{postId}")
    public String getPost(@PathVariable("postId") Long postId, Model model) {
        PostDetailDtoRes postDetailDtoRes = postService.getPost(postId);

        model.addAttribute("post", postDetailDtoRes);
        model.addAttribute("comments", postDetailDtoRes.getCommentList());

        return "post-detail";
    }

    //아직 안 만듦
    @Operation(summary = "게시물 수정", description = "게시물을 수정합니다.")
    @PatchMapping("/posts/{postId}")
    public ResponseEntity<PostDetailDtoRes> updatePost(Long userId, @PathVariable("postId") Long postId
    , @RequestBody PostDtoReq postDtoReq){
        postService.update(userId, postId, postDtoReq);

        PostDetailDtoRes updatePost = postService.getPost(postId);

        return ResponseEntity.ok(updatePost);
    }

    //아직 안 만듦
    @Operation(summary = "게시물 삭제", description = "게시물을 삭제합니다.")
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<String> postDelete(Long userId, @PathVariable("postId") Long postId) {
        postService.delete(userId, postId);

        return ResponseEntity.ok("게시물이 삭제되었습니다.");
    }

}
