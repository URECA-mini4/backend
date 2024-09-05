package mini.backend.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import mini.backend.domain.Post;
import mini.backend.domain.User;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class CommentDtoRes {
    private final Long commentId;
    private final User userId;
    private final Post postId;
    private final String content;
    private final Timestamp createdDate;
    private final Timestamp updatedDate;
}
