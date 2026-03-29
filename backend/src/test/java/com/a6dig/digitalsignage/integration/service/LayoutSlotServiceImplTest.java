package com.a6dig.digitalsignage.integration.service;


import com.a6dig.digitalsignage.dto.*;
import com.a6dig.digitalsignage.entity.Layout;
import com.a6dig.digitalsignage.entity.LayoutSlot;
import com.a6dig.digitalsignage.exception.InvalidLayoutException;
import com.a6dig.digitalsignage.exception.InvalidLayoutSlotException;
import com.a6dig.digitalsignage.exception.LayoutNotFoundException;
import com.a6dig.digitalsignage.repository.LayoutRepository;
import com.a6dig.digitalsignage.repository.LayoutSlotRepository;
import com.a6dig.digitalsignage.service.LayoutService;
import com.a6dig.digitalsignage.service.LayoutServiceImpl;
import com.a6dig.digitalsignage.service.LayoutSlotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
public class LayoutSlotServiceImplTest {

    @Autowired
    private LayoutSlotService layoutSlotService;

    @Autowired
    private LayoutService layoutService;


    @Autowired
    private LayoutRepository layoutRepository;

    @Autowired
    private LayoutSlotRepository layoutSlotRepository;


    @Autowired
    private LayoutServiceImpl layoutServiceImpl;

    @BeforeEach
    void cleanUp() {
        this.layoutRepository.deleteAll();
        this.layoutSlotRepository.deleteAll();
    }


    private <T extends LayoutSlotRequestDto> LayoutRequestDto<T> buildLayoutRequestDto(String name, Integer col, Integer row) {
        LayoutRequestDto<T> dto = new LayoutRequestDto<>();
        dto.setName(name);
        dto.setCols(col);
        dto.setRows(row);
        dto.setSlots(new ArrayList<>());
        return dto;
    }


    private LayoutSlotRequestDto buildLayoutSlotRequestDto(Long moduleId, Integer colPos, Integer rowPos, Integer colSpan, Integer rowSpan, Integer zIndex){
        LayoutSlotRequestDto slot = new LayoutSlotRequestDto();

        slot.setModuleId(moduleId);
        slot.setColPos(colPos);
        slot.setRowPos(rowPos);
        slot.setColSpan(colSpan);
        slot.setRowSpan(rowSpan);
        slot.setzIndex(zIndex);

        return slot;
    }


    private LayoutSlotRequestUpdateDto buildLayoutSlotRequestUpdateDto(Long id, Long moduleId, Integer colPos, Integer rowPos, Integer colSpan, Integer rowSpan, Integer zIndex){
        LayoutSlotRequestUpdateDto slot = new LayoutSlotRequestUpdateDto();

        slot.setId(id);
        slot.setModuleId(moduleId);
        slot.setColPos(colPos);
        slot.setRowPos(rowPos);
        slot.setColSpan(colSpan);
        slot.setRowSpan(rowSpan);
        slot.setzIndex(zIndex);

        return slot;
    }

    private void assertLayout(LayoutResponseDto<LayoutSlotResponseDto> layout, String expectedName, Integer expectedCols, Integer expectedRows) {
        assertNotNull(layout.getId());
        assertNotNull(layout.getName());
        assertNotNull(layout.getCols());
        assertNotNull(layout.getRows());
        assertEquals(expectedName, layout.getName());
        assertEquals(expectedCols, layout.getCols());
        assertEquals(expectedRows, layout.getRows());
        assertNotNull(layout.getCreatedAt());
        assertNotNull(layout.getUpdatedAt());
        assertTrue(layoutRepository.existsById(layout.getId()));
    }


    private void assertLayoutSlot(LayoutSlotResponseDto slot
            , Long expectedLayoutId
            , Long expectedModuleId
            , Integer expectedColPos
            , Integer expectedRowPos
            , Integer expectedColSpan
            , Integer expectedRowSpan
            , Integer expectedzIndex) {
        assertNotNull(slot.getId());
        assertNotNull(slot.getLayoutId());
        assertNotNull(expectedColPos);
        assertNotNull(expectedRowPos);
        assertNotNull(expectedColSpan);
        assertNotNull(expectedRowSpan);
        assertNotNull(expectedzIndex);
        assertEquals(expectedLayoutId, slot.getLayoutId());
        assertEquals(expectedModuleId, slot.getModuleId());
        assertEquals(expectedColPos, slot.getColPos());
        assertEquals(expectedRowPos, slot.getRowPos());
        assertEquals(expectedColSpan, slot.getColSpan());
        assertEquals(expectedRowSpan, slot.getRowSpan());
        assertEquals(expectedzIndex, slot.getzIndex());
        assertNotNull(slot.getCreatedAt());
        assertNotNull(slot.getUpdatedAt());
        assertTrue(layoutSlotRepository.existsById(slot.getId()));
    }



    @Test
    void shouldDeleteLayoutSlotsByLayoutId() {


        // layout 1
        LayoutRequestDto<LayoutSlotRequestDto> layout1 = this.buildLayoutRequestDto("Default Layout", 2,2);
        LayoutSlotRequestDto layoutSlot11 = this.buildLayoutSlotRequestDto(1L, 2, 2, 2,1,0);
        LayoutSlotRequestDto layoutSlot12 = this.buildLayoutSlotRequestDto(1L, 22, 22, 2,1,0);

        layout1.setSlots(List.of(layoutSlot11, layoutSlot12));

        LayoutResponseDto<LayoutSlotResponseDto> saved1 = this.layoutServiceImpl.createLayout(layout1);
        List<LayoutSlotResponseDto> savedSlots1 = new ArrayList<>(saved1.getSlots());
        savedSlots1.sort(Comparator.comparing(LayoutSlotResponseDto::getColPos));

        assertLayout(saved1, "Default Layout", 2,2);
        assertEquals(2, saved1.getSlots().size());
        assertLayoutSlot(savedSlots1.get(0), saved1.getId(), 1L, 2, 2, 2,1, 0);
        assertLayoutSlot(savedSlots1.get(1), saved1.getId(), 1L, 22, 22, 2,1,0);


        // layout 2

        LayoutRequestDto<LayoutSlotRequestDto> layout2 = this.buildLayoutRequestDto("Secondary Layout", 2,2);
        LayoutSlotRequestDto layoutSlot21 = this.buildLayoutSlotRequestDto( 1L, 2, 2, 2,1,0);
        LayoutSlotRequestDto layoutSlot22 = this.buildLayoutSlotRequestDto( 1L, 22, 22, 2,1,0);



        layout2.setSlots(List.of(layoutSlot21, layoutSlot22));

        LayoutResponseDto<LayoutSlotResponseDto> saved2 = this.layoutServiceImpl.createLayout(layout2);
        List<LayoutSlotResponseDto> savedSlots2 = new ArrayList<>(saved2.getSlots());
        savedSlots2.sort(Comparator.comparing(LayoutSlotResponseDto::getColPos));

        assertLayout(saved2, "Secondary Layout", 2,2);
        assertEquals(2, saved2.getSlots().size());
        assertLayoutSlot(savedSlots2.get(0), saved2.getId(), 1L, 2, 2, 2,1, 0);
        assertLayoutSlot(savedSlots2.get(1), saved2.getId(), 1L, 22, 22, 2,1,0);



        // verify slots deleted from layout 1

        LayoutRequestDto<LayoutSlotRequestUpdateDto> layout = this.buildLayoutRequestDto(null,null,null);
        LayoutSlotRequestUpdateDto deleteSlot1 = this.buildLayoutSlotRequestUpdateDto(savedSlots1.get(0).getId(),null, null, null, null, null, null);
        layout.setSlots(List.of(deleteSlot1));

        this.layoutSlotService.deleteLayoutSlotsByLayoutId(saved1.getId(), layout);
        this.layoutSlotRepository.flush();

        List<LayoutSlot> response1 = this.layoutSlotRepository.getAllLayoutSlotsByLayoutId(saved1.getId());

        assertEquals(1, response1.size());




        // verify  layout 2's slots didn't get delete accidentally

        List<LayoutSlot> response2 = this.layoutSlotRepository.getAllLayoutSlotsByLayoutId(saved2.getId());

        assertEquals(2, response2.size());
    }



    @Test
    void shouldDeleteAllLayoutSlotsByLayoutId() {


        // layout 1
        LayoutRequestDto<LayoutSlotRequestDto> layout1 = this.buildLayoutRequestDto("Default Layout", 2,2);
        LayoutSlotRequestDto layoutSlot11 = this.buildLayoutSlotRequestDto(1L, 2, 2, 2,1,0);
        LayoutSlotRequestDto layoutSlot12 = this.buildLayoutSlotRequestDto(1L, 22, 22, 2,1,0);

        layout1.setSlots(List.of(layoutSlot11, layoutSlot12));

        LayoutResponseDto<LayoutSlotResponseDto> saved1 = this.layoutServiceImpl.createLayout(layout1);
        List<LayoutSlotResponseDto> savedSlots1 = new ArrayList<>(saved1.getSlots());
        savedSlots1.sort(Comparator.comparing(LayoutSlotResponseDto::getColPos));

        assertLayout(saved1, "Default Layout", 2,2);
        assertEquals(2, saved1.getSlots().size());
        assertLayoutSlot(savedSlots1.get(0), saved1.getId(), 1L, 2, 2, 2,1, 0);
        assertLayoutSlot(savedSlots1.get(1), saved1.getId(), 1L, 22, 22, 2,1,0);


        // layout 2

        LayoutRequestDto<LayoutSlotRequestDto> layout2 = this.buildLayoutRequestDto("Secondary Layout", 2,2);
        LayoutSlotRequestDto layoutSlot21 = this.buildLayoutSlotRequestDto( 1L, 2, 2, 2,1,0);
        LayoutSlotRequestDto layoutSlot22 = this.buildLayoutSlotRequestDto( 1L, 22, 22, 2,1,0);



        layout2.setSlots(List.of(layoutSlot21, layoutSlot22));

        LayoutResponseDto<LayoutSlotResponseDto> saved2 = this.layoutServiceImpl.createLayout(layout2);
        List<LayoutSlotResponseDto> savedSlots2 = new ArrayList<>(saved2.getSlots());
        savedSlots2.sort(Comparator.comparing(LayoutSlotResponseDto::getColPos));

        assertLayout(saved2, "Secondary Layout", 2,2);
        assertEquals(2, saved2.getSlots().size());
        assertLayoutSlot(savedSlots2.get(0), saved2.getId(), 1L, 2, 2, 2,1, 0);
        assertLayoutSlot(savedSlots2.get(1), saved2.getId(), 1L, 22, 22, 2,1,0);



        // verify slots deleted from layout 1

        LayoutRequestDto<LayoutSlotRequestUpdateDto> layout = this.buildLayoutRequestDto(null,null,null);
        LayoutSlotRequestUpdateDto deleteSlot1 = this.buildLayoutSlotRequestUpdateDto(savedSlots1.get(0).getId(),null, null, null, null, null, null);
        layout.setSlots(List.of(deleteSlot1));

        this.layoutSlotService.deleteAllSlotsByLayoutId(saved1.getId());
        this.layoutSlotRepository.flush();

        List<LayoutSlot> response1 = this.layoutSlotRepository.getAllLayoutSlotsByLayoutId(saved1.getId());

        assertEquals(0, response1.size());




        // verify  layout 2's slots didn't get delete accidentally

        List<LayoutSlot> response2 = this.layoutSlotRepository.getAllLayoutSlotsByLayoutId(saved2.getId());

        assertEquals(2, response2.size());
    }



    @Test
    void shouldThrowErrorLayoutNotFoundWhenDeleteLayoutSlotsByLayoutId() {

        LayoutRequestDto<LayoutSlotRequestUpdateDto> layout = this.buildLayoutRequestDto(null,null,null);
        LayoutSlotRequestUpdateDto deleteSlot1 = this.buildLayoutSlotRequestUpdateDto(1L,null, null, null, null, null, null);
        layout.setSlots(List.of(deleteSlot1));

        assertThrows(LayoutNotFoundException.class, () -> this.layoutSlotService.deleteLayoutSlotsByLayoutId(1L, layout));
    }




    @Test
    void shouldThrowErrorLayoutSlotIdCannotBeNullWhenDeleteLayoutSlotsByLayoutId() {

        LayoutRequestDto<LayoutSlotRequestUpdateDto> layout = this.buildLayoutRequestDto(null,null,null);
        LayoutSlotRequestUpdateDto deleteSlot1 = this.buildLayoutSlotRequestUpdateDto(null,null, null, null, null, null, null);
        layout.setSlots(List.of(deleteSlot1));

        assertThrows(LayoutNotFoundException.class, () -> this.layoutSlotService.deleteLayoutSlotsByLayoutId(1L, layout));
    }


    @Test
    void shouldThrowErrorLayoutSlotDoesntBelongToTheLayoutWhenDeleteLayoutSlotsByLayoutId() {



        // layout 1
        LayoutRequestDto<LayoutSlotRequestDto> layout1 = this.buildLayoutRequestDto("Default Layout", 2,2);
        LayoutSlotRequestDto layoutSlot11 = this.buildLayoutSlotRequestDto(1L, 2, 2, 2,1,0);
        LayoutSlotRequestDto layoutSlot12 = this.buildLayoutSlotRequestDto(1L, 22, 22, 2,1,0);

        layout1.setSlots(List.of(layoutSlot11, layoutSlot12));

        LayoutResponseDto<LayoutSlotResponseDto> saved1 = this.layoutServiceImpl.createLayout(layout1);
        List<LayoutSlotResponseDto> savedSlots1 = new ArrayList<>(saved1.getSlots());
        savedSlots1.sort(Comparator.comparing(LayoutSlotResponseDto::getColPos));

        assertLayout(saved1, "Default Layout", 2,2);
        assertEquals(2, saved1.getSlots().size());
        assertLayoutSlot(savedSlots1.get(0), saved1.getId(), 1L, 2, 2, 2,1, 0);
        assertLayoutSlot(savedSlots1.get(1), saved1.getId(), 1L, 22, 22, 2,1,0);


        // layout 2

        LayoutRequestDto<LayoutSlotRequestDto> layout2 = this.buildLayoutRequestDto("Secondary Layout", 2,2);
        LayoutSlotRequestDto layoutSlot21 = this.buildLayoutSlotRequestDto( 1L, 2, 2, 2,1,0);
        LayoutSlotRequestDto layoutSlot22 = this.buildLayoutSlotRequestDto( 1L, 22, 22, 2,1,0);



        layout2.setSlots(List.of(layoutSlot21, layoutSlot22));

        LayoutResponseDto<LayoutSlotResponseDto> saved2 = this.layoutServiceImpl.createLayout(layout2);
        List<LayoutSlotResponseDto> savedSlots2 = new ArrayList<>(saved2.getSlots());
        savedSlots2.sort(Comparator.comparing(LayoutSlotResponseDto::getColPos));

        assertLayout(saved2, "Secondary Layout", 2,2);
        assertEquals(2, saved2.getSlots().size());
        assertLayoutSlot(savedSlots2.get(0), saved2.getId(), 1L, 2, 2, 2,1, 0);
        assertLayoutSlot(savedSlots2.get(1), saved2.getId(), 1L, 22, 22, 2,1,0);



        LayoutRequestDto<LayoutSlotRequestUpdateDto> layout = this.buildLayoutRequestDto(null,null,null);
        LayoutSlotRequestUpdateDto deleteSlot1 = this.buildLayoutSlotRequestUpdateDto(savedSlots2.get(0).getId(),null, null, null, null, null, null);
        layout.setSlots(List.of(deleteSlot1));

        assertThrows(InvalidLayoutSlotException.class, () -> this.layoutSlotService.deleteLayoutSlotsByLayoutId(saved1.getId(), layout));
    }

    @Test
    void shouldThrowErrorWhenDeleteAllLayoutSlotsByLayoutId() {
        assertThrows(LayoutNotFoundException.class, () -> this.layoutSlotService.deleteAllSlotsByLayoutId(1L));
    }
}
