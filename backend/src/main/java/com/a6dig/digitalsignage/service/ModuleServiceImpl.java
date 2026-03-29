package com.a6dig.digitalsignage.service;


import com.a6dig.digitalsignage.dto.ModuleRequestDto;
import com.a6dig.digitalsignage.dto.ModuleRequestUpdateDto;
import com.a6dig.digitalsignage.dto.ModuleResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleServiceImpl implements ModuleService {
    @Override
    public List<ModuleResponseDto> getAllModules() {
        return null;
    }

    @Override
    public ModuleResponseDto getModuleById(Long id) {
        return null;
    }

    @Override
    public ModuleResponseDto createModule(ModuleRequestDto module) {
        return null;
    }

    @Override
    public ModuleResponseDto updateModuleById(Long id, ModuleRequestUpdateDto module) {
        return null;
    }

    @Override
    public void deleteModuleById(Long id) {

    }

    @Override
    public void deleteAllModules() {

    }
}
