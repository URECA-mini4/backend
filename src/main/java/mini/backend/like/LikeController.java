package mini.backend.like;

import lombok.RequiredArgsConstructor;
import mini.backend.auth.AuthenticationFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class LikeController {
    private final AuthenticationFacade authenticationFacade;
    private final LikeService likeService;

    @PostMapping("/{postId}/like")
    public ResponseEntity<LikeDtoRes> addLike(@PathVariable Long postId){
        String Id = authenticationFacade.getAuthentication();
        LikeDtoRes createdPostLike = likeService.addLike(postId, Id);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdPostLike);
    }
}
