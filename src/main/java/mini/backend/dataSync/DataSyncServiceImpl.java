package mini.backend.dataSync;

import lombok.RequiredArgsConstructor;
import mini.backend.domain.Post;
import mini.backend.post.PostRepository;
import mini.backend.post.view.PostHitRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class DataSyncServiceImpl implements DataSyncService {
    private final PostHitRepository postHitRepository; // 변경된 이름
    private final PostRepository postRepository;

    @Override
    @Scheduled(fixedRate = 10000) // 10초마다 업데이트
    public void syncHits() {
        // Redis에서 모든 포스트 ID를 가져와서 업데이트
        Set<Long> postIds = postHitRepository.getAllUpdatedPostIds();

        for (Long postId : postIds) {
            Long hitCount = postHitRepository.getHit(postId);
            if (hitCount != null) {
                updateHitCountInMysql(postId, hitCount);
            }
        }
    }

    @Override
    @Transactional
    public void updateHitCountInMysql(Long postId, Long hitCount) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found")); // 예외 처리
        post.increasePostView(hitCount);
        postRepository.save(post);// 조회 수 업데이트
    }
}