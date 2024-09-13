package mini.backend.auth;

import mini.backend.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacadeImpl implements AuthenticationFacade {

    @Autowired
    private UserRepository userRepository;
    @Override
    public Long getAuthentication(){
        String username = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userRepository.findUserIdByUsername(username);
    }
}
