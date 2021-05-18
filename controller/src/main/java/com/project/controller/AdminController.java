package com.project.controller;

import com.project.dto.RegistrationDto;
import com.project.entity.Profile;
import com.project.entity.User;
import com.project.service.IAdvertService;
import com.project.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final IUserService userService;
    private final IAdvertService advertService;

    @Autowired
    public AdminController(IUserService userService, IAdvertService advertService) {
        this.userService = userService;
        this.advertService = advertService;
    }

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


        userService.registerAdminUser(user);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/premuim/{id}")
    public ResponseEntity<Void> enablePremiumAdvert(@PathVariable Long id) {

        advertService.enablePremiumStatus(id);

        return ResponseEntity.noContent().build();
    }

}
