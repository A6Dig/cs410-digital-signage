package com.a6dig.digitalsignage.controller;

import com.a6dig.digitalsignage.constant.AppConstant;
import com.a6dig.digitalsignage.dto.AdCollectionRequestDto;
import com.a6dig.digitalsignage.dto.AdCollectionRequestUpdateDto;
import com.a6dig.digitalsignage.dto.AdCollectionResponseDto;
import com.a6dig.digitalsignage.service.AdCollectionService;
import com.a6dig.digitalsignage.util.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/adCollections")
public class AdCollectionController {

    @Autowired
    private AdCollectionService adCollectionService;


    // get
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<AdCollectionResponseDto>>
    getAllAdCollectionById(@PathVariable Long id) {
        AdCollectionResponseDto dto = this.adCollectionService.getAdCollectionById(id);
        return ResponseEntity.ok(APIResponse.success(dto));
    }

    @GetMapping("")
    public ResponseEntity<APIResponse<List<AdCollectionResponseDto>>> getAllAdCollections() {
        return ResponseEntity.ok(APIResponse.success(this.adCollectionService.getAllAdCollections()));
    }

    // post
    @PostMapping("")
    public ResponseEntity<APIResponse<AdCollectionResponseDto>>
    createAdCollection(@RequestBody AdCollectionRequestDto adCollectionRequestDto) {
        AdCollectionResponseDto adCollectionResponseDto = this.adCollectionService.createAdCollection(adCollectionRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(APIResponse.created(adCollectionResponseDto));
    }

    // update

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<AdCollectionResponseDto>> updatedAdCollection(@PathVariable Long id, @RequestBody AdCollectionRequestUpdateDto dto) {
        AdCollectionResponseDto responseDto = this.adCollectionService.updateAdCollectionById(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success(AppConstant.SuccessMessage.AdCollection.UPDATED, responseDto));
    }

    // delete
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> deleteAdCollection(@PathVariable Long id) {
        this.adCollectionService.deleteAdCollectionById(id);
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success(AppConstant.SuccessMessage.AdCollection.DELETED));
    }

    @DeleteMapping("")
    public ResponseEntity<APIResponse<Void>> deleteAdCollections() {
        this.adCollectionService.deleteAllAdCollections();
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success(AppConstant.SuccessMessage.AdCollection.DELETED_ALL));
    }

}
