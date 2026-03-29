package com.a6dig.digitalsignage.service;

import com.a6dig.digitalsignage.dto.LayoutRequestDto;
import com.a6dig.digitalsignage.dto.LayoutSlotRequestUpdateDto;

import java.util.List;

public interface LayoutSlotService {
    void deleteAllSlotsByLayoutId(Long layoutId);
    void deleteLayoutSlotsByLayoutId(Long layoutId, LayoutRequestDto<LayoutSlotRequestUpdateDto> layout);
}
