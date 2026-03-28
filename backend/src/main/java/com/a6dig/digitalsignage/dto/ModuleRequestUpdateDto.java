package com.a6dig.digitalsignage.dto;

public class ModuleRequestUpdateDto<T extends AdCollectionRequestUpdateDto> extends ModuleRequestDto<T>{
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
