package mini.backend.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final AuthenticationFacade authenticationFacade;

    // 생성자 주입
    public AuthenticationController(AuthenticationService authenticationService, AuthenticationFacade authenticationFacade) {
        this.authenticationService = authenticationService;
        this.authenticationFacade = authenticationFacade;
    }

    @PostMapping("/users/login")
    public ResponseEntity<?> login(@RequestBody AuthDtoReq authDtoReq) throws Exception {
        AuthDtoRes response = authenticationService.authenticate(authDtoReq);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/users/logout")
    public ResponseEntity<?> logout(@RequestBody String tokens) {
        authenticationService.logout(tokens);
        return ResponseEntity.ok("Successfully logged out");
    }

    @PostMapping("/users/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody String refreshToken) {
        AuthDtoRes response = authenticationService.refreshToken(refreshToken);
        return ResponseEntity.ok(response);
    }
}
