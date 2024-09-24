package mini.backend.user;

import lombok.RequiredArgsConstructor;
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
    public void registerAdmin(UserDtoReq userDtoReq) {
        if (userRepository.existsById(userDtoReq.getId())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
        String encodedPassword = passwordEncoder.encode(userDtoReq.getPassword());

        User newUser = User.builder()
                .id(userDtoReq.getId())
                .password(encodedPassword)
                .name(userDtoReq.getName())
                .role(UserRole.ADMIN)
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
    public void updateUserStatus(String username, String id, UserStatus status) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User를 찾을 수 없습니다!!"));
        UserRole role = userRepository.findById(username)
                .map(User::getRole)
                .orElseThrow(() -> new UsernameNotFoundException("관리자를 찾을 수 없습니다!!"));

        // 사용자 직접 업데이트는 불가하므로, 관리자일 경우에만 업데이트
        if (!user.getStatus().equals(status) && role.equals(UserRole.ADMIN)) {
            user.setStatus(status);
        }
    }

    @Transactional
    public void updateUserRole(String username, String id) { // 회원가입 후 관리자로 역할변경

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User를 찾을 수 없습니다!!"));
        UserRole role = userRepository.findById(username)
                .map(User::getRole)
                .orElseThrow(() -> new UsernameNotFoundException("관리자를 찾을 수 없습니다!!"));

        // 역시나 관리자일때만 업데이트 해야하므로 체크.
        if (!user.getRole().equals(UserRole.ADMIN) && role.equals(UserRole.ADMIN)) {
            user.setRole(UserRole.ADMIN);
        }
    }

    public List<UserDtoRes> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserDtoRes::new)  // User를 UserDto로 변환
                .collect(Collectors.toList());
    }
}
