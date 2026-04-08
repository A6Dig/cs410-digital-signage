package com.a6dig.digitalsignage.service;


import com.a6dig.digitalsignage.config.DomainCache;
import com.a6dig.digitalsignage.constant.AppConstant;
import com.a6dig.digitalsignage.dto.ModuleRequestDto;
import com.a6dig.digitalsignage.entity.AdCollection;
import com.a6dig.digitalsignage.entity.Domain;
import com.a6dig.digitalsignage.entity.Module;
import com.a6dig.digitalsignage.exception.AdCollectionNotFoundException;
import com.a6dig.digitalsignage.exception.InvalidJSONException;
import com.a6dig.digitalsignage.repository.AdCollectionRepository;
import com.a6dig.digitalsignage.util.ErrorMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ModuleFactory {
    @Autowired
    private DomainCache domainCache;
    @Autowired
    private AdCollectionRepository adCollectionRepository;
    @Autowired
    private ObjectMapper objectMapper;


    public <T extends ModuleRequestDto>Module createModuleFromDto(T dto, Module existing) {

        AdCollection adCollection = existing != null ? existing.getAdCollection() : null;
        String jsonConfig;

        try {
            jsonConfig =
            dto.getConfig() != null ? objectMapper.writeValueAsString(dto.getConfig()) : (existing != null ? existing.getConfig() : null);
        } catch (JsonProcessingException ex) {
            throw new InvalidJSONException(
                    AppConstant.ExceptionMessage.INVALID_JSON,
                    List.of(ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.INVALID_JSON_UNABLE_TO_CONVERT_TO_STRING))
            );
        }


        // only let assign existing ad collection
        // no new creation through this method
        if(dto.getAdCollection() != null && dto.getAdCollection().getId() != null) {
            adCollection = this.adCollectionRepository.findById(
                    dto.getAdCollection().getId()
            ).orElseThrow(() -> new AdCollectionNotFoundException(
                    AppConstant.ExceptionMessage.AdCollection.NOT_FOUND
                    ,List.of(
                    ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.AdCollection.idDoesNotExist(
                            dto.getAdCollection().getId())))
            ));
        }
        Long id = existing != null ? existing.getId() : null;
        String name = dto.getName() != null ? dto.getName() : (existing != null ? existing.getName() : null);
        Domain domain = dto.getType() != null ? domainCache.buildDomain(dto.getType()) : (existing != null ? existing.getDomain() : null);
        LocalDateTime createdAt = existing != null ? existing.getCreatedAt() : null;
        LocalDateTime updatedAt = existing != null ? existing.getUpdatedAt() : null;


        return Module.builder()
                .Id(id)
                .name(name)
                .domain(domain)
                .adCollection(adCollection)
                .config(jsonConfig)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}
