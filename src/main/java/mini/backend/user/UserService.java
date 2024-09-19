package mini.backend.user;

import mini.backend.domain.User;
import mini.backend.domain.UserRole;
import mini.backend.domain.UserStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(SignupRequest signupRequest) {
        if (userRepository.existsById(signupRequest.getId())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

        User newUser = User.builder()
                .id(signupRequest.getId())
                .password(encodedPassword)
                .name(signupRequest.getName())
                .role(UserRole.USER)
                .status(UserStatus.ACTIVED)
                .build();

        userRepository.save(newUser);
    }
}
