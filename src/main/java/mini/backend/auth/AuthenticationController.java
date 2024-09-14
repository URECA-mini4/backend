package mini.backend.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AuthenticationFacade authenticationFacade;

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
