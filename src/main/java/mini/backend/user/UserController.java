package mini.backend.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;


    @PostMapping("/users")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        userService.registerUser(signupRequest);
        return ResponseEntity.ok("User registered successfully!");
    }
}
