package mini.backend.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.nio.file.FileStore;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends BaseTimeEntity{

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