package mini.backend.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.json.JSONObject;
import org.json.JSONException;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${spring.jwt.secret}")
    private String secretkey;
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        byte[] keyBytes = secretkey.getBytes(StandardCharsets.UTF_8);
        this.secretKey = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    public String getLoginId(String token) {
        return getClaims(token, null).get("loginId", String.class);
    }

    public String getRole(String token) {
        return getClaims(token, null).get("role", String.class);
    }

    public Boolean isExpired(String token) {
        return getClaims(token, null).getExpiration().before(new Date());
    }

    public String createJwt(String loginId, String role, Long expiredMs) {
        String token = Jwts.builder()
                .claim("loginId", loginId)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        System.out.println("Generated JWT Token: " + token);
        return token;
    }

    public Claims getClaims(String token, String tokenField) {
        // JSON 형식으로 감싸진 경우, 실제 토큰 문자열만 추출
        if (token.startsWith("{") && token.endsWith("}")) {
            token = extractTokenFromJson(token, tokenField);
        }

        System.out.println("JWT Token to decode: " + token);

        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // JSON에서 JWT 토큰 문자열 추출 (필드명 파라미터화)
    public String extractTokenFromJson(String json, String tokenField) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return jsonObject.getString(tokenField);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to extract JWT token from JSON", e);
        }
    }
}
