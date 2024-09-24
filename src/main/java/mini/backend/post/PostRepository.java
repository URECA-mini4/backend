package mini.backend.post;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // 최신순으로 게시글을 조회하는 메서드
    @Query("SELECT p FROM Post p ORDER BY p.createdDate DESC")
    List<Post> findAllOrderByCreatedDateDesc();

    @Query("SELECT p.postView FROM Post p WHERE p.postId = :postId")
    Long findPostViewByPostId(Long postId);
}
