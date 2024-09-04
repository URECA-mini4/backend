package mini.backend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue
    private Long userId;

    private String id;
    private String password;
    private String name;
    private Timestamp createdDate;
    private Timestamp modifiedDate;

    @Enumerated(EnumType.STRING)
    private UserStatus status; //[ACTIVED, SUSPENDED, DELETED]

    @Enumerated(EnumType.STRING)
    private UserRole role; //[ADMIN, USER]

    @OneToMany(mappedBy = "userId")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy="userId")
    private List<Comment> comments = new ArrayList<>();


}
