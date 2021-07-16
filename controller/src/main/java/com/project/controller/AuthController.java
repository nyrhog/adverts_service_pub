package com.project.controller;


import com.project.dto.*;
import com.project.entity.Profile;
import com.project.entity.User;
import com.project.jwt.JwtTokenProvider;
import com.project.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final IUserService userService;


    @PostMapping("/login")
    public ResponseEntity<GlobalResponseDto> login(@Valid @RequestBody AuthenticationRequestDto requestDto) {

        String username = requestDto.getUsername();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
        User user = userService.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + " not found"));

        String token = jwtTokenProvider.createToken(username, user.getRoles());

        ResponseDto response = new ResponseDto();
        response.setToken(token);
        response.setUsername(username);

        GlobalResponseDto responseDto = new GlobalResponseDto(response);

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/registration")
    public ResponseEntity<Void> registration(@Valid @RequestBody RegistrationDto requestDto) {
        User user = new User();

        Profile profile = new Profile()
                .setName(requestDto.getFirstName())
                .setPhoneNumber(requestDto.getPhoneNumber())
                .setSurname(requestDto.getSurname());

        user.setPassword(requestDto.getPassword())
                .setUsername(requestDto.getUsername())
                .setEmail(requestDto.getEmail())
                .setProfile(profile);


        userService.register(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/restore-password")
    public ResponseEntity<Void> sendMessage(@RequestParam String username){

        userService.sendMessageWithCode(username);
        return ResponseEntity.ok().build();
    }



    @PatchMapping("/restore-password")
    public ResponseEntity<Void> restorePassword(@RequestBody RestorePasswordDto dto){

        String username = dto.getUsername();
        int code = dto.getCode();
        String newPassword = dto.getNewPassword();

        userService.restorePassword(username, code, newPassword);

        return ResponseEntity.ok().build();
    }

}
