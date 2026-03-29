package com.a6dig.digitalsignage.service;

import com.a6dig.digitalsignage.dto.*;

import java.util.List;

public interface LayoutService {
    // GET

    LayoutResponseDto<LayoutSlotResponseDto> getLayoutById(Long id);
    List<LayoutResponseDto<LayoutSlotResponseDto>> getAllLayouts();


    // POST
    LayoutResponseDto<LayoutSlotResponseDto> createLayout(LayoutRequestDto<LayoutSlotRequestDto> dto);

    // PUT
    LayoutResponseDto<LayoutSlotResponseDto> updateLayout(Long id, LayoutRequestDto<LayoutSlotRequestUpdateDto> dto);
    LayoutResponseDto<LayoutSlotResponseDto> updateLayoutSlots(Long id, List<LayoutSlotRequestUpdateDto> slots);

    // DELETE
    void deleteLayout(Long id);
    void deleteAllLayouts();


    // Entity Relation
    LayoutResponseDto<LayoutSlotResponseDto> addLayoutSlotToLayout(Long layoutId, LayoutSlotRequestDto dto);
    LayoutResponseDto<LayoutSlotResponseDto> removeLayoutSlotFromLayout(Long layoutId, Long slotId);



    // Misc
    boolean layoutExist(Long id);
    long layoutCount();


    List<LayoutSlotResponseDto> getAllLayoutSlotsByLayoutId(Long layoutId);
}
