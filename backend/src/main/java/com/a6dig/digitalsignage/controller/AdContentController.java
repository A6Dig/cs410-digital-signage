package com.a6dig.digitalsignage.controller;
import com.a6dig.digitalsignage.constant.AppConstant;
import com.a6dig.digitalsignage.dto.AdContentResponseDto;
import com.a6dig.digitalsignage.dto.AdContentRequestDto;
import com.a6dig.digitalsignage.dto.AdContentRequestUpdateDto;
import com.a6dig.digitalsignage.dto.AdContentResponseDto;
import com.a6dig.digitalsignage.service.AdContentService;
import com.a6dig.digitalsignage.util.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/adContents")
public class AdContentController {
    @Autowired
    private AdContentService adContentService;


    // get
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<AdContentResponseDto>>
    getAllAdContentById(@PathVariable Long id) {
        AdContentResponseDto dto = this.adContentService.getAdContentById(id);
        return ResponseEntity.ok(APIResponse.success(dto));
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<AdContentResponseDto>>> getAllAdContents() {
        return ResponseEntity.ok(APIResponse.success(this.adContentService.getAllAdContents()));
    }

    // post
    public ResponseEntity<APIResponse<AdContentResponseDto>> createAdContent(@RequestBody AdContentRequestDto AdContentRequestDto) {
        AdContentResponseDto AdContentResponseDto = this.adContentService.createAdContent(AdContentRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(APIResponse.created(AdContentResponseDto));
    }

    // update

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<AdContentResponseDto>> updatedLayout(@PathVariable Long id, @RequestBody AdContentRequestUpdateDto dto) {
        AdContentResponseDto responseDto = this.adContentService.updateAdContentById(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success(AppConstant.SuccessMessage.AdContent.UPDATED, responseDto));
    }

    // delete
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> deleteAdContent(@PathVariable Long id) {
        this.adContentService.deleteAdContentById(id);
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success(AppConstant.SuccessMessage.AdContent.DELETED));
    }

    @DeleteMapping
    public ResponseEntity<APIResponse<Void>> deleteAdContents() {
        this.adContentService.deleteAllContents();
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success(AppConstant.SuccessMessage.AdContent.DELETED_ALL));
    }

}
