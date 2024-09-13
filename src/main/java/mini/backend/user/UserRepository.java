package mini.backend.user;

import io.lettuce.core.dynamic.annotation.Param;
import mini.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findById(String id);

    boolean existsById(String id);

}