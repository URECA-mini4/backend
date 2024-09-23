package mini.backend.auth;

import lombok.RequiredArgsConstructor;
import mini.backend.user.MyUserDetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final MyUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final AuthenticationFacade authenticationFacade;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String REDIS_LOGOUT_KEY = "BLACKLISTED_TOKEN:";

    // 생성자 주입

    public AuthDtoRes authenticate(AuthDtoReq authDtoReq) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authDtoReq.getUsername(), authDtoReq.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authDtoReq.getUsername());
        final String accessToken = jwtUtil.createJwt(String.valueOf(userDetails.getUsername()), userDetails.getAuthorities().toString(), 1000L * 60 * 60);

        if (isTokenBlacklisted(accessToken)) {
            throw new Exception("The user is blacklisted. Please contact support.");
        }

        final String refreshToken = jwtUtil.createJwt(String.valueOf(userDetails.getUsername()), userDetails.getAuthorities().toString(), 1000L * 60 * 60 * 24 * 7);

        return new AuthDtoRes(accessToken, refreshToken);
    }

    public void logout(String json) {
        try {
            String accessToken = jwtUtil.extractTokenFromJson(json, "accessToken");
            String refreshToken = jwtUtil.extractTokenFromJson(json, "refreshToken");
            Long expiration = jwtUtil.getClaims(accessToken, null).getExpiration().getTime() - System.currentTimeMillis();

            redisTemplate.opsForValue().set(REDIS_LOGOUT_KEY + accessToken, true, expiration, TimeUnit.MILLISECONDS);

            redisTemplate.delete(refreshToken);
        } catch (Exception e) {
            System.err.println("Invalid JWT token: " + json);
            throw new RuntimeException("Invalid token", e);
        }
    }

    public AuthDtoRes refreshToken(String refreshToken) {
        try {
            String username = jwtUtil.getLoginId(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            String newAccessToken = jwtUtil.createJwt(userDetails.getUsername(), userDetails.getAuthorities().toString(), 1000L * 60 * 60);
            return new AuthDtoRes(newAccessToken, refreshToken);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid refresh token request", e);
        }
    }

    private boolean isTokenBlacklisted(String accessToken) {
        Boolean isBlacklisted = (Boolean) redisTemplate.opsForValue().get(REDIS_LOGOUT_KEY + accessToken);
        return isBlacklisted != null && isBlacklisted;
    }
}
