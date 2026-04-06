package com.a6dig.digitalsignage.mapper;

import com.a6dig.digitalsignage.constant.AppConstant;
import com.a6dig.digitalsignage.constant.ModuleTypeEnum;
import com.a6dig.digitalsignage.dto.ModuleResponseDto;
import com.a6dig.digitalsignage.entity.Module;
import com.a6dig.digitalsignage.exception.InvalidJSONException;
import com.a6dig.digitalsignage.util.ErrorMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ModuleMapper {
    @Autowired
    private AdCollectionMapper adCollectionMapper;

    @Autowired
    private ObjectMapper objectMapper;

    public ModuleResponseDto toModuleResponseDto(Module module) {
        ModuleResponseDto moduleResponseDto = new ModuleResponseDto();

        moduleResponseDto.setId(module.getId());
        moduleResponseDto.setType(ModuleTypeEnum.valueOf(module.getDomain().getAlphaNumCode()));
        moduleResponseDto.setAdCollection(module.getAdCollection() == null ? null : this.adCollectionMapper.toAdCollectionResponseDto(module.getAdCollection()));
        moduleResponseDto.setName(module.getName());


        try {
            moduleResponseDto.setConfig(module.getConfig() == null ? null : objectMapper.readTree(module.getConfig()));
        } catch (JsonProcessingException ex) {
            throw new InvalidJSONException(
                    AppConstant.ExceptionMessage.INVALID_JSON,
                    List.of(ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.INVALID_JSON_UNABLE_TO_CONVERT_TO_JSON))
            );
        }

        moduleResponseDto.setUpdatedAt(module.getUpdatedAt());
        moduleResponseDto.setCreatedAt(module.getCreatedAt());

        return moduleResponseDto;

    }
}
