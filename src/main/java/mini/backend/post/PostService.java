package mini.backend.post;

import mini.backend.domain.Post;

import java.util.List;

public interface PostService {

    // 목록 조회
    List<Post> getList();

    // 상세 조회
    Post getById(Long postId);

    // 생성
    Post save(Long userId, Post post);

    // 수정
    Post update(Long userId, Post post);

    // 삭제
    void delete(Long postId);

}
