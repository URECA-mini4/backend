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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class CommentServiceCRUD {
    @Autowired CommentRepository commentRepository;
    @Autowired CommentService commentService;
    @Autowired PostRepository postRepository;
    @Autowired UserRepository userRepository;

    private Post testPost;
    private User testUser;

    @BeforeEach
    public void setUp(){
        commentRepository.deleteAll();

        testPost = Post.builder()
                    .title("Test Title")
                    .content("Test Content")
                    .build();
        testPost = postRepository.save(testPost);

        testUser = User.builder()
                .name("test user name")
                .build();
        testUser = userRepository.save(testUser);
    }

    @Test
    public void 댓글작성_및_단건조회() throws Exception{
        //Given
        CommentDtoReq commentDtoReq = new CommentDtoReq("this is comment test string");

        //When
        Long commentId = commentService.create(commentDtoReq, testPost.getPostId(), testUser.getUserId());

        //Then
        Comment savedComment = commentRepository.findById(commentId).orElseThrow();
        Assertions.assertEquals(commentDtoReq.getContent(), savedComment.getContent());
    }

    @Test
    public void 댓글수정() throws Exception{
        //given
        CommentDtoReq commentDtoReq = new CommentDtoReq("this is comment test string");
        Long commentId = commentService.create(commentDtoReq, testPost.getPostId(), testUser.getUserId());

        //when
        CommentDtoReq updatedCommentDtoReq = new CommentDtoReq("this is updated comment test string");
        CommentDtoRes commentDtoRes = commentService.update(updatedCommentDtoReq, commentId, testUser.getUserId());

        //then
        Comment updatedComment = commentRepository.findById(commentId).orElseThrow();
        Assertions.assertEquals(updatedCommentDtoReq.getContent(), updatedComment.getContent());
    }

    @Test
    public void 댓글삭제() throws Exception{
        //given
        CommentDtoReq commentDtoReq = new CommentDtoReq("this is comment test string");
        Long commentId = commentService.create(commentDtoReq, testPost.getPostId(), testUser.getUserId());
        Comment savedComment = commentRepository.findById(commentId).orElseThrow();

        //when
        commentService.deleteOne(commentId);

        //then
        Optional<Comment> deletedComment = commentRepository.findById(commentId);
        assertTrue(deletedComment.isEmpty(), "댓글이 삭제되지 않았습니다.");
    }
}
