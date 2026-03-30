package com.a6dig.digitalsignage.mapper;

import com.a6dig.digitalsignage.constant.AdContentTypeEnum;
import com.a6dig.digitalsignage.dto.AdContentResponseDto;
import com.a6dig.digitalsignage.entity.AdContent;
import org.springframework.stereotype.Component;


@Component
public class AdContentMapper {
    public AdContentResponseDto toAdContentResponseDto(AdContent adContent) {
        AdContentResponseDto dto = new AdContentResponseDto();
        dto.setId(adContent.getId());
        dto.setName(adContent.getName());
        dto.setUrl(adContent.getUrl());
        dto.setType(AdContentTypeEnum.valueOf(adContent.getDomain().getAlphaNumCode()));
        dto.setUpdatedAt(adContent.getUpdatedAt());
        dto.setCreatedAt(adContent.getCreatedAt());
        return dto;
    }
}
