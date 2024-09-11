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

        List<CommentDtoRes> commentList = post.getComments().stream()
                .map(comment -> new CommentDtoRes(
                        comment.getCommentId(),
                        comment.getContent(),
                        new UserDtoRes(
                                comment.getUser().getUserId(),
                                comment.getUser().getId(),
                                comment.getUser().getName())
                ))
                .collect(Collectors.toList());

        UserDtoRes userInfo = new UserDtoRes(
                post.getUser().getUserId()
                , post.getUser().getId()
                , post.getUser().getName()
        );

        return new PostDetailDtoRes(
                post.getPostId()
                , post.getTitle()
                , post.getContent()
                , post.isAnnounce()
                , userInfo
                , commentList
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

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. userId: " + userId));

        if(Objects.equals(post.getUser().getUserId(), user.getUserId())) {
            post.setTitle(postDtoReq.getTitle());
            post.setContent(postDtoReq.getContent());
            postRepository.save(post);
        } else {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }
    }

    @Override
    public void delete(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + postId));

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. userId: " + userId));

        if (Objects.equals(post.getUser().getUserId(), user.getUserId())) {
            postRepository.deleteById(postId);
        } else {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }
    }
}
