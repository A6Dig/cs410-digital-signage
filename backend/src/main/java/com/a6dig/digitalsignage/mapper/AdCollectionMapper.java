package com.a6dig.digitalsignage.mapper;

import com.a6dig.digitalsignage.dto.AdCollectionResponseDto;
import com.a6dig.digitalsignage.dto.AdContentResponseDto;
import com.a6dig.digitalsignage.entity.AdCollection;
import org.springframework.stereotype.Component;


@Component
public class AdCollectionMapper {
    public static AdCollectionResponseDto toAdCollectionResponseDto(AdCollection adCollection) {
        AdCollectionResponseDto dto = new AdCollectionResponseDto();

        dto.setId(adCollection.getId());
        dto.setName(adCollection.getName());
        dto.setUrl(adCollection.getUrl());
        dto.setAdContents(adCollection.getAdContents().stream().map(AdContentMapper::toAdContentResponseDto).toList());
        dto.setUpdatedAt(adCollection.getUpdatedAt());
        dto.setCreatedAt(adCollection.getCreatedAt());
        return dto;


    }
}
