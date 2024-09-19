package mini.backend.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor

public class SignupRequest {
    private String id;
    private String password;
    private String name;
}