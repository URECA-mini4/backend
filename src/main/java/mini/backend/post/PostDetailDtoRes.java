package mini.backend.post;

import lombok.*;

@Getter
@ToString
public class PostDetailDtoRes extends PostBaseDtoRes{
    private final String content;
//    private final List<CommentDtoRes> comments;

    public PostDetailDtoRes(Long postId, Long userId, String title, boolean isAnnounce, String content /*,List<CommentDtoRes> comments */) {
        super(postId, userId, title, isAnnounce);
        this.content = content;
//        this.comments = comments;
    }
}
