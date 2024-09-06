package mini.backend.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import mini.backend.domain.Post;
import mini.backend.domain.User;
import mini.backend.user.UserDtoRes;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class CommentDtoReq {
    private final UserDtoRes userDtoRes; //pull 받아야 함 (구현x)
    private final String content;
}
