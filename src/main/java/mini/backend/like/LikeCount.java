package mini.backend.like;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mini.backend.post.Post;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "like_count")
public class LikeCount {

    @Id
    private Long postId; // 게시물 ID

    private int count; // 좋아요 수

    @OneToOne
    @MapsId
    @JoinColumn(name = "post_id")
    private Post post; // 게시물과의 관계

}