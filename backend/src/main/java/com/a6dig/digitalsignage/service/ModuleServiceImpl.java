package com.a6dig.digitalsignage.service;


import com.a6dig.digitalsignage.config.DomainCache;
import com.a6dig.digitalsignage.constant.AppConstant;
import com.a6dig.digitalsignage.dto.*;
import com.a6dig.digitalsignage.entity.AdCollection;
import com.a6dig.digitalsignage.entity.AdContent;
import com.a6dig.digitalsignage.entity.Module;
import com.a6dig.digitalsignage.exception.AdCollectionNotFoundException;
import com.a6dig.digitalsignage.exception.ModuleNotFoundException;
import com.a6dig.digitalsignage.mapper.ModuleMapper;
import com.a6dig.digitalsignage.repository.AdCollectionRepository;
import com.a6dig.digitalsignage.repository.ModuleRepository;
import com.a6dig.digitalsignage.util.ErrorMessage;
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
    private DomainCache domainCache;

    @Autowired
    private AdCollectionRepository adCollectionRepository;


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
        Module newModule = new Module();
        newModule.setName(module.getName());
        newModule.setConfig(module.getConfig());
        newModule.setDomain(this.domainCache.buildDomain(module.getType()));

        // only let assign existing ad collection
        // no new creation through this method
        if(module.getAdCollectionRequestUpdateDto() != null && module.getAdCollectionRequestUpdateDto().getId() != null) {
            AdCollection adCollection = this.adCollectionRepository.findById(
                    module.getAdCollectionRequestUpdateDto().getId()
            ).orElseThrow(() -> new AdCollectionNotFoundException(
                    AppConstant.ExceptionMessage.AdCollection.NOT_FOUND
                    ,List.of(
                            ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.AdCollection.idDoesNotExist(
                                    module.getAdCollectionRequestUpdateDto().getId())))
            ));


            newModule.setAdCollection(adCollection);
        }

        Module saved = this.moduleRepository.saveAndFlush(newModule);

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
        module.setName(dto.getName() == null ? module.getName() : dto.getName());
        module.setConfig(dto.getConfig() == null ? module.getConfig() : dto.getConfig());
        module.setDomain(dto.getType() == null ? module.getDomain() : this.domainCache.buildDomain(dto.getType()));




        // only let assign existing ad collection
        // no new creation through this method
        if(dto.getAdCollectionRequestUpdateDto() != null && dto.getAdCollectionRequestUpdateDto().getId() != null) {
            AdCollection adCollection = this.adCollectionRepository.findById(
                    dto.getAdCollectionRequestUpdateDto().getId()
            ).orElseThrow(() -> new AdCollectionNotFoundException(
                    AppConstant.ExceptionMessage.AdCollection.NOT_FOUND
                    ,List.of(
                    ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.AdCollection.idDoesNotExist(
                            dto.getAdCollectionRequestUpdateDto().getId())))
            ));


            module.setAdCollection(adCollection);
        }

        Module updated = this.moduleRepository.saveAndFlush(module);

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
