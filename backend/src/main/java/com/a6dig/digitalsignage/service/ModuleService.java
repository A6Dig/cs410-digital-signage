package com.a6dig.digitalsignage.service;

import com.a6dig.digitalsignage.dto.*;

import java.util.List;

public interface ModuleService {
    // get
    List<ModuleResponseDto<AdCollectionResponseDto<AdContentResponseDto>>> getAllModules();
    ModuleResponseDto<AdCollectionResponseDto<AdContentResponseDto>> getModuleById();
    // post
    ModuleResponseDto<AdCollectionResponseDto<AdContentResponseDto>>
        createModule(ModuleRequestDto<? extends AdCollectionRequestDto<? extends AdContentRequestDto>> module);

    // update
    ModuleResponseDto<AdCollectionResponseDto<AdContentResponseDto>>
        updateModuleById(Long id, ModuleRequestUpdateDto<? extends AdCollectionRequestDto<? extends AdContentRequestDto>> module);

    // delete
    void deleteModuleById(Long id);
    void deleteAllModules();
}
