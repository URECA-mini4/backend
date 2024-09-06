package mini.backend.post;

import java.util.List;

public interface PostService {

    // 목록 조회
    List<PostBaseDtoRes> getPostList();

    // 상세 조회
    PostDetailDtoRes getPost(Long postId);

    // 생성
    PostDetailDtoRes create(Long userId, PostDtoReq postDtoReq);

    // 수정
    PostDetailDtoRes update(Long userId, PostDtoReq postDtoReq);

    // 삭제
    void delete(Long postId);

}