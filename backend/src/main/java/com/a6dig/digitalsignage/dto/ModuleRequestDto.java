package com.a6dig.digitalsignage.dto;

public class ModuleRequestDto extends ModuleDtoBase{
    private AdCollectionRequestUpdateDto adCollection;

    public AdCollectionRequestUpdateDto getAdCollection() {
        return adCollection;
    }

    public void setAdCollection(AdCollectionRequestUpdateDto adCollection) {
        this.adCollection = adCollection;
    }
}
