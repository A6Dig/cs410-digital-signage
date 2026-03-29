package com.a6dig.digitalsignage.service;

import com.a6dig.digitalsignage.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ModuleService {
    // get
    List<ModuleResponseDto> getAllModules();
    ModuleResponseDto getModuleById(Long id);
    // post
    ModuleResponseDto createModule(ModuleRequestDto module);

    // update
    ModuleResponseDto updateModuleById(Long id, ModuleRequestUpdateDto module);

    // delete
    void deleteModuleById(Long id);
    void deleteAllModules();
}
