package mini.backend.comment;

import mini.backend.domain.Comment;

import java.util.List;

public interface CommentService {
    //POST
    void create(CommentDtoReq commentDtoReq);

    //GET
    List<CommentDtoRes> findById(Long postId);

    //PATCH
    CommentDtoReq update(CommentDtoReq commentDtoReq, Long userId);

    //DELETE
    void deleteOne(Long commentId);
    void deleteAll(List<Long> commentIds);
}
