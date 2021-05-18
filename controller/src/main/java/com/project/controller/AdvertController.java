package com.project.controller;

import com.project.dto.CommentaryDto;
import com.project.dto.CreateAdvertDto;
import com.project.dto.UpdateAdvertDto;
import com.project.service.IAdvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/adverts")
public class AdvertController {

    private final IAdvertService advertService;

    @Autowired
    public AdvertController(IAdvertService advertService) {
        this.advertService = advertService;
    }

    @PostMapping
    public ResponseEntity<Void> createAdvert(@RequestBody @Valid CreateAdvertDto createAdvertDto) {

        advertService.createAdvert(createAdvertDto);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping
    public ResponseEntity<Void> updateAdvert(@RequestBody UpdateAdvertDto updateAdvertDto) {

        advertService.updateAdvert(updateAdvertDto);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdvert(@PathVariable Long id) {

        advertService.deleteAdvert(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/comment")
    public ResponseEntity<Void> addComment(@Valid @RequestBody CommentaryDto commentaryDto){

        advertService.addCommentaryToAdvert(commentaryDto);
        return ResponseEntity.noContent().build();
    }

}
