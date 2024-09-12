package mini.backend.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacadeImpl implements AuthenticationFacade {
    @Override
    public Long getAuthentication(){
        return Long.parseLong((String)SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }
}
