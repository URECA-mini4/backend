package mini.backend.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import mini.backend.domain.Post;
import mini.backend.domain.User;
import mini.backend.user.UserDtoRes;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class CommentDtoRes {
    private final Long commentId;
    private final String content;
    private final UserDtoRes userDtoRes;
}
