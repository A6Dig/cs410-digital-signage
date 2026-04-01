package com.a6dig.digitalsignage.service;


import com.a6dig.digitalsignage.config.DomainCache;
import com.a6dig.digitalsignage.constant.AppConstant;
import com.a6dig.digitalsignage.dto.*;
import com.a6dig.digitalsignage.entity.AdCollection;
import com.a6dig.digitalsignage.entity.AdContent;
import com.a6dig.digitalsignage.entity.Module;
import com.a6dig.digitalsignage.exception.AdCollectionNotFoundException;
import com.a6dig.digitalsignage.exception.InvalidDomainException;
import com.a6dig.digitalsignage.exception.InvalidJSONException;
import com.a6dig.digitalsignage.exception.ModuleNotFoundException;
import com.a6dig.digitalsignage.mapper.ModuleMapper;
import com.a6dig.digitalsignage.repository.AdCollectionRepository;
import com.a6dig.digitalsignage.repository.ModuleRepository;
import com.a6dig.digitalsignage.util.ErrorMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ModuleServiceImpl implements ModuleService {
    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private ModuleMapper moduleMapper;

    @Autowired
    private ModuleFactory moduleFactory;



    @Override
    @Transactional(readOnly = true)
    public List<ModuleResponseDto> getAllModules() {
        return this.moduleRepository
                .findAll()
                .stream()
                .map(this.moduleMapper::toModuleResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ModuleResponseDto getModuleById(Long id) {
        Module module = this.moduleRepository
                .findById(id)
                .orElseThrow(() -> new ModuleNotFoundException(
                        AppConstant.ExceptionMessage.Module.NOT_FOUND
                        ,List.of(ErrorMessage.createErrorMessage(
                                AppConstant.ExceptionMessage.Module.idDoesNotExist(id)
                        ))
                ));
        return this.moduleMapper.toModuleResponseDto(module);
    }

    @Override
    @Transactional
    public ModuleResponseDto createModule(ModuleRequestDto module) {


        Module saved = this.moduleRepository.saveAndFlush(this.moduleFactory.createModuleFromDto(module, null));

        return this.moduleMapper.toModuleResponseDto(saved);
    }

    @Override
    @Transactional
    public ModuleResponseDto updateModuleById(Long id, ModuleRequestUpdateDto dto) {

        Module module = this.moduleRepository.findById(id)
                .orElseThrow(() -> new ModuleNotFoundException(
                        AppConstant.ExceptionMessage.Module.NOT_FOUND,
                        List.of(ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.Module.idDoesNotExist(id)))
                ));

        Module updated = this.moduleRepository.saveAndFlush(this.moduleFactory.createModuleFromDto(dto, module));

        return this.moduleMapper.toModuleResponseDto(updated);
    }

    @Override
    public void deleteModuleById(Long id) {

        Module module = this.moduleRepository.findById(id)
                .orElseThrow(() -> new ModuleNotFoundException(
                        AppConstant.ExceptionMessage.Module.NOT_FOUND,
                        List.of(ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.Module.idDoesNotExist(id)))
                ));
        this.moduleRepository.delete(module);
    }

    @Override
    public void deleteAllModules() {
        this.moduleRepository.deleteAll();
    }
}
