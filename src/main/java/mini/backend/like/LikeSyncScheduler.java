package mini.backend.like;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mini.backend.post.Post;
import mini.backend.user.User;
import mini.backend.post.PostRepository;
import mini.backend.user.UserRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LikeSyncScheduler {

    private final RedisTemplate<String, Object> redisTemplate;
    private final LikeRepository likeRepository;
    private final LikeCountRepository likeCountRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 임시 주석 처리
    // @Scheduled(fixedRate = 10000) // 10초마다 동기화 실행
    @Transactional
    public void syncLikesToDB() {
        Set<String> postIds = redisTemplate.keys("likes:*"); // Redis에서 모든 게시물 ID 가져오기
        if (postIds != null) {
            for (String postId : postIds) {
                Long postIdLong = Long.parseLong(postId.split(":")[1]); // 게시물 ID 추출
                String likeKey = "likes:" + postIdLong;
                Set<Long> redisUserIds = redisTemplate.opsForSet().members(likeKey)
                        .stream()
                        .filter(value -> value instanceof String) // String 타입만 필터링
                        .map(value -> Long.valueOf((String) value)) // Long으로 변환
                        .collect(Collectors.toSet());

                Post post = postRepository.findById(postIdLong)
                        .orElseThrow(() -> new RuntimeException("Post not found. Post ID: " + postIdLong));
                // MySQL에서 해당 게시물에 대한 좋아요한 사용자 정보를 가져옴
                Set<Long> myUserIds = likeRepository.findByPost(post)
                        .stream()
                        .map(like -> like.getUser().getUserId())
                        .collect(Collectors.toSet());
                // Redis에 있는 유저 ID 중 MySQL에 없는 경우 좋아요 추가
                addLikesFromRedis(redisUserIds, myUserIds, postIdLong, post);
                // MySQL에 있는 유저 ID 중 Redis에 없는 경우 좋아요 삭제
                removeLikesFromMySQL(redisUserIds, myUserIds, postIdLong, post);
            }
        }
        // MySQL에 있는 게시글 ID 중 Redis에 없는 경우 좋아요 삭제
        removeLikesNotInRedis(postIds);
    }

    private void removeLikesNotInRedis(Set<String> postIds) {
        Set<Long> allPostIds = likeRepository.findAll()
                .stream()
                .map(like -> like.getPost().getPostId())
                .collect(Collectors.toSet());


        for (Long postIdLong : allPostIds) {
            if (!postIds.contains("likes:" + postIdLong)) {
                // Redis에 포스트 ID가 없는 경우 MySQL에서 해당 포스트에 대한 좋아요 삭제
                Post post = postRepository.findById(postIdLong)
                        .orElseThrow(() -> new RuntimeException("Post not found. Post ID: " + postIdLong));

                // 좋아요 데이터 삭제
                likeRepository.deleteByPost(post);

                // 좋아요 카운트 로직
                LikeCount likeCount = likeCountRepository.findByPostId(postIdLong)
                        .orElseThrow(() -> new RuntimeException("LikeCount not found for Post ID: " + postIdLong));
                likeCount.setCount(0); // 좋아요 카운트를 0으로 초기화
                likeCountRepository.save(likeCount);
            }
        }
    }

    private void removeLikesFromMySQL(Set<Long> redisUserIds, Set<Long> myUserIds, Long postIdLong, Post post) {
        for (Long checkUserId : myUserIds) {
            if (!redisUserIds.contains(checkUserId)) {
                User user = userRepository.findByUserId(checkUserId)
                        .orElseThrow(() -> new IllegalArgumentException("User not found. User ID: " + checkUserId));
                Like like = likeRepository.findByUserAndPost(user, post)
                        .orElseThrow(() -> new RuntimeException("Like not found for User ID: " + checkUserId + " on Post ID: " + postIdLong));

                // 좋아요 데이터 삭제
                likeRepository.delete(like);

                // 좋아요 카운트 로직
                LikeCount likeCount = likeCountRepository.findByPostId(postIdLong)
                        .orElseThrow(() -> new RuntimeException("LikeCount not found for Post ID: " + postIdLong));
                likeCount.setCount(likeCount.getCount() - 1);
                likeCountRepository.save(likeCount);
            }
        }
    }

    private void addLikesFromRedis(Set<Long> redisUserIds, Set<Long> myUserIds, Long postIdLong, Post post) {
        for (Long checkUserId : redisUserIds) {
            if (!myUserIds.contains(checkUserId)) {
                User user = userRepository.findByUserId(checkUserId)
                        .orElseThrow(() -> new IllegalArgumentException("User not found. User ID: " + checkUserId));
                // 새로운 좋아요 데이터 저장
                Like newLike = new Like();
                newLike.setPost(post);
                newLike.setUser(user);
                likeRepository.save(newLike);

                // 좋아요 카운트 증가
                LikeCount likeCount = likeCountRepository.findByPostId(postIdLong)
                        .orElseGet(() -> {
                            LikeCount newLikeCount = LikeCount.builder()
                                    .post(post)
                                    .count(0)
                                    .build();
                            likeCountRepository.save(newLikeCount); // 새로 생성된 객체 저장a
                            return newLikeCount;
                        });
                likeCount.setCount(likeCount.getCount() + 1);
                likeCountRepository.save(likeCount);
            }
        }
    }
}
