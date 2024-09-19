package mini.backend.post.view;

import org.springframework.stereotype.Repository;

@Repository
public interface PostHitRepository {
    Long incrementHit(Long postId);
    Long getHit(Long postId);
}
