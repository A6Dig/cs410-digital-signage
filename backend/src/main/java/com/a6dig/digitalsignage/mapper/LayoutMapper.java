package com.a6dig.digitalsignage.mapper;

import com.a6dig.digitalsignage.dto.LayoutResponseDto;
import com.a6dig.digitalsignage.dto.LayoutSlotResponseDto;
import com.a6dig.digitalsignage.entity.Layout;
import com.a6dig.digitalsignage.entity.LayoutSlot;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class LayoutMapper {
    public LayoutResponseDto toLayoutResponseDto(Layout layout) {
        LayoutResponseDto dto = new LayoutResponseDto();
        dto.setId(layout.getId());
        dto.setName(layout.getName());
        dto.setLayoutCol(layout.getLayoutCol());
        dto.setLayoutRow(layout.getLayoutRow());
        dto.setCreatedAt(layout.getCreatedAt());
        dto.setUpdatedAt(layout.getUpdatedAt());

        dto.setLayoutSlotList(
                layout.getLayoutSlotList() == null ? new ArrayList<>()
                        : layout.getLayoutSlotList()
                        .stream()
                        .map(this::toLayoutSlotResponseDto)
                        .collect(Collectors.toList())
        );
        return dto;
    }


    public LayoutSlotResponseDto toLayoutSlotResponseDto(LayoutSlot slot) {
        LayoutSlotResponseDto dto = new LayoutSlotResponseDto();
        dto.setId(slot.getId());
        dto.setGridCol(dto.getGridCol());
        dto.setGridRow(dto.getGridRow());
        dto.setColSpan(dto.getColSpan());
        dto.setRowSpan(dto.getRowSpan());
        dto.setCreatedAt(dto.getCreatedAt());
        dto.setUpdatedAt(dto.getUpdatedAt());
        return dto;
    }
}
