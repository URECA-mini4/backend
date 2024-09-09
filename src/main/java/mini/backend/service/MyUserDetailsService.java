package mini.backend.service;

import mini.backend.domain.User;
import mini.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        // 데이터베이스에서 username에 해당하는 사용자 정보 조회
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + id));

        // UserRole을 GrantedAuthority로 변환
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());

        // UserDetails 객체로 변환하여 반환
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getId()) // id를 username으로 사용
                .password(user.getPassword()) // 보통 암호화된 비밀번호
                .authorities(Collections.singletonList(authority))   // 사용자의 권한
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
