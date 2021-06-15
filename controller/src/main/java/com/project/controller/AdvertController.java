package com.project.controller;

import com.project.dto.*;
import com.project.service.IAdvertService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class AdvertController {

    private final IAdvertService advertService;

    @PostMapping
    public ResponseEntity<Void> createAdvert(@RequestBody @Valid CreateAdvertDto createAdvertDto) {

        advertService.createAdvert(createAdvertDto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PatchMapping
    public ResponseEntity<Void> updateAdvert(@RequestBody UpdateAdvertDto updateAdvertDto) {

        advertService.updateAdvert(updateAdvertDto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @DeleteMapping
    public ResponseEntity<DeleteAdvertDto> deleteAdvert(@RequestBody @Valid DeleteAdvertDto deleteAdvertDto) {

        advertService.deleteAdvert(deleteAdvertDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/comment")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> updateComment(@Valid @RequestBody EditCommentDto commentDto) {

        advertService.editComment(commentDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/comment")
    public ResponseEntity<Void> addComment(@Valid @RequestBody CommentaryDto commentaryDto) {

        advertService.addCommentaryToAdvert(commentaryDto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/comment")
    public ResponseEntity<Void> deleteComment(@RequestParam Long id) {

        advertService.deleteComment(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<AdvertDto>> getAdverts(@RequestParam String category,
                                                      @RequestParam Integer size,
                                                      @RequestParam Integer pageNumber) {
        Integer realPage = pageNumber - 1;
        AdvertListDto advertListDto = new AdvertListDto();
        advertListDto.setCategories(List.of(category));
        advertListDto.setPageSize(size);
        advertListDto.setPageNumber(realPage);

        Page<AdvertDto> adverts = advertService.getAdverts(advertListDto);

        return new ResponseEntity<>(adverts, HttpStatus.OK);
    }

    @GetMapping("/history")
    public ResponseEntity<Page<AdvertDto>> getAdvertsHistoryOfProfile(@RequestParam Long id,
                                                                      @RequestParam Integer size,
                                                                      @RequestParam Integer page) {
        Integer realPage = page - 1;

        Page<AdvertDto> adverts = advertService.sellingHistory(id, realPage, size);
        return new ResponseEntity<>(adverts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdvertDto> getAdvert(@PathVariable Long id) {
        AdvertDto advert = advertService.getOneAdvert(id);

        return new ResponseEntity<>(advert, HttpStatus.OK);
    }
}
