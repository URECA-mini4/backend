package mini.backend.comment;

import mini.backend.domain.Comment;

import java.util.List;

public interface CommentService {
    //POST
    void create(CommentDtoReq commentDtoReq, Long postId);

    //GET
    List<CommentDtoRes> findById(Long postId);

    //PATCH
    CommentDtoRes update(CommentDtoReq commentDtoReq, Long userId);

    //DELETE
    void deleteOne(Long commentId);
    void deleteAll(List<Long> commentIds);
}
