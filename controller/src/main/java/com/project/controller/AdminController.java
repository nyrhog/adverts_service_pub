package com.project.controller;

import com.project.dto.AdvertPremiumDto;
import com.project.dto.GlobalResponseDto;
import com.project.dto.RegistrationDto;
import com.project.entity.AdvertPremium;
import com.project.entity.Profile;
import com.project.entity.User;
import com.project.service.IAdvertService;
import com.project.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController{

    private final IUserService userService;
    private final IAdvertService advertService;

    @PostMapping("/registration")
    public ResponseEntity<Void> registerAdmin(@Valid @RequestBody RegistrationDto registrationDto){

        User user = new User();

        Profile profile = new Profile()
                .setName(registrationDto.getFirstName())
                .setPhoneNumber(registrationDto.getPhoneNumber())
                .setSurname(registrationDto.getSurname());

        user.setPassword(registrationDto.getPassword())
                .setUsername(registrationDto.getUsername())
                .setEmail(registrationDto.getEmail())
                .setProfile(profile);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/premium/{id}")
    public ResponseEntity<GlobalResponseDto> enablePremiumAdvert(@PathVariable Long id) {

        advertService.enablePremiumStatus(id);
        return ResponseEntity.ok().build() ;
    }
}
