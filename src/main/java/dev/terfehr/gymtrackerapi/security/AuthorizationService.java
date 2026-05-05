package dev.terfehr.gymtrackerapi.security;

import dev.terfehr.gymtrackerapi.model.User;
import org.springframework.stereotype.Service;

@Service("authorizationService")
@SuppressWarnings("unused")
public class AuthorizationService {
    public boolean isVerified(Object user) {
        if (user instanceof User) {
            return ((User) user).isVerified();
        }
        return false;
    }
}
