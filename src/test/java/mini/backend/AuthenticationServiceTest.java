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
        String username = "testUser";
        String role = "USER";
        Long expirationMs = 1000L * 60 * 60; // 1 hour

        // Create JWT
        String token = jwtUtil.createJwt(username, role, expirationMs);

        // Validate JWT
        assertNotNull(token);
        assertEquals(username, jwtUtil.getLoginId(token));
        assertEquals(role, jwtUtil.getRole(token));
        assertFalse(jwtUtil.isExpired(token));
    }

    @Test
    public void testRefreshToken() {
        // Given
        String userId = "testUser";
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
