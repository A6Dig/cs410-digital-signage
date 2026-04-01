package com.a6dig.digitalsignage.controller;

import com.a6dig.digitalsignage.constant.AppConstant;
import com.a6dig.digitalsignage.dto.*;
import com.a6dig.digitalsignage.service.ModuleFacade;
import com.a6dig.digitalsignage.service.ModuleService;
import com.a6dig.digitalsignage.util.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modules")
public class ModuleController {
    @Autowired
    private ModuleFacade moduleFacade;

    // get
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<ModuleResponseDto>>
        getAllModuleById(@PathVariable Long id) {
        ModuleResponseDto dto = this.moduleFacade.getModuleById(id);
        return ResponseEntity.ok(APIResponse.success(dto));
    }

    @GetMapping("")
    public ResponseEntity<APIResponse<List<ModuleResponseDto>>> getAllModules() {
        return ResponseEntity.ok(APIResponse.success(this.moduleFacade.getAllModules()));
    }

    // post
    @PostMapping("")
    public ResponseEntity<APIResponse<ModuleResponseDto>>
        createModule(@RequestBody ModuleRequestDto moduleRequestDto) {
        ModuleResponseDto moduleResponseDto = this.moduleFacade.createModule(moduleRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(APIResponse.created(moduleResponseDto));
    }

    // update

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<ModuleResponseDto>> updatedLayout(@PathVariable Long id, @RequestBody ModuleRequestUpdateDto dto) {
        ModuleResponseDto responseDto = this.moduleFacade.updateModuleById(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success(AppConstant.SuccessMessage.Module.UPDATED, responseDto));
    }

    // delete
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> deleteModule(@PathVariable Long id) {
        this.moduleFacade.deleteModuleById(id);
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success(AppConstant.SuccessMessage.Module.DELETED));
    }

    @DeleteMapping("")
    public ResponseEntity<APIResponse<Void>> deleteModules() {
        this.moduleFacade.deleteAllModules();
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success(AppConstant.SuccessMessage.Module.DELETED_ALL));
    }



}
