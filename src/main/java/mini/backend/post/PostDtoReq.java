package mini.backend.post;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class PostDtoReq {
    private final String title;
    private final String content;
    private final boolean isAnnounce;
}
