package mini.backend.comment;

import mini.backend.domain.Comment;
import mini.backend.domain.Post;
import mini.backend.domain.User;
import mini.backend.post.PostRepository;
import mini.backend.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class CommentServiceCRUD {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CommentService commentService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    private Post testPost;
    private User testUser;

    @BeforeEach
    public void setUp() {
        // 기존 댓글 및 테스트 데이터 초기화
        commentRepository.deleteAll();

        // 테스트 게시글 생성
        testPost = Post.builder()
                .title("Test Title")
                .content("Test Content")
                .build();
        testPost = postRepository.save(testPost);

        // 테스트 유저 생성
        testUser = User.builder()
                .id("test123")
                .password("password123")  // 패스워드도 추가
                .name("Test User")
                .build();
        testUser = userRepository.save(testUser);
    }

    @Test
    public void 댓글작성_및_단건조회() throws Exception {
        // Given: 댓글 요청 객체 생성
        CommentDtoReq commentDtoReq = new CommentDtoReq("this is comment test string", testPost.getPostId());

        // When: 댓글 작성
        Long commentId = commentService.create(commentDtoReq, testPost.getPostId(), testUser.getId());

        // Then: 작성된 댓글이 잘 저장되었는지 확인
        Comment savedComment = commentRepository.findById(commentId).orElseThrow();
        Assertions.assertEquals(commentDtoReq.getContent(), savedComment.getContent());

        // 로그 출력
        System.out.println("댓글 작성 및 단건 조회 테스트 성공. 댓글 ID: " + commentId);
    }

    @Test
    public void 댓글수정() throws Exception {
        // Given: 댓글 생성 후 수정할 요청 객체 준비
        CommentDtoReq commentDtoReq = new CommentDtoReq("this is comment test string", testPost.getPostId());
        Long commentId = commentService.create(commentDtoReq, testPost.getPostId(), testUser.getId());

        // When: 댓글 수정 요청
        CommentDtoReq updatedCommentDtoReq = new CommentDtoReq("this is updated comment test string", testPost.getPostId());
        CommentDtoRes commentDtoRes = commentService.update(updatedCommentDtoReq, commentId, testUser.getId());

        // Then: 수정된 내용 확인
        Comment updatedComment = commentRepository.findById(commentId).orElseThrow();
        Assertions.assertEquals(updatedCommentDtoReq.getContent(), updatedComment.getContent());

        // 로그 출력
        System.out.println("댓글 수정 테스트 성공. 수정된 댓글 ID: " + commentId);
    }

    @Test
    public void 댓글삭제() throws Exception {
        // Given: 댓글 작성 후 삭제 요청
        CommentDtoReq commentDtoReq = new CommentDtoReq("this is comment test string", testPost.getPostId());
        Long commentId = commentService.create(commentDtoReq, testPost.getPostId(), testUser.getId());

        // When: 댓글 삭제
        commentService.deleteOne(commentId, testUser.getId());

        // Then: 댓글이 실제로 삭제되었는지 확인
        Optional<Comment> deletedComment = commentRepository.findById(commentId);
        assertTrue(deletedComment.isEmpty(), "댓글이 삭제되지 않았습니다.");

        // 로그 출력
        System.out.println("댓글 삭제 테스트 성공. 삭제된 댓글 ID: " + commentId);
    }
}
