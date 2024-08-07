package com.phenikaa.jobhuntly.auth;

import com.phenikaa.jobhuntly.dto.ExchangeTokenDTO;
import com.phenikaa.jobhuntly.dto.OutboundUserResponse;
import com.phenikaa.jobhuntly.entity.User;
import com.phenikaa.jobhuntly.enums.Role;
import com.phenikaa.jobhuntly.mapper.AuthMapper;
import com.phenikaa.jobhuntly.repository.OutboundClient;
import com.phenikaa.jobhuntly.repository.OutboundUserClient;
import com.phenikaa.jobhuntly.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;
    private final OutboundClient outboundClient;
    private final OutboundUserClient outboundUserClient;

    @Value("${outbound.job-huntly.client-id}")
    private String CLIENT_ID;

    @Value("${outbound.job-huntly.client-secret}")
    private String CLIENT_SECRET;

    @Value("${outbound.job-huntly.redirect-uri}")
    private String REDIRECT_URI;

    @Value("${outbound.job-huntly.grant-type}")
    private String GRANT_TYPE;

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

    public User register(AuthDTO.RegisterRequest request) {
        User user = authMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.valueOf(request.role()));
        return userRepository.save(user);
    }

    public Map<String, Object> outboundAuthenticate(String code, String role) {

        ExchangeTokenDTO.ExchangeTokenResponse response = outboundClient.exchangeToken(
                ExchangeTokenDTO.ExchangeTokenRequest.builder()
                        .code(code)
                        .clientId(CLIENT_ID)
                        .clientSecret(CLIENT_SECRET)
                        .redirectUri(REDIRECT_URI)
                        .grantType(GRANT_TYPE)
                        .build()
        );
        System.out.println("Duy: " + response);

        OutboundUserResponse userInfo = outboundUserClient.getUserInfo("json", response.accessToken());
        User user = userRepository.findUserByEmail(userInfo.email()).orElseGet(
                () -> userRepository.save(User.builder()
                        .email(userInfo.email())
                        .username(userInfo.familyName() + " " + userInfo.givenName())
                        .role(Role.valueOf(role))
                        .isEnable(true)
                        .build())
        );
        String token = jwtProvider.generateToken(user);
        System.out.println("Duy: " + token);

        AuthDTO.UserLoginResponse userLoginResponse = authMapper.toUserLoginResponse(user);

        Map<String, Object> authenticateResultMap = new HashMap<>();
        authenticateResultMap.put("user", userLoginResponse);
        authenticateResultMap.put("token", token);

        return authenticateResultMap;
    }

}
