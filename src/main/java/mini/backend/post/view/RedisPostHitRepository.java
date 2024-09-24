package mini.backend.post.view;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import mini.backend.post.Post;
import mini.backend.post.PostRepository;
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

    private final PostRepository postRepository; // PostRepository 추가

    @PostConstruct
    public void setUp() {
        valueOperations = redisTemplate.opsForValue();
        setOperations = redisTemplate.opsForSet();
    }

    private static final String POST_HIT_KEY = "post:hit:";
    private static final String POST_IDS_KEY = "post:ids"; // 업데이트된 모든 포스트 아이디들
    private final Map<Long, Long> postIdTimeMap = new HashMap<>(); // postId와 추가 시간을 저장

    @Override
    public void incrementHit(Long postId) {
        // Set에 postId 추가
        setOperations.add(POST_IDS_KEY, postId);
        // 현재 시간을 저장
        postIdTimeMap.put(postId, System.currentTimeMillis());

        // 처음 추가될 때 DB에서 조회수 가져오기
        if (!valueOperations.getOperations().hasKey(POST_HIT_KEY + postId)) {
            Long initialHitCount = postRepository.findPostViewByPostId(postId); // DB에서 조회수 가져오기
            if (initialHitCount != null) {
                valueOperations.set(POST_HIT_KEY + postId, initialHitCount); // Redis에 초기화
            } else {
                valueOperations.set(POST_HIT_KEY + postId, 0L); // DB에 값이 없으면 0으로 초기화
            }
        }

        // Set의 크기가 5를 넘으면 가장 오래된 포스트 제거
        if (setOperations.size(POST_IDS_KEY) > 5) {
            // 가장 오래된 postId 찾기
            Long oldestPostId = Collections.min(postIdTimeMap.entrySet(), Map.Entry.comparingByValue()).getKey();
            //데이터 싱크
            Post post = postRepository.findById(oldestPostId)
                    .orElseThrow(() -> new RuntimeException("Post not found"));
            post.increasePostView(getHit(oldestPostId));
            postRepository.save(post);// 조회 수 업데이트
            // 해당 postId의 hit 키 삭제
            redisTemplate.delete(POST_HIT_KEY + oldestPostId);
            // Set에서 가장 오래된 postId 제거
            setOperations.remove(POST_IDS_KEY, oldestPostId);
            // Map에서도 해당 postId 삭제
            postIdTimeMap.remove(oldestPostId);
        }

        // 게시물 조회수 증가
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
        // Redis에서 모든 멤버를 가져옴
        Set<Object> members = setOperations.members(POST_IDS_KEY);
        if (members == null || members.isEmpty()) {
            return Set.of(); // 빈 집합 반환
        }
        // Set<Object>를 Set<Long>으로 변환
        return members.stream()
                .map(member -> {
                    try {
                        return Long.parseLong(member.toString()); // String으로 변환 후 Long으로 파싱
                    } catch (NumberFormatException e) {
                        // 변환 실패 시 null 반환
                        return null;
                    }
                })
                .filter(Objects::nonNull) // null 필터링
                .collect(Collectors.toSet()); // Set<Long>으로 수집
    }
}