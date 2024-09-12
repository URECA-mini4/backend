package mini.backend;

import mini.backend.auth.AuthenticationResponse;
import mini.backend.auth.JwtUtil;
import mini.backend.auth.AuthenticationService;
import mini.backend.user.MyUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Test
    public void testJwtCreationAndValidation() {
        String userId = "1";
        String role = "USER";
        Long expirationMs = 1000L * 60 * 60; // 1 hour

        // Create JWT
        String token = jwtUtil.createJwt(userId, role, expirationMs);

        // Validate JWT
        assertNotNull(token);
        assertEquals(userId, jwtUtil.getUserId(token));
        assertEquals(role, jwtUtil.getRole(token));
        assertFalse(jwtUtil.isExpired(token));
    }

    @Test
    public void testRefreshToken() {
        // Given
        String userId = "1";
        String role = "USER";
        Long expirationMs = 1000L * 60 * 60; // 1 hour
        String refreshToken = jwtUtil.createJwt(userId, role, expirationMs * 24 * 7); // 7 days

        // When
        AuthenticationResponse response = authenticationService.refreshToken(refreshToken);

        // Then
        assertNotNull(response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());
        assertFalse(jwtUtil.isExpired(response.getAccessToken()));
    }
}
