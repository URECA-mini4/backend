package mini.backend.service;

import mini.backend.model.AuthenticationRequest;
import mini.backend.model.AuthenticationResponse;
import mini.backend.security.JwtUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String accessToken = jwtUtil.createJwt(userDetails.getUsername(), userDetails.getAuthorities().toString(), 1000L * 60 * 60);
        final String refreshToken = jwtUtil.createJwt(userDetails.getUsername(), userDetails.getAuthorities().toString(), 1000L * 60 * 60 * 24 * 7);

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    public void logout(String json) {
        try {
            // JSON에서 accessToken 및 refreshToken 추출
            String accessToken = jwtUtil.extractTokenFromJson(json, "accessToken");
            String refreshToken = jwtUtil.extractTokenFromJson(json, "refreshToken");

            // accessToken의 만료 시간 계산
            Long expiration = jwtUtil.getClaims(accessToken, null).getExpiration().getTime() - System.currentTimeMillis();

            // Redis에 accessToken을 블랙리스트로 등록 (expiration 기간 동안)
            redisTemplate.opsForValue().set(accessToken, true, expiration, TimeUnit.MILLISECONDS);

            // Redis에서 refreshToken 삭제
            redisTemplate.delete(refreshToken);

        } catch (Exception e) {
            System.err.println("Invalid JWT token: " + json);
            throw new RuntimeException("Invalid token", e);
        }
    }

    public AuthenticationResponse refreshToken(String json) {
        try {
            // JSON에서 refreshToken 추출
            String refreshToken = jwtUtil.extractTokenFromJson(json, "refreshToken");

            // 토큰 만료 여부 확인
            if (jwtUtil.isExpired(refreshToken)) {
                throw new RuntimeException("Refresh token is expired");
            }

            // 사용자 정보와 새 Access Token 생성
            String username = jwtUtil.getLoginId(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            String newAccessToken = jwtUtil.createJwt(userDetails.getUsername(), userDetails.getAuthorities().toString(), 1000L * 60 * 60);

            return new AuthenticationResponse(newAccessToken, refreshToken);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid refresh token request", e);
        }
    }
}
// 