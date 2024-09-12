package mini.backend.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import mini.backend.domain.Post;
import mini.backend.domain.User;
import mini.backend.user.UserDtoRes;

import java.sql.Timestamp;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class CommentDtoReq {
    private final String content;
    private Long postId;
}
