package mini.backend.post;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mini.backend.auth.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "게시물", description = "게시물 관련 API")
@RequestMapping("/api/v1/")
public class PostController {

    private final PostService postService;
    private final AuthenticationFacade authenticationFacade;


    // 게시물 생성
    @Operation(summary = "게시물 생성", description = "사용자가 게시물을 생성합니다.")
    @PostMapping("/posts")
    public ResponseEntity<PostDetailDtoRes> createPost(@RequestBody PostDtoReq postDtoReq){
        String Id = authenticationFacade.getAuthentication();
        Long createdPostId = postService.create(Id, postDtoReq);
        PostDetailDtoRes createdPost = postService.getPost(createdPostId);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    // 게시물 목록 조회
    @Operation(summary = "게시물 목록 조회", description = "전체 게시물 목록을 조회합니다.")
    @GetMapping("/posts")
    public ResponseEntity<List<PostBaseDtoRes>> getPostList(@RequestParam(defaultValue = "1") int page) {
        int pageSize = 10;
        int pageNumber = Math.max(page - 1, 0);

        List<PostBaseDtoRes> postList = postService.getPostList(pageNumber,pageSize);

        return ResponseEntity.ok(postList);
    }

    // 게시물 상세 조회
    @Operation(summary = "게시물 상세 조회", description = "게시물 상세 목록을 조회합니다.")
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDetailDtoRes> getPost(@PathVariable Long postId, HttpServletRequest request, HttpServletResponse response) {
        PostDetailDtoRes postDetailDtoRes = postService.getPost(postId);
        Long postView = postService.increaseUp(postId, request, response);
        return ResponseEntity.ok(postDetailDtoRes);
    }

    // 게시물 수정
    @Operation(summary = "게시물 수정", description = "게시물을 수정합니다.")
    @PatchMapping("/posts/{postId}")
    public ResponseEntity<PostDetailDtoRes> updatePost(@PathVariable("postId") Long postId
    , @RequestBody PostDtoReq postDtoReq){
        String Id = authenticationFacade.getAuthentication();
        postService.update(Id, postId, postDtoReq);

        PostDetailDtoRes updatePost = postService.getPost(postId);

        return ResponseEntity.ok(updatePost);
    }

    // 게시물 삭제
    @Operation(summary = "게시물 삭제", description = "게시물을 삭제합니다.")
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<String> postDelete(@PathVariable("postId") Long postId) {
        String Id = authenticationFacade.getAuthentication();
        postService.delete(Id, postId);

        return ResponseEntity.ok("게시물이 삭제되었습니다.");
    }

}
