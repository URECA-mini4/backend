package mini.backend.like;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LikeDtoRes {
    private Long postId; // 좋아요가 추가된 게시물 ID
    private Long userId; // 좋아요를 누른 사용자 ID
    private String message; // 성공 메시지
}
