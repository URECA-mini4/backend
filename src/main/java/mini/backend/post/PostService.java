package mini.backend.post;

import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {

    // 목록 조회
    List<PostBaseDtoRes> getPostList();

    // 상세 조회
    PostDetailDtoRes getPost(Long postId);

    // 생성
    Long create(Long userId, PostDtoReq postDtoReq);

    // 수정
    void update(Long userId, Long postId, PostDtoReq postDtoReq);

    // 삭제
    void delete(Long postId);

}