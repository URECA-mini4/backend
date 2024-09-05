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
    private final Long userId;
    private final String content;
}
