package mini.backend.post;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mini.backend.domain.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Tag(name = "게시물", description = "게시물 관련 API")
@RequestMapping("/api/v1/")
public class PostController {
    @Autowired
    private PostService postService;

    @Operation(summary = "게시물 생성", description = "사용자가 게시물을 생성합니다.")
    @PostMapping("/posts")
    public ResponseEntity<PostDetailDtoRes> createPost(Long userId, @RequestParam("title") String title, @RequestParam("content") String content) {
        PostDtoReq postDtoReq = new PostDtoReq(title, content);
        Long createdPostId = postService.create(userId,postDtoReq);
        PostDetailDtoRes createdPost = postService.getPost(createdPostId);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    @Operation(summary = "게시물 목록 조회", description = "전체 게시물 목록을 조회합니다.")
    @GetMapping("/posts")
    public ResponseEntity<List<PostBaseDtoRes>> getPostList() {
        List<PostBaseDtoRes> postList = postService.getPostList();

        return ResponseEntity.ok(postList);
    }

    @Operation(summary = "게시물 상세 조회", description = "게시물 상세 목록을 조회합니다.")
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDetailDtoRes> getPost(@PathVariable("postId") Long postId) {
        PostDetailDtoRes post = postService.getPost(postId);

        return ResponseEntity.ok(post);
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
