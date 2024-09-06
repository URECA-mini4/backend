package mini.backend.post;

import lombok.*;
import mini.backend.user.UserDtoRes;

import java.util.List;

@Data
@AllArgsConstructor
public class PostDetailDtoRes extends PostBaseDtoRes{
    private final String content;
//    private final List<CommentDtoRes> comments;

    public PostDetailDtoRes(Long postId, String title, boolean isAnnounce, List<UserDtoRes> userInfo, String content /*,List<CommentDtoRes> comments */) {
        super(postId, title, isAnnounce, userInfo);
        this.content = content;
//        this.comments = comments;
    }
}
