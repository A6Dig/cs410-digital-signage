package com.a6dig.digitalsignage.service;

import com.a6dig.digitalsignage.dto.*;

import java.util.List;

public interface LayoutService {
    // GET

    LayoutResponseDto getLayoutById(Long id);
    List<LayoutResponseDto> getAllLayouts();

    List<LayoutSlotResponseDto> getAllLayoutSlotsByLayoutId(Long layoutId);

    // POST
    LayoutResponseDto createLayout(LayoutRequestDto dto);

    // PUT
    LayoutResponseDto updateLayout(Long id, LayoutRequestUpdateDto dto);
    LayoutResponseDto updateLayoutSlots(Long id, List<LayoutSlotRequestUpdateDto> slots);

    // DELETE
    void deleteLayout(Long id);
    void deleteAllLayouts();


    // Entity Relation
    LayoutResponseDto addLayoutSlotToLayout(Long layoutId, LayoutSlotRequestDto dto);
    LayoutResponseDto removeLayoutSlotFromLayout(Long layoutId, Long slotId);



    // Misc
    boolean layoutExist(Long id);
    long layoutCount();
}
