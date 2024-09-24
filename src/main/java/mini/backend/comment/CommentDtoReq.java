package mini.backend.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class CommentDtoReq {
    private final String content;
    private Long postId;
}
