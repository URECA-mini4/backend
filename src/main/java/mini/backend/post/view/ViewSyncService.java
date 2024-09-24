package mini.backend.post.view;

public interface ViewSyncService {
    void syncHits();
    void updateHitCountInMysql(Long postId, Long hitCount);
}
