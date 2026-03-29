package com.a6dig.digitalsignage.service;

import com.a6dig.digitalsignage.constant.AppConstant;
import com.a6dig.digitalsignage.dto.LayoutRequestDto;
import com.a6dig.digitalsignage.dto.LayoutSlotRequestUpdateDto;
import com.a6dig.digitalsignage.entity.Layout;
import com.a6dig.digitalsignage.entity.LayoutSlot;
import com.a6dig.digitalsignage.exception.InvalidLayoutSlotException;
import com.a6dig.digitalsignage.exception.LayoutNotFoundException;
import com.a6dig.digitalsignage.repository.LayoutRepository;
import com.a6dig.digitalsignage.repository.LayoutSlotRepository;
import com.a6dig.digitalsignage.util.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LayoutSlotServiceImpl implements LayoutSlotService{
    @Autowired
    private LayoutRepository layoutRepository;
    @Autowired
    private LayoutSlotRepository layoutSlotRepository;
    @Override
    @Transactional
    public void deleteAllSlotsByLayoutId(Long layoutId) {

        Layout layout = layoutRepository.findById(layoutId)
                .orElseThrow(() -> new LayoutNotFoundException(
                        AppConstant.ExceptionMessage.LAYOUT_NOT_FOUND
                        ,List.of(ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.layoutIdDoesNotExist(layoutId)))
                ));
        layoutSlotRepository.deleteAllLayoutSlotsByLayoutId(layout.getId());
    }

    @Override
    @Transactional
    public void deleteLayoutSlotsByLayoutId(Long layoutId, LayoutRequestDto<LayoutSlotRequestUpdateDto> layoutDto) {
        List<Map<String, String>> errors = new ArrayList<>();


        Layout layout = layoutRepository.findById(layoutId)
                .orElseThrow(() -> new LayoutNotFoundException(
                        AppConstant.ExceptionMessage.LAYOUT_NOT_FOUND
                        ,List.of(ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.layoutIdDoesNotExist(layoutId)))
                ));

        List<Long> existingSlots = layout.getSlots().stream().map(LayoutSlot::getId).toList();

        List<Long> idsToDelete = new ArrayList<>();

        for(LayoutSlotRequestUpdateDto s : layoutDto.getSlots()) {

            if (s.getId() == null) {
                errors.add(ErrorMessage.createErrorMessage("Layout slot's id cannot be empty."));
                throw new InvalidLayoutSlotException(AppConstant.ExceptionMessage.LAYOUT_SLOT_VALIDATION_FAILED, errors);
            }

            if (s.getId() != null && !existingSlots.contains(s.getId())) {
                throw new InvalidLayoutSlotException(
                        AppConstant.ExceptionMessage.LAYOUT_SLOT_INVALID_LAYOUT_SLOT
                        , List.of(ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.layoutSlotIdDoesNotBelongToTheLayout(s.getId(), layout.getName())))
                );
            }

            idsToDelete.add(s.getId());
        }

        layoutSlotRepository.deleteAllByIdInBatch(idsToDelete);


    }
}
