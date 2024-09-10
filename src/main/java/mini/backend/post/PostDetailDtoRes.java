package mini.backend.post;

import lombok.*;
import mini.backend.comment.CommentDtoRes;
import mini.backend.domain.Comment;
import mini.backend.user.UserDtoRes;

import java.util.List;

@Data
public class PostDetailDtoRes extends PostBaseDtoRes{
    private final String content;
    private final List<CommentDtoRes> commentList;

    public PostDetailDtoRes(Long postId, String title, String content, boolean isAnnounce, UserDtoRes userDtoRes, List<CommentDtoRes> commentList) {
        super(postId, title, isAnnounce, userDtoRes);
        this.content = content;
        this.commentList = commentList;
    }
}
