package mini.backend.user;

import lombok.RequiredArgsConstructor;
import mini.backend.domain.User;
import mini.backend.domain.UserRole;
import mini.backend.domain.UserStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerUser(UserDtoReq userDtoReq) {
        if (userRepository.existsById(userDtoReq.getId())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
        String encodedPassword = passwordEncoder.encode(userDtoReq.getPassword());

        User newUser = User.builder()
                .id(userDtoReq.getId())
                .password(encodedPassword)
                .name(userDtoReq.getName())
                .role(UserRole.USER)
                .status(UserStatus.ACTIVED)
                .build();

        userRepository.save(newUser);  // 새로 생성한 유저는 영속성 컨텍스트에 저장되기 때문에 수동으로 호출
    }

    @Transactional
    public void updateUser(String id, UserDtoReq userDtoReq) {

        // 기존 사용자 정보 조회
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User를 찾을 수 없습니다!!"));

        if (userDtoReq.getName() != null && !userDtoReq.getName().equals(user.getName())) {
            user.setName(userDtoReq.getName());
        }
        if (userDtoReq.getPassword() != null && !userDtoReq.getPassword().equals(user.getPassword())) {
            String encodedpassword = passwordEncoder.encode(userDtoReq.getPassword());
            user.setPassword(encodedpassword);
        }

    }

    @Transactional
    public void updateUserStatus(String id, UserStatus status) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User를 찾을 수 없습니다!!"));


        // 상태가 다를 경우에만 업데이트
        if (!user.getStatus().equals(status)) {
            user.setStatus(status);
        }
    }

    public List<UserDtoRes> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserDtoRes::new)  // User를 UserDto로 변환
                .collect(Collectors.toList());
    }
}
