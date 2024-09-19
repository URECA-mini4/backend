package mini.backend.post.view;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisPostHitRepository implements PostHitRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private ValueOperations<String, Object> valueOperations;

    @PostConstruct
    public void setUp(){
        valueOperations = redisTemplate.opsForValue();
    }

    private final static String POST_HIT_KEY = "post:hit:";

    @Override
    public Long incrementHit(Long postId) {
        return valueOperations.increment(POST_HIT_KEY+postId, 1L);
    }

    @Override
    public Long getHit(Long postId) {
        Object value = valueOperations.get(POST_HIT_KEY + postId);
        if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException e) {
                return null; // 또는 적절한 기본값
            }
        }
        return null; // 또는 적절한 기본값
    }
}
