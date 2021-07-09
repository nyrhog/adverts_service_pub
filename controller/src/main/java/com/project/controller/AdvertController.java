package com.project.controller;

import com.project.Logging;
import com.project.dto.*;
import com.project.service.IAdvertService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/adverts")
@RequiredArgsConstructor
public class AdvertController {

    private final IAdvertService advertService;

    @PostMapping
    public ResponseEntity<Void> createAdvert(@RequestBody @Valid CreateAdvertDto createAdvertDto) {

        advertService.createAdvert(createAdvertDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PatchMapping
    public ResponseEntity<Void> updateAdvert(@RequestBody UpdateAdvertDto updateAdvertDto) {

        advertService.updateAdvert(updateAdvertDto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdvert(@PathVariable Long id) {

        advertService.deleteAdvert(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/close/{id}")
    public ResponseEntity<Void> closeAdvert(@PathVariable Long id){
        advertService.closeAdvert(id);
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
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<AdvertDto>> getAdverts(@RequestParam String category,
                                                      @RequestParam Integer size,
                                                      @RequestParam Integer pageNumber,
                                                      @RequestParam(required = false) String search
    ) {
        Integer realPage = pageNumber - 1;
        AdvertListDto advertListDto = new AdvertListDto();
        advertListDto.setCategories(List.of(category));
        advertListDto.setPageSize(size);
        advertListDto.setPageNumber(realPage);
        advertListDto.setSearch(search);

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

    @GetMapping("/billing")
    public ResponseEntity<BillingDetailsDto> getBillingDetails(@RequestParam Long advertId,
                                                              @Valid @Min(3) @Max(14) @RequestParam Integer days){

        BillingDetailsDto billingDetails = advertService.getBillingDetails(advertId, days);
        return new ResponseEntity<>(billingDetails, HttpStatus.OK);

    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<List<AdvertDto>> getProfileAdverts(@PathVariable Long id){
        List<AdvertDto> profileActiveAdverts = advertService.getProfileActiveAdverts(id);

        return new ResponseEntity<>(profileActiveAdverts, HttpStatus.OK);
    }
}
