package dev.terfehr.gymtrackerapi.service;

import dev.terfehr.gymtrackerapi.repository.UserRepositoryI;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@NullMarked
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepositoryI userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("There is no user with username: " + username));
    }
}
