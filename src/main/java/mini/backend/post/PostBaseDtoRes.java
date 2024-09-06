package mini.backend.post;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostBaseDtoRes {
    private final Long postId;
    private final Long userId;
    private final String title;
    private final boolean isAnnounce;
}
