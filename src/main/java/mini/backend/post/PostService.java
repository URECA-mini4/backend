package mini.backend.post;

import java.util.List;

public interface PostService {

    // 목록 조회
    List<PostDtoRes> getPostList();

    // 상세 조회
    PostDtoRes getPost(Long postId);

    // 생성
    PostDtoRes create(Long userId, PostDtoReq postDtoReq);

    // 수정
    PostDtoRes update(Long userId, PostDtoReq postDtoReq);

    // 삭제
    void delete(Long postId);

}