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
public class Post {

    @Id
    @GeneratedValue
    private Long postId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name ="user_id")
    private User userId;

    private String title;
    private String content;
    private Timestamp createdDate;
    private Timestamp modifiedDate;
    private boolean isAnnounce;

    @OneToMany(mappedBy = "postId")
    private List<Comment> comments = new ArrayList<>();
}

