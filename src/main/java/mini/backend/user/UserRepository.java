package mini.backend.user;

import io.lettuce.core.dynamic.annotation.Param;
import mini.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(Long userId);

    Optional<User> findById(String id);

    @Query("SELECT u.userId FROM User u WHERE u.id = :username")
    Long findUserIdByUsername(@Param("username") String username);

}