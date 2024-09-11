package mini.backend.comment;

import lombok.RequiredArgsConstructor;
import mini.backend.domain.Comment;
import mini.backend.domain.Post;
import mini.backend.domain.User;
import mini.backend.post.PostRepository;
import mini.backend.user.UserDtoRes;
import mini.backend.user.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    //POST
    @Override
    @Transactional
    public Long create(CommentDtoReq commentDtoReq, Long postId, Long userId) {
        //Post조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 postId: " + postId));

        //User조회
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 userId: " + userId));

        //Comment 엔티티 생성
        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content(commentDtoReq.getContent())
                .build();

        //Comment 저장
        commentRepository.save(comment);

        return comment.getCommentId();
    }

    //GET
    @Override
    @Transactional(readOnly = true)
    public List<CommentDtoRes> findByPostId(Long postId) {
        //Post조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 postId: " + postId));

        //Post에 해당하는 댓글 조회
        //Comment -> CommentDtoRes 변환
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

        return commentList;
    }

    //PATCH
    @Override
    @Transactional
    public CommentDtoRes update(CommentDtoReq commentDtoReq, Long commentId, Long userId) {
        //유저 정보 확인
        User user = userRepository.findByUserId(userId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 userId: " +userId));

        //수정할 댓글 정보 확인
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 commentId: " + commentId));

        //댓글 작성자와 수정 요청자 일치 확인
        if(!comment.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("댓글을 수정할 권한이 없음.");
        }

        //댓글 수정
        comment.editContent(commentDtoReq.getContent());

        //수정된 댓글 반환
        return new CommentDtoRes(comment);
    }

    //DELETE
    @Override
    @Transactional
    public void deleteOne(Long commentId, Long userId) {
        //유저 정보 확인
        User user = userRepository.findByUserId(userId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 userId: " +userId));

        //수정할 댓글 정보 확인
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 commentId: " + commentId));

        //댓글 작성자와 삭제 요청자 일치 확인
        if(!comment.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("댓글을 수정할 권한이 없음.");
        }

        //삭제할 댓글 존재 확인
        if (!commentRepository.existsById(commentId)) {
            throw new IllegalArgumentException("존재하지 않는 댓글.");
        }

        //댓글 삭제
        commentRepository.deleteById(commentId);
    }

/*    @Override
    @Transactional
    public void deleteAll(List<Long> commentIds) {
        // 삭제할 댓글이 있는지 확인 (ID 리스트 기준으로 모두 확인)
        //삭제할 댓글이 아예없는 경우, 삭제할 여러개의 댓글 중에 일부만 없는 경우 등등을 케이스도 확인?
        List<Comment> comments = commentRepository.findAllById(commentIds);
        if (comments.isEmpty()) {
            throw new IllegalArgumentException("해당 게시글의 댓글이 0개.");
        }

        // 댓글들 삭제
        commentRepository.deleteAll(comments);
    }*/
}
