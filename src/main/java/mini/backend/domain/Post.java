package mini.backend.domain;

import io.swagger.v3.oas.annotations.info.Info;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Post extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name ="user_id")
    private User user;

    private String title;
    private String content;

    private boolean isAnnounce;

    @OneToMany(mappedBy = "post", fetch=FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();
}

