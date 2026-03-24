package com.a6dig.digitalsignage.service;

import com.a6dig.digitalsignage.constant.AppConstant;
import com.a6dig.digitalsignage.dto.*;
import com.a6dig.digitalsignage.entity.Layout;
import com.a6dig.digitalsignage.entity.LayoutSlot;
import com.a6dig.digitalsignage.exception.InvalidLayoutException;
import com.a6dig.digitalsignage.exception.LayoutNotFoundException;
import com.a6dig.digitalsignage.exception.LayoutSlotNotFoundException;
import com.a6dig.digitalsignage.mapper.LayoutMapper;
import com.a6dig.digitalsignage.repository.LayoutRepository;
import com.a6dig.digitalsignage.repository.LayoutSlotRepository;
import com.a6dig.digitalsignage.util.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LayoutServiceImpl implements LayoutService{

    @Autowired
    private LayoutRepository layoutRepository;

    @Autowired
    private LayoutSlotRepository layoutSlotRepository;

    @Autowired
    private LayoutMapper layoutMapper;

    // GET
    @Override
    @Transactional(readOnly = true)
    public LayoutResponseDto getLayoutById(Long id) {

        Layout layout = layoutRepository.findById(id)
                .orElseThrow(() -> new LayoutNotFoundException(
                        AppConstant.ExceptionMessage.LAYOUT_NOT_FOUND
                        ,List.of(ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.layoutIdDoesNotExist(id)))
                ));

        return layoutMapper.toLayoutResponseDto(layout);
    }

    @Override
    @Transactional
    public List<LayoutResponseDto> getAllLayouts() {
        return layoutRepository.findAll().stream().map(layoutMapper::toLayoutResponseDto).collect(Collectors.toList());
    }

    @Override
    public LayoutResponseDto createLayout(LayoutRequestDto dto) {
        List<Map<String, String>> errors = new ArrayList<>();


        // validation
        if(dto.getCols() < 0) {
            errors.add(ErrorMessage.createErrorMessage("Layout column cannot be negative."));
        }
        if(dto.getRows() < 0) {
            errors.add(ErrorMessage.createErrorMessage("Layout row cannot be negative."));
        }
        if(dto.getCols() == 0) {
            errors.add(ErrorMessage.createErrorMessage("Layout column cannot be 0."));
        }
        if(dto.getRows() == 0) {
            errors.add(ErrorMessage.createErrorMessage("Layout row cannot be 0."));
        }

        if(!errors.isEmpty()) {
            throw new InvalidLayoutException(AppConstant.ExceptionMessage.LAYOUT_VALIDATION_FAILED, errors);
        }

        Layout layout = new Layout();
        layout.setName(dto.getName());
        layout.setCols(dto.getCols());
        layout.setRows(dto.getRows());
        layout.setSlots(new ArrayList<>());


        if(dto.getSlots() != null) {
            for(LayoutSlotRequestDto s : dto.getSlots()) {
                LayoutSlot slot = new LayoutSlot(layout);
                slot.setModuleId(s.getModuleId());
                slot.setColPos(s.getColPos());
                slot.setRowPos(s.getRowPos());
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
    public LayoutResponseDto updateLayout(Long id, LayoutRequestDto dto) {
        List<Map<String, String>> errors = new ArrayList<>();


        // validation
        if(dto.getCols() < 0) {
            errors.add(ErrorMessage.createErrorMessage("Layout column cannot be negative."));
        }
        if(dto.getRows() < 0) {
            errors.add(ErrorMessage.createErrorMessage("Layout row cannot be negative."));
        }
        if(dto.getCols() == 0) {
            errors.add(ErrorMessage.createErrorMessage("Layout column cannot be 0."));
        }
        if(dto.getRows() == 0) {
            errors.add(ErrorMessage.createErrorMessage("Layout row cannot be 0."));
        }

        if(!errors.isEmpty()) {
            throw new InvalidLayoutException(AppConstant.ExceptionMessage.LAYOUT_VALIDATION_FAILED, errors);
        }

        Layout layout = layoutRepository.findById(id)
                .orElseThrow(() -> new LayoutNotFoundException(
                        AppConstant.ExceptionMessage.LAYOUT_NOT_FOUND
                        ,List.of(ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.layoutIdDoesNotExist(id)))
                ));
        layout.setName(dto.getName());
        layout.setCols(dto.getCols());
        layout.setRows(dto.getRows());

        Layout updated = layoutRepository.saveAndFlush(layout);
        return layoutMapper.toLayoutResponseDto(updated);
    }

    @Override
    @Transactional
    public LayoutResponseDto updateLayoutSlots(Long id, List<LayoutSlotRequestDto> slots) {

        Layout layout = layoutRepository.findById(id)
                .orElseThrow(() -> new LayoutNotFoundException(
                        AppConstant.ExceptionMessage.LAYOUT_NOT_FOUND
                        ,List.of(ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.layoutIdDoesNotExist(id)))
                ));

        layout.getSlots().clear();

        for (LayoutSlotRequestDto s : slots) {
            LayoutSlot slot = new LayoutSlot(layout);
            slot.setModuleId(s.getModuleId());
            slot.setColPos(s.getColPos());
            slot.setRowPos(s.getRowPos());
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
                .orElseThrow(() -> new LayoutNotFoundException(
                        AppConstant.ExceptionMessage.LAYOUT_NOT_FOUND
                        ,List.of(ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.layoutIdDoesNotExist(id)))
                ));
        layoutRepository.delete(layout);
    }

    @Override
    @Transactional
    public void deleteAllLayouts() {
        layoutRepository.deleteAll();
    }

    @Override
    @Transactional
    public LayoutResponseDto addLayoutSlotToLayout(Long layoutId, LayoutSlotRequestDto dto) {
        Layout layout = layoutRepository.findById(layoutId)
                .orElseThrow(() -> new LayoutNotFoundException(
                        AppConstant.ExceptionMessage.LAYOUT_NOT_FOUND
                        ,List.of(ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.layoutIdDoesNotExist(layoutId)))
                ));


        LayoutSlot slot = new LayoutSlot(layout);
        slot.setModuleId(dto.getModuleId());
        slot.setColPos(dto.getColPos());
        slot.setRowPos(dto.getRowPos());
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
        List<Map<String, String>> errors = new ArrayList<>();

        Layout layout = layoutRepository.findById(layoutId)
                .orElseThrow(() -> new LayoutNotFoundException(
                        AppConstant.ExceptionMessage.LAYOUT_NOT_FOUND

                        , List.of(ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.layoutIdDoesNotExist(layoutId))
                )));

        LayoutSlot slot = layoutSlotRepository.findById(slotId)
                .orElseThrow(() -> new LayoutSlotNotFoundException(
                        AppConstant.ExceptionMessage.LAYOUT_SLOT_NOT_FOUND
                        , List.of(ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.layoutSlotIdDoesNotExist(slotId))
                )));

        if(!slot.getLayout().getId().equals(layoutId)) {
            errors.add(ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.layoutIdDoesNotExist(layoutId)));
            throw new InvalidLayoutException(AppConstant.ExceptionMessage.LAYOUT_INVALID_LAYOUT_ID, errors);
        }

        layout.getSlots().removeIf(s -> s.getId().equals(slotId));
        Layout updated = layoutRepository.save(layout);

        return layoutMapper.toLayoutResponseDto(updated);
    }


    @Override
    public boolean layoutExist(Long id) {
        return layoutRepository.existsById(id);
    }

    @Override
    public long layoutCount() {
        return layoutRepository.count();
    }


    // layout slots

    @Override
    public List<LayoutSlotResponseDto> getAllLayoutSlotsByLayoutId(Long layoutId) {
        return layoutSlotRepository.getAllLayoutSlotsByLayoutId(layoutId).stream().map(layoutMapper::toLayoutSlotResponseDto).collect(Collectors.toList());
    }
}
