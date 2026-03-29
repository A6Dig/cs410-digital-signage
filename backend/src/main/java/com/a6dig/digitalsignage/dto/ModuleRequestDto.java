package com.a6dig.digitalsignage.dto;

public class ModuleRequestDto extends ModuleDtoBase{
    private AdCollectionRequestDto adCollectionRequestDto;

    public AdCollectionRequestDto getAdCollectionRequestDto() {
        return adCollectionRequestDto;
    }

    public void setAdCollectionRequestDto(AdCollectionRequestDto adCollectionRequestDto) {
        this.adCollectionRequestDto = adCollectionRequestDto;
    }
}
