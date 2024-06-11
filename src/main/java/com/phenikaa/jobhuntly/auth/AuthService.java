package com.phenikaa.jobhuntly.auth;

import com.phenikaa.jobhuntly.entity.User;
import com.phenikaa.jobhuntly.mapper.AuthMapper;
import com.phenikaa.jobhuntly.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;

    public Map<String, Object> login(AuthDTO.LoginRequest request) {
        Authentication authentication =
                authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(
                                request.email(),
                                request.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        AuthUser userDetails = (AuthUser) authentication.getPrincipal();
        User user = userDetails.getUser();
        AuthDTO.UserLoginResponse userLoginResponse = authMapper.toUserLoginResponse(user);
        String token = jwtProvider.generateToken(authentication);

        Map<String, Object> loginResultMap = new HashMap<>();
        loginResultMap.put("user", userLoginResponse);
        loginResultMap.put("token", token);
        return loginResultMap;
    }

    public void register(AuthDTO.RegisterRequest request) {
        User user = authMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(user);
    }

}
