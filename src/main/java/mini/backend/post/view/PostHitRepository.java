package mini.backend.post.view;

import org.springframework.stereotype.Repository;

import java.util.Set;

public interface PostHitRepository {
    void incrementHit(Long postId);
    Long getHit(Long postId);
    Set<Long> getAllUpdatedPostIds();
}
