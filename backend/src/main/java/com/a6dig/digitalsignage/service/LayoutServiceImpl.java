package com.a6dig.digitalsignage.service;

import com.a6dig.digitalsignage.constant.AppConstant;
import com.a6dig.digitalsignage.dto.*;
import com.a6dig.digitalsignage.entity.Layout;
import com.a6dig.digitalsignage.entity.LayoutSlot;
<<<<<<< HEAD
import com.a6dig.digitalsignage.exception.InvalidLayoutException;
import com.a6dig.digitalsignage.exception.InvalidLayoutSlotException;
import com.a6dig.digitalsignage.exception.LayoutNotFoundException;
import com.a6dig.digitalsignage.exception.LayoutSlotNotFoundException;
=======
import com.a6dig.digitalsignage.entity.Module;
import com.a6dig.digitalsignage.exception.*;
>>>>>>> 28ec52b383dd2e358d2e5711391f9e0f9f3feb92
import com.a6dig.digitalsignage.mapper.LayoutMapper;
import com.a6dig.digitalsignage.repository.LayoutRepository;
import com.a6dig.digitalsignage.repository.LayoutSlotRepository;
import com.a6dig.digitalsignage.repository.ModuleRepository;
import com.a6dig.digitalsignage.util.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LayoutServiceImpl implements LayoutService{

    @Autowired
    private LayoutRepository layoutRepository;

    @Autowired
    private LayoutSlotRepository layoutSlotRepository;

    @Autowired
    private LayoutMapper layoutMapper;

    @Autowired
    private ModuleRepository moduleRepository;

    // GET
    @Override
    @Transactional(readOnly = true)
    public LayoutResponseDto<LayoutSlotResponseDto> getLayoutById(Long id) {

        Layout layout = layoutRepository.findById(id)
                .orElseThrow(() -> new LayoutNotFoundException(
                        AppConstant.ExceptionMessage.LAYOUT_NOT_FOUND
                        ,List.of(ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.layoutIdDoesNotExist(id)))
                ));

        return layoutMapper.toLayoutResponseDto(layout);
    }

    @Override
    @Transactional
    public List<LayoutResponseDto<LayoutSlotResponseDto>> getAllLayouts() {
        return layoutRepository.findAll().stream().map(layoutMapper::toLayoutResponseDto).collect(Collectors.toList());
    }

    @Override
    public LayoutResponseDto<LayoutSlotResponseDto> createLayout(LayoutRequestDto<LayoutSlotRequestDto> dto) {
<<<<<<< HEAD
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
=======
        this.validateLayout(true, dto);
>>>>>>> 28ec52b383dd2e358d2e5711391f9e0f9f3feb92

        Layout layout = new Layout();
        layout.setName(dto.getName());
        layout.setCols(dto.getCols());
        layout.setRows(dto.getRows());
        layout.setSlots(new ArrayList<>());


<<<<<<< HEAD

        if(dto.getSlots() != null) {
            for(LayoutSlotRequestDto s : dto.getSlots()) {
                LayoutSlot slot = new LayoutSlot(layout);
                slot.setModuleId(s.getModuleId());
=======
        Map<Long, Module> existingModule = new HashMap<>();

        List<Long> moduleIds = new ArrayList<>(Optional.ofNullable(dto.getSlots())
                .orElse(List.of())
                .stream()
                .map(LayoutSlotDtoBase::getModuleId)
                .filter(Objects::nonNull)
                .toList());


        this.moduleRepository.findAllById(moduleIds).forEach(module -> existingModule.put(module.getId(), module));

        moduleIds.removeAll(existingModule.keySet());


        if (!moduleIds.isEmpty()) {
            List<Map<String, String>> errors = moduleIds.stream()
                    .map(moduleId -> ErrorMessage.createErrorMessage(
                            AppConstant.ExceptionMessage.Module.idDoesNotExist(moduleId)))
                    .toList();

            throw new ModuleNotFoundException(
                    AppConstant.ExceptionMessage.Module.NOT_FOUND,
                    errors
            );
        }


        if(dto.getSlots() != null) {
            for(LayoutSlotRequestDto s : dto.getSlots()) {

                validateLayoutSlot(true, s);

                LayoutSlot slot = new LayoutSlot(layout);
                slot.setModule(existingModule.get(s.getModuleId()));
>>>>>>> 28ec52b383dd2e358d2e5711391f9e0f9f3feb92
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
    public LayoutResponseDto<LayoutSlotResponseDto> updateLayout(Long id, LayoutRequestDto<LayoutSlotRequestUpdateDto> dto) {
<<<<<<< HEAD
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
=======
        validateLayout(false, dto);
>>>>>>> 28ec52b383dd2e358d2e5711391f9e0f9f3feb92

        Layout layout = layoutRepository.findById(id)
                .orElseThrow(() -> new LayoutNotFoundException(
                        AppConstant.ExceptionMessage.LAYOUT_NOT_FOUND
                        ,List.of(ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.layoutIdDoesNotExist(id)))
                ));
<<<<<<< HEAD
        layout.setName(dto.getName());
        layout.setCols(dto.getCols());
        layout.setRows(dto.getRows());


        List<Long> existingSlotIds = this.layoutSlotRepository.getAllLayoutSlotsByLayoutId(id).stream().map(LayoutSlot::getId).toList();

=======
        layout.setName(dto.getName() == null ? layout.getName() : dto.getName());
        layout.setCols(dto.getCols() == null ? layout.getCols() : dto.getCols());
        layout.setRows(dto.getRows() == null ? layout.getRows() : dto.getRows());


        Map<Long, Module> existingModule = new HashMap<>();

        List<Long> moduleIds = new ArrayList<>(Optional.ofNullable(dto.getSlots())
                .orElse(List.of())
                .stream()
                .map(LayoutSlotDtoBase::getModuleId)
                .filter(Objects::nonNull)
                .toList());


        this.moduleRepository.findAllById(moduleIds).forEach(module -> existingModule.put(module.getId(), module));

        moduleIds.removeAll(existingModule.keySet());


        if (!moduleIds.isEmpty()) {
            List<Map<String, String>> errors = moduleIds.stream()
                    .map(moduleId -> ErrorMessage.createErrorMessage(
                            AppConstant.ExceptionMessage.Module.idDoesNotExist(moduleId)))
                    .toList();

            throw new ModuleNotFoundException(
                    AppConstant.ExceptionMessage.Module.NOT_FOUND,
                    errors
            );
        }

        Map<Long, LayoutSlot> slots = new HashMap<>();

        layout.getSlots().forEach(layoutSlot -> {
            slots.put(layoutSlot.getId(), layoutSlot);
        });

//        layout.getSlots().clear();
>>>>>>> 28ec52b383dd2e358d2e5711391f9e0f9f3feb92

        if(dto.getSlots() != null) {
            for(LayoutSlotRequestUpdateDto s : dto.getSlots()) {

<<<<<<< HEAD
                if (s.getId() != null && !existingSlotIds.contains(s.getId())) {
=======
                validateLayoutSlot(s.getId() == null, s);

                if (s.getId() != null && !slots.containsKey(s.getId())) {
>>>>>>> 28ec52b383dd2e358d2e5711391f9e0f9f3feb92
                    throw new InvalidLayoutSlotException(
                            AppConstant.ExceptionMessage.LAYOUT_SLOT_INVALID_LAYOUT_SLOT
                            ,List.of(ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.layoutSlotIdDoesNotBelongToTheLayout(s.getId(), layout.getName())))
                    );
                }

<<<<<<< HEAD
                LayoutSlot slot = new LayoutSlot(layout);
                slot.setId(s.getId());
                slot.setModuleId(s.getModuleId());
                slot.setColPos(s.getColPos());
                slot.setRowPos(s.getRowPos());
                slot.setColSpan(s.getColSpan());
                slot.setRowSpan(s.getRowSpan());
                slot.setzIndex(s.getzIndex());

                layout.addLayoutSlot(slot);
=======

                if (s.getId() == null) {

                    LayoutSlot slot = new LayoutSlot(layout);
                    slot.setModule(existingModule.get(s.getModuleId()));
//                    slot.setModuleId(s.getModuleId());
                    slot.setColPos(s.getColPos());
                    slot.setRowPos(s.getRowPos());
                    slot.setColSpan(s.getColSpan());
                    slot.setRowSpan(s.getRowSpan());
                    slot.setzIndex(s.getzIndex());

                    layout.addLayoutSlot(slot);
                } else {
                    LayoutSlot existing = slots.get(s.getId());
                    existing.setId(s.getId());
                    existing.setModule(existingModule.get(s.getModuleId()));
//                    existing.setModuleId(s.getModuleId());
                    existing.setColPos(s.getColPos() == null ? existing.getColPos() : s.getColPos());
                    existing.setRowPos(s.getRowPos() == null ? existing.getRowPos() : s.getRowPos());
                    existing.setColSpan(s.getColSpan() == null ? existing.getColSpan() : s.getColSpan());
                    existing.setRowSpan(s.getRowSpan() == null ? existing.getRowSpan() : s.getRowSpan());
                    existing.setzIndex(s.getzIndex() == null ? existing.getzIndex() : s.getzIndex());
                }
>>>>>>> 28ec52b383dd2e358d2e5711391f9e0f9f3feb92
            }
        }

        Layout updated = layoutRepository.saveAndFlush(layout);
        return layoutMapper.toLayoutResponseDto(updated);
    }

    @Override
    @Transactional
<<<<<<< HEAD
    public LayoutResponseDto<LayoutSlotResponseDto> updateLayoutSlots(Long id, List<LayoutSlotRequestDto> slots) {
=======
    public LayoutResponseDto<LayoutSlotResponseDto> updateLayoutSlots(Long id, List<LayoutSlotRequestUpdateDto> slots) {
>>>>>>> 28ec52b383dd2e358d2e5711391f9e0f9f3feb92

        Layout layout = layoutRepository.findById(id)
                .orElseThrow(() -> new LayoutNotFoundException(
                        AppConstant.ExceptionMessage.LAYOUT_NOT_FOUND
                        ,List.of(ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.layoutIdDoesNotExist(id)))
                ));

<<<<<<< HEAD
        layout.getSlots().clear();

        for (LayoutSlotRequestDto s : slots) {
            LayoutSlot slot = new LayoutSlot(layout);
            slot.setModuleId(s.getModuleId());
            slot.setColPos(s.getColPos());
            slot.setRowPos(s.getRowPos());
            slot.setColSpan(s.getColSpan());
            slot.setRowSpan(s.getRowSpan());
            slot.setzIndex(s.getzIndex());
=======



    Map<Long, Module> existingModule = new HashMap<>();

    List<Long> moduleIds = new ArrayList<>(Optional.ofNullable(slots)
            .orElse(List.of())
            .stream()
            .map(LayoutSlotDtoBase::getModuleId)
            .filter(Objects::nonNull)
            .toList());


    this.moduleRepository.findAllById(moduleIds).forEach(module -> existingModule.put(module.getId(), module));

    moduleIds.removeAll(existingModule.keySet());


    if (!moduleIds.isEmpty()) {
        List<Map<String, String>> errors = moduleIds.stream()
                .map(moduleId -> ErrorMessage.createErrorMessage(
                        AppConstant.ExceptionMessage.Module.idDoesNotExist(moduleId)))
                .toList();

        throw new ModuleNotFoundException(
                AppConstant.ExceptionMessage.Module.NOT_FOUND,
                errors
        );
    }


    Map<Long, LayoutSlot> existingSlots = new HashMap<>();
    this.layoutSlotRepository.getAllLayoutSlotsByLayoutId(id).forEach(layoutSlot -> existingSlots.put(layoutSlot.getId(), layoutSlot));


        for(LayoutSlotRequestUpdateDto s : slots) {

            if (s.getId() != null && !existingSlots.containsKey(s.getId())) {
                throw new InvalidLayoutSlotException(
                        AppConstant.ExceptionMessage.LAYOUT_SLOT_INVALID_LAYOUT_SLOT
                        ,List.of(ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.layoutSlotIdDoesNotBelongToTheLayout(s.getId(), layout.getName())))
                );
            }

            LayoutSlot existing = existingSlots.get(s.getId());

            LayoutSlot slot = new LayoutSlot(layout);
            slot.setId(s.getId());
            slot.setModule(existingModule.get(s.getModuleId()));
//            slot.setModuleId(s.getModuleId());
            slot.setColPos(s.getColPos() == null ? existing.getColPos() : s.getColPos());
            slot.setRowPos(s.getRowPos() == null ? existing.getRowPos() : s.getRowPos());
            slot.setColSpan(s.getColSpan() == null ? existing.getColSpan() : s.getColSpan());
            slot.setRowSpan(s.getRowSpan() == null ? existing.getRowSpan() : s.getRowSpan());
            slot.setzIndex(s.getzIndex() == null ? existing.getzIndex() : s.getzIndex());
>>>>>>> 28ec52b383dd2e358d2e5711391f9e0f9f3feb92

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
    public LayoutResponseDto<LayoutSlotResponseDto> addLayoutSlotToLayout(Long layoutId, LayoutSlotRequestDto dto) {
        Layout layout = layoutRepository.findById(layoutId)
                .orElseThrow(() -> new LayoutNotFoundException(
                        AppConstant.ExceptionMessage.LAYOUT_NOT_FOUND
                        ,List.of(ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.layoutIdDoesNotExist(layoutId)))
                ));


        Optional<Module> existingModule = Optional.empty();

        if (dto.getModuleId() != null) {
            existingModule =
                    this.moduleRepository.findById(dto.getModuleId());
            if (existingModule.isEmpty()) {
                throw new ModuleNotFoundException(
                        AppConstant.ExceptionMessage.Module.NOT_FOUND,
                        List.of(ErrorMessage.createErrorMessage(
                                AppConstant.ExceptionMessage.Module.idDoesNotExist(dto.getModuleId())))
                );
            }
        }

        LayoutSlot slot = new LayoutSlot(layout);
<<<<<<< HEAD
        slot.setModuleId(dto.getModuleId());
=======
        slot.setModule(existingModule.orElse(null));
>>>>>>> 28ec52b383dd2e358d2e5711391f9e0f9f3feb92
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
    public LayoutResponseDto<LayoutSlotResponseDto> removeLayoutSlotFromLayout(Long layoutId, Long slotId) {
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
<<<<<<< HEAD
=======




    private void validateLayoutSlot(boolean isNew, LayoutSlotRequestDto dto) {
        List<Map<String, String>> errors = new ArrayList<>();

        // validation


        if(isNew && dto.getColPos() == null) {
            errors.add(ErrorMessage.createErrorMessage("Layout slot's position column cannot be empty."));
        }
        if(isNew && dto.getRowPos() == null) {
            errors.add(ErrorMessage.createErrorMessage("Layout slot's position row cannot be empty."));
        }

        if(isNew && dto.getColSpan() == null) {
            errors.add(ErrorMessage.createErrorMessage("Layout slot's position column span cannot be empty."));
        }
        if(isNew && dto.getRowSpan() == null) {
            errors.add(ErrorMessage.createErrorMessage("Layout slot's position row span cannot be empty."));
        }
        if(isNew && dto.getzIndex() == null) {
            errors.add(ErrorMessage.createErrorMessage("Layout slot's Z-index cannot be empty."));
        }


        if(dto.getColPos() != null && dto.getColPos() == 0) {
            errors.add(ErrorMessage.createErrorMessage("Layout slot's position column cannot be 0."));
        }
        if(dto.getColPos() != null && dto.getColPos() < 0) {
            errors.add(ErrorMessage.createErrorMessage("Layout slot's position column cannot be negative."));
        }
        if(dto.getRowPos() != null && dto.getRowPos() == 0) {
            errors.add(ErrorMessage.createErrorMessage("Layout slot's position row cannot be 0."));
        }
        if(dto.getRowPos() != null && dto.getRowPos() < 0) {
            errors.add(ErrorMessage.createErrorMessage("Layout slot's position row cannot be negative."));
        }


        if(dto.getColSpan() != null && dto.getColSpan() == 0) {
            errors.add(ErrorMessage.createErrorMessage("Layout slot's position column span cannot be 0."));
        }
        if(dto.getColSpan() != null && dto.getColSpan() < 0) {
            errors.add(ErrorMessage.createErrorMessage("Layout slot's position column span cannot be negative."));
        }


        if(dto.getRowSpan() != null && dto.getRowSpan() == 0) {
            errors.add(ErrorMessage.createErrorMessage("Layout slot's position row span cannot be 0."));
        }
        if(dto.getRowSpan() != null && dto.getRowSpan() < 0) {
            errors.add(ErrorMessage.createErrorMessage("Layout slot's position row span cannot be negative."));
        }

        if(!errors.isEmpty()) {
            throw new InvalidLayoutSlotException(AppConstant.ExceptionMessage.LAYOUT_SLOT_VALIDATION_FAILED, errors);
        }
    }


    private void validateLayout(boolean isNew, LayoutRequestDto<? extends LayoutSlotRequestDto> dto) {
        List<Map<String, String>> errors = new ArrayList<>();


        // validation


        if(isNew && dto.getRows() == null) {
            errors.add(ErrorMessage.createErrorMessage("Layout row cannot be empty."));
        }
        if(isNew && dto.getCols() == null) {
            errors.add(ErrorMessage.createErrorMessage("Layout column cannot be empty."));
        }

        if(dto.getCols() != null && dto.getCols() < 0) {
            errors.add(ErrorMessage.createErrorMessage("Layout column cannot be negative."));
        }
        if(dto.getRows() != null && dto.getRows() < 0) {
            errors.add(ErrorMessage.createErrorMessage("Layout row cannot be negative."));
        }
        if(dto.getCols() != null && dto.getCols() == 0) {
            errors.add(ErrorMessage.createErrorMessage("Layout column cannot be 0."));
        }
        if(dto.getRows() != null && dto.getRows() == 0) {
            errors.add(ErrorMessage.createErrorMessage("Layout row cannot be 0."));
        }

        if(!errors.isEmpty()) {
            throw new InvalidLayoutException(AppConstant.ExceptionMessage.LAYOUT_VALIDATION_FAILED, errors);
        }
    }
>>>>>>> 28ec52b383dd2e358d2e5711391f9e0f9f3feb92
}
