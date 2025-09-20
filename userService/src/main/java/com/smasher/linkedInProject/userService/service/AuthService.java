package com.smasher.linkedInProject.userService.service;

import com.smasher.linkedInProject.userService.dto.LoginRequestDto;
import com.smasher.linkedInProject.userService.dto.SignUpRequestDto;
import com.smasher.linkedInProject.userService.dto.UserDto;
import com.smasher.linkedInProject.userService.entity.User;
import com.smasher.linkedInProject.userService.exception.BadRequestException;
import com.smasher.linkedInProject.userService.exception.ResourceNotFoundException;
import com.smasher.linkedInProject.userService.repository.UserRepository;
import com.smasher.linkedInProject.userService.util.BCrypt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;

    public UserDto signUp(SignUpRequestDto signUpRequestDto) {
        log.info("Signup a user with email : {}", signUpRequestDto.getEmail());
        boolean exists = userRepository.existsByEmail(signUpRequestDto.getEmail());
        if(exists) {
            throw new BadRequestException("User already exists with email : "+signUpRequestDto.getEmail());
        }
        User user = modelMapper.map(signUpRequestDto, User.class);
        user.setPassword(BCrypt.hash(signUpRequestDto.getPassword()));
        user = userRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    public String login(LoginRequestDto loginRequestDto) {
        log.info("Loging request for user with email : {}", loginRequestDto.getEmail());
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new BadRequestException("Incorrect email or Password"));
        boolean isPasswordMatch = BCrypt.match(loginRequestDto.getPassword(), user.getPassword());

        if(!isPasswordMatch) {
            throw new BadRequestException("Incorrect email or Password");
        }

        return jwtService.generateAccessToken(user);
    }
}
