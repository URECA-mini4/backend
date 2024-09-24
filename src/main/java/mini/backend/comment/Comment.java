package mini.backend.comment;

import jakarta.persistence.*;
import lombok.*;
import mini.backend.common.BaseTimeEntity;
import mini.backend.post.Post;
import mini.backend.user.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String content;
  
    public void editContent(String content){
        this.content = content;
    }
}
