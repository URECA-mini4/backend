package mini.backend.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import mini.backend.common.BaseTimeEntity;
import mini.backend.comment.Comment;
import mini.backend.post.Post;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String id;
    private String password;
    private String name;

    @Enumerated(EnumType.STRING)
    private UserStatus status; //[ACTIVED, SUSPENDED, DELETED]

    @Enumerated(EnumType.STRING)
    private UserRole role; //[ADMIN, USER]

    @OneToMany(mappedBy = "user", fetch=FetchType.LAZY)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy="user", fetch=FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();
}