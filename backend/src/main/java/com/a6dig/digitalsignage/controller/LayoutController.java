package com.a6dig.digitalsignage.controller;

import com.a6dig.digitalsignage.constant.AppConstant;
import com.a6dig.digitalsignage.dto.LayoutRequestDto;
import com.a6dig.digitalsignage.dto.LayoutRequestUpdateDto;
import com.a6dig.digitalsignage.dto.LayoutResponseDto;
import com.a6dig.digitalsignage.service.LayoutService;
import com.a6dig.digitalsignage.util.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/layouts")
public class LayoutController {
    @Autowired
    private LayoutService layoutService;

    // get
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<LayoutResponseDto>> getLayoutById(@PathVariable Long id) {
        LayoutResponseDto dto = this.layoutService.getLayoutById(id);
        return ResponseEntity.ok(APIResponse.success(dto));
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<LayoutResponseDto>>> getAllLayouts() {
        return ResponseEntity.ok(APIResponse.success(this.layoutService.getAllLayouts()));
    }

    // post

    @PostMapping
    public ResponseEntity<APIResponse<LayoutResponseDto>> createLayout(@RequestBody LayoutRequestDto request) {
        LayoutResponseDto dto = this.layoutService.createLayout(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                APIResponse.created(dto)
        );
    }

    // update

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<LayoutResponseDto>> updateLayout(@PathVariable Long id, @RequestBody LayoutRequestUpdateDto request) {
        LayoutResponseDto dto = this.layoutService.updateLayout(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                APIResponse.success(AppConstant.SuccessMessage.LAYOUT_UPDATED,dto)
        );
    }


    // delete
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> deleteLayout(@PathVariable Long id) {
        this.layoutService.deleteLayout(id);
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success(AppConstant.SuccessMessage.LAYOUT_DELETED));
    }

    @DeleteMapping
    public ResponseEntity<APIResponse<Void>> deleteAllLayouts() {
        this.layoutService.deleteAllLayouts();
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success(AppConstant.SuccessMessage.LAYOUT_ALL_DELETED));
    }
}
