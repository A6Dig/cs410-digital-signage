package com.a6dig.digitalsignage.dto;

import java.time.LocalDateTime;

public class LayoutSlotResponseDto extends LayoutSlotDtoBase{

    private Long id;

    private Long layoutId;
<<<<<<< HEAD
=======

    private ModuleResponseDto moduleResponseDto;
>>>>>>> 28ec52b383dd2e358d2e5711391f9e0f9f3feb92
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(Long layoutId) {
        this.layoutId = layoutId;
    }

<<<<<<< HEAD
=======
    public ModuleResponseDto getModuleResponseDto() {
        return moduleResponseDto;
    }

    public void setModuleResponseDto(ModuleResponseDto moduleResponseDto) {
        this.moduleResponseDto = moduleResponseDto;
    }

>>>>>>> 28ec52b383dd2e358d2e5711391f9e0f9f3feb92
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
