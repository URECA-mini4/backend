package mini.backend.post;

import lombok.*;
import mini.backend.comment.CommentDtoRes;
import mini.backend.domain.Comment;
import mini.backend.user.UserDtoRes;

import java.util.List;

@Data
public class PostDetailDtoRes extends PostBaseDtoRes{
    private final String content;
    private List<CommentDtoRes> comments;

    public PostDetailDtoRes(Long postId, String title, String content, boolean isAnnounce, UserDtoRes userDtoRes, List<Comment> comments) {
        super(postId, title, isAnnounce, userDtoRes);
        this.content = content;
    }
}
