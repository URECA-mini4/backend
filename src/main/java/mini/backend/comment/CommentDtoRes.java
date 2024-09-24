package mini.backend.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import mini.backend.user.UserDtoRes;

@Getter
@AllArgsConstructor
public class CommentDtoRes {
    private final Long commentId;
    private final String content;
    private UserDtoRes userInfo;

    public CommentDtoRes(Comment comment) {
        this.commentId = comment.getCommentId();
        this.content = comment.getContent();
    }
}
