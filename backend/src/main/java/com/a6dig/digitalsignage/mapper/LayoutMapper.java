package com.a6dig.digitalsignage.mapper;

import com.a6dig.digitalsignage.dto.LayoutResponseDto;
import com.a6dig.digitalsignage.dto.LayoutSlotResponseDto;
import com.a6dig.digitalsignage.entity.Layout;
import com.a6dig.digitalsignage.entity.LayoutSlot;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class LayoutMapper {
    public LayoutResponseDto<LayoutSlotResponseDto> toLayoutResponseDto(Layout layout) {
        LayoutResponseDto<LayoutSlotResponseDto> dto = new LayoutResponseDto<>();
        dto.setId(layout.getId());
        dto.setName(layout.getName());
        dto.setCols(layout.getCols());
        dto.setRows(layout.getRows());
        dto.setCreatedAt(layout.getCreatedAt());
        dto.setUpdatedAt(layout.getUpdatedAt());

        dto.setSlots(
                layout.getSlots() == null ? new ArrayList<>()
                        : layout.getSlots()
                        .stream()
                        .map(this::toLayoutSlotResponseDto)
                        .collect(Collectors.toList())
        );
        return dto;
    }


    public LayoutSlotResponseDto toLayoutSlotResponseDto(LayoutSlot slot) {
        LayoutSlotResponseDto dto = new LayoutSlotResponseDto();
        dto.setId(slot.getId());
        dto.setLayoutId(slot.getLayout().getId());
        dto.setModuleId(slot.getModuleId());
        dto.setColPos(slot.getColPos());
        dto.setRowPos(slot.getRowPos());
        dto.setColSpan(slot.getColSpan());
        dto.setRowSpan(slot.getRowSpan());
        dto.setzIndex(slot.getzIndex());
        dto.setCreatedAt(slot.getCreatedAt());
        dto.setUpdatedAt(slot.getUpdatedAt());
        return dto;
    }
}
