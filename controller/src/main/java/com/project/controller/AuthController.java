package com.project.controller;

import com.project.dao.ProfileRepository;
import com.project.dto.AuthenticationRequestDto;
import com.project.dto.RegistrationDto;
import com.project.dto.ResponseDto;
import com.project.entity.Profile;
import com.project.entity.User;
import com.project.jwt.JwtTokenProvider;
import com.project.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final IUserService userService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, IUserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@RequestBody AuthenticationRequestDto requestDto) {

        String username = requestDto.getUsername();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
        User user = userService.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + " not found"));

        String token = jwtTokenProvider.createToken(username, user.getRoles());

        ResponseDto response = new ResponseDto();
        response.setToken(token);
        response.setUsername(username);

        return ResponseEntity.ok(response);
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
        return ResponseEntity.noContent().build();
    }

}
