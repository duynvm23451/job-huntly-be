package com.phenikaa.jobhuntly.service;

import com.phenikaa.jobhuntly.auth.AuthUser;
import com.phenikaa.jobhuntly.dto.UserDTO;
import com.phenikaa.jobhuntly.entity.User;
import com.phenikaa.jobhuntly.enums.Gender;
import com.phenikaa.jobhuntly.exception.ObjectNotFoundException;
import com.phenikaa.jobhuntly.mapper.UserMapper;
import com.phenikaa.jobhuntly.repository.UserRepository;
import com.phenikaa.jobhuntly.s3.S3ImageUploader;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final S3ImageUploader s3ImageUploader;

    private final UserMapper userMapper;
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

    public UserDTO.UserResponse update(UserDTO.UserRequest userRequest, Integer userId) throws ParseException {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("Người dùng", userId)
        );

        String avatar = s3ImageUploader.upload(userRequest.avatarFileName());
        user.setAvatarFileName(avatar);
        String cover = s3ImageUploader.upload(userRequest.coverFileName());
        user.setCoverFileName(cover);
        String resume = s3ImageUploader.upload(userRequest.resumeFileName());
        user.setResumeFileName(resume);
        user.setAddress(userRequest.address().split(",")[0]);
        user.setFullName(userRequest.fullName().split(",")[0]);
        user.setPhoneNumber(userRequest.phoneNumber().split(",")[0]);
        user.setGender(Gender.valueOf(userRequest.gender().split(",")[0]));
        String date = userRequest.dateOfBirth().split(",")[0];
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = formatter.parse(date);
        user.setDateOfBirth(date1);
        user.setAboutMe(userRequest.aboutMe().split(",")[0]);

        return userMapper.toUserResponse(userRepository.save(user));
    }
}
