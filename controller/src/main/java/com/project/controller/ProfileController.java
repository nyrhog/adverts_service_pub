package com.project.controller;

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
   @PreAuthorize("#updateDto.username == authentication.principal.username or hasRole('ROLE_ADMIN')")
   public ResponseEntity<Void> updateProfile(ProfileUpdateDto updateDto){

       profileService.updateProfile(updateDto);

       return ResponseEntity.noContent().build();
   }

   @PutMapping("/rate")
   @PreAuthorize("#rateDto.username == authentication.principal.username")
    public ResponseEntity<Void> rateProfile(@Valid @RequestBody RateDto rateDto){
       profileService.rateProfile(rateDto);

       return ResponseEntity.noContent().build();
   }

}
