package com.phenikaa.jobhuntly.service;

import com.phenikaa.jobhuntly.auth.AuthUser;
import com.phenikaa.jobhuntly.entity.User;
import com.phenikaa.jobhuntly.exception.ObjectNotFoundException;
import com.phenikaa.jobhuntly.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User getUser(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(
                () -> new ObjectNotFoundException("Người dùng", "email", email)
        );
    }

    public User getMyInfo(Principal principal) {
        return getUser(principal.getName());
    }
}
