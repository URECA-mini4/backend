package mini.backend.comment;

import mini.backend.domain.Comment;

import java.util.List;

public interface CommentService {
    //POST
    void save(Comment comment);

    //GET
    List<Comment> getByPostId(Long postId);
    //List<Comment> findByUserId(Long userId);
    //Comment findById(Long commentId);

    //PATCH
    Comment update(Long userId, Comment comment);

    //DELETE
    void deleteOne(Long commentId);
    void deleteAll(List<Long> commentId);
}
