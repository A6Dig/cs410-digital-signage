package com.a6dig.digitalsignage.mapper;

import com.a6dig.digitalsignage.constant.ModuleTypeEnum;
import com.a6dig.digitalsignage.dto.AdCollectionResponseDto;
import com.a6dig.digitalsignage.dto.AdContentResponseDto;
import com.a6dig.digitalsignage.dto.ModuleResponseDto;
import com.a6dig.digitalsignage.entity.AdCollection;
import com.a6dig.digitalsignage.entity.Module;
import org.springframework.stereotype.Component;

@Component
public class ModuleMapper {
    public ModuleResponseDto toModuleResponseDto(Module module) {
        ModuleResponseDto moduleResponseDto = new ModuleResponseDto();

        moduleResponseDto.setId(module.getId());
        moduleResponseDto.setType(ModuleTypeEnum.valueOf(module.getDomain().getAlphaNumCode()));
        moduleResponseDto.setAdCollectionResponseDto(module.getAdCollection() == null ? null : AdCollectionMapper.toAdCollectionResponseDto(module.getAdCollection()));
        moduleResponseDto.setName(module.getName());
        moduleResponseDto.setConfig(module.getConfig());
        moduleResponseDto.setUpdatedAt(module.getUpdatedAt());
        moduleResponseDto.setCreatedAt(module.getCreatedAt());

        return moduleResponseDto;

    }
}
