package mini.backend.post;

import mini.backend.domain.Post;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {

    // 목록 조회
    List<PostBaseDtoRes> getPostList(int page, int size);

    // 상세 조회
    PostDetailDtoRes getPost(Long postId);

    // 생성
    Long create(String Id, PostDtoReq postDtoReq);

    // 수정
    void update(String Id, Long postId, PostDtoReq postDtoReq);

    // 삭제
    void delete(String Id, Long postId);
}