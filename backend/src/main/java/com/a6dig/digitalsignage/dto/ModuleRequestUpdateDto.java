package com.a6dig.digitalsignage.dto;

public class ModuleRequestUpdateDto extends ModuleDtoBase{
    private Long id;

    private AdCollectionRequestUpdateDto adCollectionRequestUpdateDto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AdCollectionRequestUpdateDto getAdCollectionRequestUpdateDto() {
        return adCollectionRequestUpdateDto;
    }

    public void setAdCollectionRequestUpdateDto(AdCollectionRequestUpdateDto adCollectionRequestUpdateDto) {
        this.adCollectionRequestUpdateDto = adCollectionRequestUpdateDto;
    }
}
