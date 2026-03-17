package com.a6dig.digitalsignage.service;

import com.a6dig.digitalsignage.constant.AppConstant;
import com.a6dig.digitalsignage.dto.*;
import com.a6dig.digitalsignage.entity.Layout;
import com.a6dig.digitalsignage.entity.LayoutSlot;
import com.a6dig.digitalsignage.exception.LayoutNotFoundException;
import com.a6dig.digitalsignage.mapper.LayoutMapper;
import com.a6dig.digitalsignage.repository.LayoutRepository;
import com.a6dig.digitalsignage.repository.LayoutSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LayoutServiceImpl implements LayoutService{

    @Autowired
    private LayoutRepository layoutRepository;

    @Autowired
    private LayoutSlotRepository layoutSlotRepository;

    @Autowired
    private LayoutMapper layoutMapper;

    // GET
    @Override
    public LayoutResponseDto getLayoutById(Long id) {

        Layout layout = layoutRepository.findById(id)
                .orElseThrow(() -> new LayoutNotFoundException(AppConstant.ExceptionMessage.LAYOUT_NOT_FOUND));

        return layoutMapper.toLayoutResponseDto(layout);
    }

    @Override
    public List<LayoutResponseDto> getAllLayouts() {
        return layoutRepository.findAll().stream().map(layoutMapper::toLayoutResponseDto).collect(Collectors.toList());
    }

    @Override
    public LayoutResponseDto createLayout(LayoutRequestDto dto) {
        Layout layout = new Layout();
        layout.setName(dto.getName());
        layout.setLayoutCol(dto.getLayoutCol());
        layout.setLayoutRow(dto.getLayoutRow());
        layout.setLayoutSlotList(new ArrayList<>());

        if(dto.getLayoutSlotRequestDtoList() != null) {
            for(LayoutSlotRequestDto s : dto.getLayoutSlotRequestDtoList()) {
                LayoutSlot slot = new LayoutSlot(layout);
                slot.setModuleId(null);
                slot.setGridCol(s.getGridCol());
                slot.setGridRow(s.getGridRow());
                slot.setColSpan(s.getColSpan());
                slot.setRowSpan(s.getRowSpan());
                slot.setzIndex(s.getzIndex());

                layout.addLayoutSlot(slot);
            }
        }

        Layout saved = layoutRepository.save(layout);

        return layoutMapper.toLayoutResponseDto(saved);
    }

    @Override
    @Transactional
    public LayoutResponseDto updateLayout(Long id, LayoutRequestUpdateDto dto) {

        Layout layout = layoutRepository.findById(id)
                .orElseThrow(() -> new LayoutNotFoundException(AppConstant.ExceptionMessage.LAYOUT_NOT_FOUND));
        layout.setName(dto.getName());
        layout.setLayoutCol(dto.getLayoutCol());
        layout.setLayoutRow(dto.getLayoutRow());

        Layout updated = layoutRepository.save(layout);
        return layoutMapper.toLayoutResponseDto(updated);
    }

    @Override
    @Transactional
    public LayoutResponseDto updateLayoutSlots(Long id, List<LayoutSlotRequestUpdateDto> slots) {

        Layout layout = layoutRepository.findById(id)
                .orElseThrow(() -> new LayoutNotFoundException(AppConstant.ExceptionMessage.LAYOUT_NOT_FOUND));

        layout.getLayoutSlotList().clear();

        for (LayoutSlotRequestUpdateDto s : slots) {
            LayoutSlot slot = new LayoutSlot(layout);
            slot.setModuleId(s.getModuleId());
            slot.setGridCol(s.getGridCol());
            slot.setGridRow(s.getGridRow());
            slot.setColSpan(s.getColSpan());
            slot.setRowSpan(s.getRowSpan());
            slot.setzIndex(s.getzIndex());

            layout.addLayoutSlot(slot);

        }

        Layout updated = layoutRepository.save(layout);
        return layoutMapper.toLayoutResponseDto(updated);
    }

    @Override
    @Transactional
    public void deleteLayout(Long id) {
        Layout layout = layoutRepository.findById(id)
                .orElseThrow(() -> new LayoutNotFoundException(AppConstant.ExceptionMessage.LAYOUT_NOT_FOUND));
        layoutRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteAllLayouts() {
        layoutRepository.deleteAll();
    }

    @Override
    @Transactional
    public LayoutResponseDto addLayoutSlotToLayout(Long layoutId, LayoutSlotRequestUpdateDto dto) {
        Layout layout = layoutRepository.findById(layoutId)
                .orElseThrow(() -> new LayoutNotFoundException(AppConstant.ExceptionMessage.LAYOUT_NOT_FOUND));


        LayoutSlot slot = new LayoutSlot(layout);
        slot.setModuleId(dto.getModuleId());
        slot.setGridCol(dto.getGridCol());
        slot.setGridRow(dto.getGridRow());
        slot.setColSpan(dto.getColSpan());
        slot.setRowSpan(dto.getRowSpan());
        slot.setzIndex(dto.getzIndex());

        layout.addLayoutSlot(slot);


        Layout updated = layoutRepository.save(layout);
        return layoutMapper.toLayoutResponseDto(updated);
    }

    @Override
    @Transactional
    public LayoutResponseDto removeLayoutSlotFromLayout(Long layoutId, Long slotId) {
        Layout layout = layoutRepository.findById(layoutId)
                .orElseThrow(() -> new LayoutNotFoundException(AppConstant.ExceptionMessage.LAYOUT_NOT_FOUND));

        // TODO
//        Layout slot = layoutSlotRepository.findById(slotId)
//                .orElseThrow(() -> new LayoutNotFoundException(AppConstant.ExceptionMessage.LAYOUT_SLOT_NOT_FOUND));

        return null;
    }


    @Override
    public boolean layoutExist(Long id) {
        return layoutRepository.existsById(id);
    }

    @Override
    public long layoutCount() {
        return layoutRepository.count();
    }
}
