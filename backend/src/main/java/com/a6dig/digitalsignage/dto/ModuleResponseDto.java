package com.a6dig.digitalsignage.dto;

import java.time.LocalDateTime;

public class ModuleResponseDto extends ModuleDtoBase{
    private Long id;

    private AdCollectionResponseDto adCollection;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AdCollectionResponseDto getAdCollection() {
        return adCollection;
    }

    public void setAdCollection(AdCollectionResponseDto adCollectionResponseDto) {
        this.adCollection = adCollectionResponseDto;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
