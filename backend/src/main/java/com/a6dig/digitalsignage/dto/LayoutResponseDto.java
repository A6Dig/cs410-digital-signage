package com.a6dig.digitalsignage.dto;

import com.a6dig.digitalsignage.entity.Layout;
import com.a6dig.digitalsignage.entity.LayoutSlot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LayoutResponseDto {
    private Long id;
    private String name;
    private int layoutCol;
    private int layoutRow;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<LayoutSlotResponseDto> layoutSlotList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLayoutCol() {
        return layoutCol;
    }

    public void setLayoutCol(int layoutCol) {
        this.layoutCol = layoutCol;
    }

    public int getLayoutRow() {
        return layoutRow;
    }

    public void setLayoutRow(int layoutRow) {
        this.layoutRow = layoutRow;
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

    public List<LayoutSlotResponseDto> getLayoutSlotList() {
        return layoutSlotList;
    }

    public void setLayoutSlotList(List<LayoutSlotResponseDto> layoutSlotList) {
        this.layoutSlotList = layoutSlotList;
    }
}
