package mini.backend.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mini.backend.auth.AuthenticationFacade;
import mini.backend.domain.UserStatus;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;
    private final AuthenticationFacade authenticationFacade;


    @PostMapping("/users")
    public ResponseEntity<?> registerUser(@RequestBody UserDtoReq userDtoReq) {
        userService.registerUser(userDtoReq);
        return ResponseEntity.ok("User registered successfully!");
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDtoRes>> getAllUsers() {
        List<UserDtoRes> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }


    @PatchMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@RequestBody UserDtoReq userDtoReq) {
        String id = authenticationFacade.getAuthentication();
        userService.updateUser(id, userDtoReq);
        return ResponseEntity.ok("User updated successfully!!");
    }

    @PatchMapping("/users/{id}/status")
    public ResponseEntity<?> updateUserStatus(@RequestBody UserDtoReq userDtoReq) {
        String id = authenticationFacade.getAuthentication();
        UserStatus status = userDtoReq.getStatus();  // JSON에서 status 값 추출
        userService.updateUserStatus(id, status);  // 서비스 호출
        return ResponseEntity.ok("User Status updated successfully!!");
    }
}
