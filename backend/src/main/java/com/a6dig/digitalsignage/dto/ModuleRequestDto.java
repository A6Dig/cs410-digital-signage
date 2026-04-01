package com.a6dig.digitalsignage.dto;

public class ModuleRequestDto extends ModuleDtoBase{
    private AdCollectionRequestUpdateDto adCollection;

    public AdCollectionRequestUpdateDto getAdCollectionRequestUpdateDto() {
        return adCollection;
    }

    public void setAdCollectionRequestUpdateDto(AdCollectionRequestUpdateDto adCollectionRequestUpdateDto) {
        this.adCollection = adCollectionRequestUpdateDto;
    }
}
