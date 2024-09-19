package mini.backend;

import mini.backend.auth.AuthDtoRes;
import mini.backend.auth.JwtUtil;
import mini.backend.auth.AuthenticationService;
import mini.backend.user.MyUserDetailsService;
import org.json.JSONException;
import org.json.JSONObject;
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
        String username = "User123";
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
        String userId = "User123";
        String role = "USER";
        Long expirationMs = 1000L * 60 * 60; // 1 hour
        String refreshToken = jwtUtil.createJwt(userId, role, expirationMs * 24 * 7); // 7 days

        // JSON 형식으로 감싸기
        String requestBody = null;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("refreshToken", refreshToken);
            requestBody = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to create JSON request body");
        }

        // When
        AuthDtoRes response = authenticationService.refreshToken(requestBody.toString());

        // Then
        assertNotNull(response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());
        assertFalse(jwtUtil.isExpired(response.getAccessToken()));
        
    }

    public String extractTokenFromJson(String json, String tokenField) {
        // JSON 형식으로 감싸진 경우, 실제 토큰 문자열만 추출
        try {
            JSONObject jsonObject = new JSONObject(json);
            return jsonObject.getString(tokenField);
        } catch (JSONException e) {
            // JSON 형식이 아닌 경우, 그대로 반환
            return json;
        }
    }

}