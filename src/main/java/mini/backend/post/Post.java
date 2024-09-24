package mini.backend.post;

import jakarta.persistence.*;
import lombok.*;
import mini.backend.common.BaseTimeEntity;
import mini.backend.comment.Comment;
import mini.backend.user.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name ="user_id")
    private User user;

    private String title;
    private String content;

    private boolean isAnnounce;

    @OneToMany(mappedBy = "post", fetch=FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    private Long postView = 0L;

    public void increasePostView(Long postView) {
        this.postView = postView;
    }
}

