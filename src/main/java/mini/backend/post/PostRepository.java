package mini.backend.post;

import mini.backend.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 최신순으로 게시글을 조회하는 메서드
    @Query("SELECT p FROM Post p ORDER BY p.createdDate DESC")
    List<Post> findAllOrderByCreatedDateDesc();
}
