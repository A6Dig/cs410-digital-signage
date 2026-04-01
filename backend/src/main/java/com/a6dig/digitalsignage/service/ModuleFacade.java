package com.a6dig.digitalsignage.service;

import com.a6dig.digitalsignage.constant.AppConstant;
import com.a6dig.digitalsignage.dto.ModuleRequestDto;
import com.a6dig.digitalsignage.dto.ModuleRequestUpdateDto;
import com.a6dig.digitalsignage.dto.ModuleResponseDto;
import com.a6dig.digitalsignage.entity.AdCollection;
import com.a6dig.digitalsignage.entity.Domain;
import com.a6dig.digitalsignage.entity.Module;
import com.a6dig.digitalsignage.exception.AdCollectionNotFoundException;
import com.a6dig.digitalsignage.exception.InvalidDomainException;
import com.a6dig.digitalsignage.exception.InvalidJSONException;
import com.a6dig.digitalsignage.exception.ModuleNotFoundException;
import com.a6dig.digitalsignage.mapper.ModuleMapper;
import com.a6dig.digitalsignage.util.ErrorMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ModuleFacade {
    @Autowired
    private ModuleService moduleService;

    @Autowired
    private ModuleValidator moduleValidator;




    public List<ModuleResponseDto> getAllModules() {
        return this.moduleService.getAllModules();
    }


    public ModuleResponseDto getModuleById(Long id) {
        return this.moduleService.getModuleById(id);
    }


    public ModuleResponseDto createModule(ModuleRequestDto module) {
        moduleValidator.validateType(module);
        return this.moduleService.createModule(module);
    }

    public ModuleResponseDto updateModuleById(Long id, ModuleRequestUpdateDto dto) {
        moduleValidator.validateType(dto);
        return this.moduleService.updateModuleById(id, dto);
    }


    public void deleteModuleById(Long id) {
        this.moduleService.deleteModuleById(id);
    }


    public void deleteAllModules() {
        this.moduleService.deleteAllModules();
    }
}