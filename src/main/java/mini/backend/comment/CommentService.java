package mini.backend.comment;

import mini.backend.domain.Comment;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CommentService {
    //POST
    public Long create(CommentDtoReq commentDtoReq, Long postId, String Id);

    //GET
    public List<CommentDtoRes> findByPostId(Long postId);

    //PATCH
    public CommentDtoRes update(CommentDtoReq commentDtoReq, Long commentId, String Id);

    //DELETE
    public void deleteOne(Long commentId, String Id);
    //public void deleteAll(List<Long> commentIds);
}
