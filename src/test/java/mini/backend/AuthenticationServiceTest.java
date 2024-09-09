package mini.backend;

import mini.backend.model.AuthenticationRequest;
import mini.backend.model.AuthenticationResponse;
import mini.backend.security.JwtUtil;
import mini.backend.service.AuthenticationService;
import mini.backend.service.MyUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

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
        String username = "user123";
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
        String username = "user123";
        String role = "USER";
        Long expirationMs = 1000L * 60 * 60; // 1 hour
        String refreshToken = jwtUtil.createJwt(username, role, expirationMs * 24 * 7); // 7 days

        // When
        AuthenticationResponse response = authenticationService.refreshToken(refreshToken);

        // Then
        assertNotNull(response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());
        assertFalse(jwtUtil.isExpired(response.getAccessToken()));
    }
}
