package mini.backend.post;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mini.backend.comment.CommentDtoRes;
import mini.backend.comment.CommentService;
import mini.backend.domain.Comment;
import mini.backend.domain.Post;
import mini.backend.domain.User;
import mini.backend.user.UserDtoRes;
import mini.backend.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentService commentService;

    @Override
    public List<PostBaseDtoRes> getPostList() {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(post -> new PostBaseDtoRes(
                        post.getPostId(),
                        post.getTitle(),
                        post.isAnnounce(),
                        new UserDtoRes(
                                post.getUser().getUserId(),
                                post.getUser().getId(),
                                post.getUser().getName()
                        )
                ))
                .collect(Collectors.toList());
    }

    @Override
    public PostDetailDtoRes getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다. id=" + postId));

        // 댓글 리스트(Comment -> CommentDtoRes로 변환)
        List<CommentDtoRes> commentDtoResList = post.getComments().stream()
                .map(comment -> new CommentDtoRes(
                        comment.getCommentId(),
                        comment.getContent(),
                        new UserDtoRes(
                                comment.getUser().getUserId(),
                                comment.getUser().getId(),
                                comment.getUser().getName()
                        )
                )).collect(Collectors.toList());

        // 작성자 정보(User -> UserDtoRes로 변환)
        UserDtoRes userInfo = new UserDtoRes(
                post.getUser().getUserId(),
                post.getUser().getId(),
                post.getUser().getName()
        );

        // PostDetailDtoRes 반환
        return new PostDetailDtoRes(
                post.getPostId(),
                post.getTitle(),
                post.getContent(),
                post.isAnnounce(),
                userInfo,
                commentDtoResList  // 변환된 댓글 리스트를 설정
        );
    }

    @Override
    public Long create(Long userId, PostDtoReq postDtoReq) {
        User user = userRepository.findByUserId(userId).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 존재하지 않습니다. id=" + userId));

        Post post = new Post();
        post.setTitle(postDtoReq.getTitle());
        post.setContent(postDtoReq.getContent());
        post.setAnnounce(postDtoReq.isAnnounce());
        post.setUser(user);

        Post savedPost = postRepository.save(post);
        return savedPost.getPostId();
    }

    @Override
    public void update(Long userId, Long postId, PostDtoReq postDtoReq) {
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + postId));

        Optional<User> opUser = userRepository.findById(userId);
        if(opUser.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다. userId: " + userId);
        }
        if(Objects.equals(post.getUser().getUserId(), opUser.get().getUserId())) {
            post.setTitle(postDtoReq.getTitle());
            post.setContent(postDtoReq.getContent());

            postRepository.save(post);
        }
    }

    @Override
    public void delete(Long postId) {
        postRepository.deleteById(postId);
    }
}
