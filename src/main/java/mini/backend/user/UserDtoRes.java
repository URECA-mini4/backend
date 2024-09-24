package mini.backend.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class UserDtoRes {
    private final Long userId;
    private final String id;
    private final String name;
    private final UserStatus status;
    private final UserRole role;

    public UserDtoRes(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.userId = user.getUserId();
        this.status = user.getStatus();
        this.role = user.getRole();
    }


}
