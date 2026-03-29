package com.a6dig.digitalsignage.dto;

public class ModuleRequestDto extends ModuleDtoBase{
    private AdCollectionRequestUpdateDto adCollectionRequestUpdateDto;

    public AdCollectionRequestUpdateDto getAdCollectionRequestUpdateDto() {
        return adCollectionRequestUpdateDto;
    }

    public void setAdCollectionRequestUpdateDto(AdCollectionRequestUpdateDto adCollectionRequestUpdateDto) {
        this.adCollectionRequestUpdateDto = adCollectionRequestUpdateDto;
    }
}
