package mini.backend.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mini.backend.auth.AuthenticationFacade;

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
        String username = authenticationFacade.getAuthentication();
        String id = userDtoReq.getId();
        UserStatus status = userDtoReq.getStatus();  // JSON에서 status 값 추출
        userService.updateUserStatus(username, id, status);  // 서비스 호출
        return ResponseEntity.ok("User Status updated successfully!!");
    }

    @PostMapping("/users/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody UserDtoReq userDtoReq) {
        userService.registerAdmin(userDtoReq);
        return ResponseEntity.ok("Admin registered successfully!");
    }

    @PatchMapping("/users/{id}/role") // 관리자로 변경하기
    public ResponseEntity<?> updateUserRole(@RequestBody UserDtoReq userDtoReq) {
        String username = authenticationFacade.getAuthentication();
        String id = userDtoReq.getId();
        userService.updateUserRole(username, id);  // 서비스 호출
        return ResponseEntity.ok("User Status updated successfully!!");
    }

}
