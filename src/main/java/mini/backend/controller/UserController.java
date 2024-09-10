package mini.backend.controller;

import mini.backend.domain.User;
import mini.backend.domain.UserRole;
import mini.backend.domain.UserStatus;
import mini.backend.model.SignupRequest;
import mini.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/users")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

        User newUser = new User();
        newUser.setId(signupRequest.getId());
        newUser.setPassword(encodedPassword);
        newUser.setName(signupRequest.getName());
        newUser.setRole(UserRole.USER);
        newUser.setStatus(UserStatus.ACTIVED);


        userRepository.save(newUser);

        return ResponseEntity.ok("User registered successfully!");
    }
}
