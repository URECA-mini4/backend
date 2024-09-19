package mini.backend.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDtoRes {
    private final Long userId;
    private final String id;
    private final String name;
}
