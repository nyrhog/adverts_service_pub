package com.project.controller;

import com.project.dto.ProfileDto;
import com.project.dto.ProfileUpdateDto;
import com.project.dto.RateDto;
import com.project.service.IProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profiles")
public class ProfileController {

    private final IProfileService profileService;

    @PatchMapping
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> updateProfile(@Valid @RequestBody ProfileUpdateDto updateDto) {

        profileService.updateProfile(updateDto);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/rate")
    public ResponseEntity<Void> rateProfile(@Valid @RequestBody RateDto rateDto) {

        profileService.rateProfile(rateDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileDto> getProfile(@PathVariable Long id){

        ProfileDto profile = profileService.getProfile(id);
        return ResponseEntity.ok(profile);

    }
}
