package mini.backend.post;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface PostService {

    // 목록 조회
    List<PostBaseDtoRes> getPostList(int page, int size);

    // 상세 조회
    PostDetailDtoRes getPost(Long postId);

    //조회수
    Long increaseUp(Long postId, HttpServletRequest request, HttpServletResponse response);

    // 생성
    Long create(Long userId, PostDtoReq postDtoReq);

    // 수정
    void update(Long userId, Long postId, PostDtoReq postDtoReq);

    // 삭제
    void delete(Long userId, Long postId);
}