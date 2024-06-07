package com.phenikaa.jobhuntly.auth;

import com.phenikaa.jobhuntly.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser user = userRepository
                .findByUsername(username)
                .map(AuthUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("User name not found: " + username));

        return user;

    }
}
