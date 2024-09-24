package mini.backend.dataSync;

public interface DataSyncService {
    void syncHits();
    void updateHitCountInMysql(Long postId, Long hitCount);
}
