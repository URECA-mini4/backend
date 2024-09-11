package mini.backend.post;
import lombok.*;
import mini.backend.user.UserDtoRes;

@Data
@AllArgsConstructor
public class PostBaseDtoRes {
    private final Long postId;
    private final String title;
    private final boolean isAnnounce;
    private final UserDtoRes userInfo;
}
