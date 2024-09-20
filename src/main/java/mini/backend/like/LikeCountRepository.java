package mini.backend.like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LikeCountRepository extends JpaRepository<LikeCount, Long> {
    @Modifying
    @Query("UPDATE LikeCount lc SET lc.count = lc.count + 1 WHERE lc.postId = ?1")
    void incrementLikeCount(Long postId);

    @Modifying
    @Query("UPDATE LikeCount lc SET lc.count = lc.count - 1 WHERE lc.postId = ?1")
    void decrementLikeCount(Long postId);

    Optional<LikeCount> findByPostId(Long postId);
}
