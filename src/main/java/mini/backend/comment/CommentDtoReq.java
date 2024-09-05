package mini.backend.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import mini.backend.domain.Post;
import mini.backend.domain.User;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class CommentDtoReq {
    private final String commenterId; //댓글작성자의 계정 아이디입니다. userId로 두자니 pk인 id랑 헷갈려서 바꿔봤습니다
    private final String name; //댓글 작성자 혹은 이름
    private final String content;
}
