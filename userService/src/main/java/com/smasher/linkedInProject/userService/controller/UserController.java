package com.smasher.linkedInProject.userService.controller;

import com.smasher.linkedInProject.userService.dto.LoginRequestDto;
import com.smasher.linkedInProject.userService.dto.SignUpRequestDto;
import com.smasher.linkedInProject.userService.dto.UserDto;
import com.smasher.linkedInProject.userService.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;

    @PostMapping("/signUp")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        UserDto userDto = authService.signUp(signUpRequestDto);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto) {
        String token = authService.login(loginRequestDto);
        return ResponseEntity.ok(token);
    }
}
