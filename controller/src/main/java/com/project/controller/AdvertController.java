package com.project.controller;

import com.project.dto.*;
import com.project.service.IAdvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/adverts")
public class AdvertController {

    private final IAdvertService advertService;

    @Autowired
    public AdvertController(IAdvertService advertService) {
        this.advertService = advertService;
    }

    @PreAuthorize("#createAdvertDto.username == authentication.principal.username")
    @PostMapping
    public ResponseEntity<Void> createAdvert(@RequestBody @Valid CreateAdvertDto createAdvertDto) {

        advertService.createAdvert(createAdvertDto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("#updateAdvertDto.username == authentication.principal.username or hasRole('ROLE_ADMIN')")
    @PatchMapping
    public ResponseEntity<Void> updateAdvert(@RequestBody UpdateAdvertDto updateAdvertDto) {

        advertService.updateAdvert(updateAdvertDto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("#deleteAdvertDto.username == authentication.principal.username or hasRole('ROLE_ADMIN')")
    @DeleteMapping
    public ResponseEntity<DeleteAdvertDto> deleteAdvert(@RequestBody @Valid DeleteAdvertDto deleteAdvertDto) {

        advertService.deleteAdvert(deleteAdvertDto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/comment")
    public ResponseEntity<Void> addComment(@Valid @RequestBody CommentaryDto commentaryDto){

        advertService.addCommentaryToAdvert(commentaryDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<AdvertDto>> getAdverts(@RequestParam String category,
                                                      @RequestParam Integer size,
                                                      @RequestParam Integer pageNumber)
    {
        AdvertListDto advertListDto = new AdvertListDto();
        advertListDto.setCategories(List.of(category));
        advertListDto.setPageSize(size);
        advertListDto.setPageNumber(pageNumber);

        Page<AdvertDto> adverts = advertService.getAdverts(advertListDto);

        return new ResponseEntity<>(adverts, HttpStatus.OK);
    }

}