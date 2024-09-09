package mini.backend.service;

import mini.backend.model.AuthenticationRequest;
import mini.backend.model.AuthenticationResponse;
import mini.backend.security.JwtUtil;
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

    public void logout(String token) {
        Long expiration = jwtUtil.getClaims(token).getExpiration().getTime() - System.currentTimeMillis();
        redisTemplate.opsForValue().set(token, true, expiration, TimeUnit.MILLISECONDS);
    }

    public AuthenticationResponse refreshToken(String refreshToken) {
        if (jwtUtil.isExpired(refreshToken)) {
            throw new RuntimeException("Refresh token is expired");
        }

        String username = jwtUtil.getLoginId(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String newAccessToken = jwtUtil.createJwt(userDetails.getUsername(), userDetails.getAuthorities().toString(), 1000L * 60 * 60);

        return new AuthenticationResponse(newAccessToken, refreshToken);
    }
}
