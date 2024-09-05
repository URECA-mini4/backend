package mini.backend.post;
import lombok.AllArgsConstructor;
import lombok.Data;
import mini.backend.domain.Comment;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
public class PostDtoRes {
    private final Long postId;
    private final Long userId;
    private final String title;
    private final String content;
    private final Timestamp createdDate;
    private final Timestamp updatedDate;
    private final boolean isAnnounce;
    private final List<Comment> comments;
}
