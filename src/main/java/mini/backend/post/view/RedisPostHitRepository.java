package mini.backend.post.view;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RedisPostHitRepository implements PostHitRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private ValueOperations<String, Object> valueOperations;
    private SetOperations<String, Object> setOperations;

    @PostConstruct
    public void setUp() {
        valueOperations = redisTemplate.opsForValue();
        setOperations = redisTemplate.opsForSet();
    }

    private static final String POST_HIT_KEY = "post:hit:";
    private static final String POST_IDS_KEY = "post:ids"; //업데이트된 모든 포스트 아이디들

    @Override
    public void incrementHit(Long postId) {
        setOperations.add(POST_IDS_KEY, postId);
        valueOperations.increment(POST_HIT_KEY + postId, 1L);
    }

    @Override
    public Long getHit(Long postId) {
        Object value = valueOperations.get(POST_HIT_KEY + postId);
        if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    public Set<Long> getAllUpdatedPostIds() {
        Set<Object> members = setOperations.members(POST_IDS_KEY);
        if (members == null) {
            return Set.of(); // 빈 집합 반환
        }
        // Set<Object>를 Set<Long>로 변환
        return members.stream()
                .map(member -> {
                    if (member instanceof String) {
                        try {
                            return Long.parseLong((String) member);
                        } catch (NumberFormatException e) {
                            return null; // 변환 실패 시 null 반환
                        }
                    }
                    return null; // 다른 타입 처리
                })
                .filter(id -> id != null) // null 필터링
                .collect(Collectors.toSet()); // Set<Long>으로 수집
    }
}