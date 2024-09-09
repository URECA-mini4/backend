package mini.backend.comment;

import mini.backend.domain.Comment;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CommentService {
    //POST
    public Long create(CommentDtoReq commentDtoReq, Long postId, Long userId);

    //GET
    public List<CommentDtoRes> findById(Long postId);

    //PATCH
    public CommentDtoRes update(CommentDtoReq commentDtoReq, Long commentId, Long userId);

    //DELETE
    public void deleteOne(Long commentId);
    //public void deleteAll(List<Long> commentIds);
}
