package mini.backend.user;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import mini.backend.domain.UserStatus;

@Data
@RequiredArgsConstructor
public class UserDtoReq {
    private String id;
    private String password;
    private String name;
    private UserStatus status;
}