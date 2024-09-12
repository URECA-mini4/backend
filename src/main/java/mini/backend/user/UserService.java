package mini.backend.user;

import mini.backend.domain.User;
import mini.backend.domain.UserRole;
import mini.backend.domain.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(SignupRequest signupRequest) {
        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

        User newUser = new User();
        newUser.setId(signupRequest.getId());
        newUser.setPassword(encodedPassword);
        newUser.setName(signupRequest.getName());
        newUser.setRole(UserRole.USER);
        newUser.setStatus(UserStatus.ACTIVED);

        userRepository.save(newUser);
    }
}
