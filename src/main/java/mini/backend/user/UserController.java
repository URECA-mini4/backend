package mini.backend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId =  (String)authentication.getPrincipal();

    @PostMapping("/users")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        userService.registerUser(signupRequest);
        return ResponseEntity.ok("User registered successfully!");
    }
}
