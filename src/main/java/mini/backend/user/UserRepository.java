package mini.backend.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findById(String id);

    boolean existsById(String id);

    Optional<User> findByUserId(Long userId);
}