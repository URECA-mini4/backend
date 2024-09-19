package mini.backend.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class AuthDtoRes {
    private String accessToken;
    private String refreshToken;
}