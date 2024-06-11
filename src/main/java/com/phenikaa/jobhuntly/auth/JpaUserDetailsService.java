package com.phenikaa.jobhuntly.auth;

import com.phenikaa.jobhuntly.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository
                .findUserByEmail(email)
                .map(AuthUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("User name not found: " + email));

    }
}
