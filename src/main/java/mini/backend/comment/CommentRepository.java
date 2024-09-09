package mini.backend.comment;

import lombok.RequiredArgsConstructor;
import mini.backend.domain.Comment;
import mini.backend.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
