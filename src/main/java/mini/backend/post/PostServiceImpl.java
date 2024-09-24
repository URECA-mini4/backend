package mini.backend.post;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mini.backend.comment.CommentDtoRes;
import mini.backend.comment.CommentService;
import mini.backend.user.User;
import mini.backend.post.view.PostHitRepository;
import mini.backend.user.UserDtoRes;
import mini.backend.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private final PostHitRepository postHitRepository;

    private final CommentService commentService;

    @Override
    public List<PostBaseDtoRes> getPostList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Post> postPage = postRepository.findAll(pageable);

        // 각 Post 객체를 dto 타입으로 변환 후 Page<Post>에서 List<Post>로 반환
        List<PostBaseDtoRes> postList = postPage.stream()
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

        return postList;
    }

    @Override
    public PostDetailDtoRes getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다. id=" + postId));

        List<CommentDtoRes> commentList = commentService.findByPostId(postId);

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
    public Long increaseUp(Long postId, HttpServletRequest request, HttpServletResponse response) {
        Cookie oldCookie = null;
        Long hitCount;

        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie:cookies){
                if(cookie.getName().equals("postView")){
                    oldCookie = cookie;
                }
            }
        }

        if(oldCookie != null) {
            if (!oldCookie.getValue().contains("[" + postId.toString() + "]")) {
                hitCount = postHitRepository.incrementHit(postId); //redis 조회수 업
                oldCookie.setValue(oldCookie.getValue() + "_[" + postId.toString() + "]");
                oldCookie.setPath("/");
                response.addCookie(oldCookie);
            } else {
                hitCount = postHitRepository.getHit(postId);
            }
        } else {
            hitCount = postHitRepository.incrementHit(postId); //redis 조회수 업
            Cookie newCookie = new Cookie("postView", "[" + postId.toString() + "]");
            newCookie.setPath("/");
            response.addCookie(newCookie);
        }

        return hitCount;
    }

    @Override
        public Long create(String Id, PostDtoReq postDtoReq) {
            User user = userRepository.findById(Id).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 존재하지 않습니다. id=" + Id));

        Post post = new Post();
        post.setTitle(postDtoReq.getTitle());
        post.setContent(postDtoReq.getContent());
        post.setAnnounce(postDtoReq.isAnnounce());
        post.setUser(user);

        Post savedPost = postRepository.save(post);
        return savedPost.getPostId();
    }

    @Override
    public void update(String Id, Long postId, PostDtoReq postDtoReq) {
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + postId));

        User user = userRepository.findById(Id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. userId: " + Id));

        if(Objects.equals(post.getUser().getId(), user.getId())) {
            post.setTitle(postDtoReq.getTitle());
            post.setContent(postDtoReq.getContent());
            postRepository.save(post);
        } else {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }
    }

    @Override
    public void delete(String Id, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + postId));

        User user = userRepository.findById(Id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. userId: " + Id));

        if (Objects.equals(post.getUser().getId(), user.getId())) {
            postRepository.deleteById(postId);
        } else {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }
    }
}
