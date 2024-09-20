package mini.backend.like;

import lombok.RequiredArgsConstructor;
import mini.backend.user.User;
import mini.backend.post.PostRepository;
import mini.backend.user.UserRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    // 레디스에서 사용할 락의 기본 키
    private static final String LOCK_KEY = "likeLock:";

    public LikeDtoRes addLike(Long postId, String Id) {
        String lockKey = LOCK_KEY + postId;

        // 분산 락 시도
        Boolean lockAcquired = redisTemplate.opsForValue().setIfAbsent(lockKey, 1L, 5, TimeUnit.SECONDS); // 값도 Long으로 설정
        if (lockAcquired != null && lockAcquired) {
            try {

                Optional<User> user = userRepository.findById(Id);

                String likeKey = "likes:" + postId; // 좋아요 세트 키 설정
                // 사용자가 이미 좋아요를 누른 경우 처리
                if (Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(likeKey, user.get().getUserId()))) {
                    // 좋아요 취소
                    redisTemplate.opsForSet().remove(likeKey, user.get().getUserId());
                    return new LikeDtoRes(postId, user.get().getUserId(), "Like canceled.");
                } else {
                    // 좋아요 추가
                    redisTemplate.opsForSet().add(likeKey, user.get().getUserId());
                    return new LikeDtoRes(postId, user.get().getUserId(), "Like added.");
                }

            } finally {
                // 락 해제
                redisTemplate.delete(lockKey);
            }
        } else {
            // 락 대기 상태에서 재시도 로직 추가 가능
            return addLike(postId, Id); // 재귀 호출 (주의: 무한 루프 방지)
        }
    }
}
