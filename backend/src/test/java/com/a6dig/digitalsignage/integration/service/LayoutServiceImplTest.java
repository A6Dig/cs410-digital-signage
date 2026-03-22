package com.a6dig.digitalsignage.integration.service;

import com.a6dig.digitalsignage.dto.*;
import com.a6dig.digitalsignage.entity.Layout;
import com.a6dig.digitalsignage.entity.LayoutSlot;
import com.a6dig.digitalsignage.exception.InvalidLayoutException;
import com.a6dig.digitalsignage.exception.LayoutNotFoundException;
import com.a6dig.digitalsignage.exception.LayoutSlotNotFoundException;
import com.a6dig.digitalsignage.mapper.LayoutMapper;
import com.a6dig.digitalsignage.repository.LayoutRepository;
import com.a6dig.digitalsignage.repository.LayoutSlotRepository;
import com.a6dig.digitalsignage.service.LayoutService;
import com.a6dig.digitalsignage.service.LayoutServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
//@Transactional
public class LayoutServiceImplTest {

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


    // helper methods
    private Layout buildLayout(Long id, String name, int col, int row) {
        Layout layout = new Layout();
        layout.setId(id);
        layout.setName(name);
        layout.setLayoutCol(col);
        layout.setLayoutRow(row);
        layout.setCreatedAt(LocalDateTime.now());
        layout.setUpdatedAt(LocalDateTime.now());
        layout.setLayoutSlotList(new ArrayList<>());
        return layout;
    }

    private LayoutResponseDto buildLayoutResponseDto(Long id, String name, int col, int row) {
        LayoutResponseDto dto = new LayoutResponseDto();
        dto.setId(id);
        dto.setName(name);
        dto.setLayoutCol(col);
        dto.setLayoutRow(row);
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());
        dto.setLayoutSlotList(new ArrayList<>());
        return dto;
    }



    private LayoutRequestDto buildLayoutRequestDto(String name, int col, int row) {
        LayoutRequestDto dto = new LayoutRequestDto();
        dto.setName(name);
        dto.setLayoutCol(col);
        dto.setLayoutRow(row);
        dto.setLayoutSlotRequestDtoList(new ArrayList<>());
        return dto;
    }



    private LayoutRequestUpdateDto buildLayoutRequestUpdateDto(Long id, String name, int col, int row) {
        LayoutRequestUpdateDto dto = new LayoutRequestUpdateDto();
        dto.setId(id);
        dto.setName(name);
        dto.setLayoutCol(col);
        dto.setLayoutRow(row);
        dto.setLayoutSlotRequestDtoList(new ArrayList<>());
        return dto;
    }

    private LayoutSlotRequestUpdateDto buildLayoutSlotRequestUpdateDto(Long layoutId, Long moduleId, int col, int row, int colSpan, int rowSpan, int zIndex) {
        LayoutSlotRequestUpdateDto dto = new LayoutSlotRequestUpdateDto();
        dto.setLayoutId(layoutId);
        dto.setModuleId(moduleId);
        dto.setGridCol(col);
        dto.setGridRow(row);
        dto.setColSpan(colSpan);
        dto.setRowSpan(rowSpan);
        dto.setzIndex(zIndex);
        return dto;
    }


    private LayoutSlot buildLayoutSlot(Layout layout, Long moduleId, int col, int row, int colSpan, int rowSpan, int zIndex) {
        LayoutSlot slot = new LayoutSlot(layout);
        slot.setModuleId(moduleId);
        slot.setGridCol(col);
        slot.setGridRow(row);
        slot.setColSpan(colSpan);
        slot.setRowSpan(rowSpan);
        slot.setzIndex(zIndex);
        return slot;
    }

    private LayoutSlotRequestDto buildLayoutSlotRequestDto(Long moduleId, int col, int row, int colSpan, int rowSpan, int zIndex){
        LayoutSlotRequestDto slot = new LayoutSlotRequestDto();

        slot.setModuleId(moduleId);
        slot.setGridCol(col);
        slot.setGridRow(row);
        slot.setColSpan(colSpan);
        slot.setRowSpan(rowSpan);
        slot.setzIndex(zIndex);

        return slot;
    }

    // create

    @Test
    void shouldCreateLayoutAndPersistToDatabase() {
        LayoutRequestDto requestDto = this.buildLayoutRequestDto("Main Layout", 2, 2);

        LayoutResponseDto result = this.layoutServiceImpl.createLayout(requestDto);

        assertNotNull(result.getId());
        assertEquals("Main Layout", result.getName());
        assertEquals(2, result.getLayoutCol());
        assertEquals(2, result.getLayoutRow());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());

        assertTrue(this.layoutRepository.existsById(result.getId()));
    }


    @Test
    void shouldThrowErrorWhenCreateLayoutWithInvalidData(){
        LayoutRequestDto request = this.buildLayoutRequestDto("Main Layout", 0, 1);
        request.setLayoutSlotRequestDtoList(null);
        assertThrows(InvalidLayoutException.class, () -> this.layoutServiceImpl.createLayout(request));
    }

    @Test
    void shouldCreateLayoutWIthSlotsAndPersistToDB() {
        LayoutRequestDto requestDto = this.buildLayoutRequestDto("Main Layout", 2, 2);

        requestDto.setLayoutSlotRequestDtoList(List.of(
                this.buildLayoutSlotRequestDto(null, 1, 1, 1, 1, 0),
                this.buildLayoutSlotRequestDto(null,1 , 2, 1, 1, 0)
        ));

        LayoutResponseDto result = this.layoutServiceImpl.createLayout(requestDto);

        assertNotNull(result.getId());
        assertEquals("Main Layout", result.getName());
        assertEquals(2, result.getLayoutCol());
        assertEquals(2, result.getLayoutRow());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());

        assertTrue(this.layoutRepository.existsById(result.getId()));

        assertEquals(2, result.getLayoutSlotList().size());
        assertEquals(result.getId(), result.getLayoutSlotList().get(0).getLayoutId());
        assertEquals(result.getId(), result.getLayoutSlotList().get(1).getLayoutId());

    }

    // Read

    @Test
    void shouldGetLayoutById(){
        LayoutRequestDto request = this.buildLayoutRequestDto("Main Layout", 1, 1);
        LayoutResponseDto created = this.layoutServiceImpl.createLayout(request);

        LayoutResponseDto result = this.layoutServiceImpl.getLayoutById(created.getId());

        assertNotNull(result);
        assertEquals(created.getId(), result.getId());
        assertEquals("Main Layout", result.getName());
        assertEquals(1, result.getLayoutCol());
        assertEquals(1, result.getLayoutRow());
    }

    @Test
    void shouldThrowErrorWhenLayoutNotFound() {
        assertThrows(LayoutNotFoundException.class, () -> this.layoutServiceImpl.getLayoutById(1000000L));
    }


    @Test
    void shouldGetAllLayouts() {
        this.layoutServiceImpl.createLayout(this.buildLayoutRequestDto("First Layout", 1, 1));
        this.layoutServiceImpl.createLayout(this.buildLayoutRequestDto("Second Layout", 1, 1));
        this.layoutServiceImpl.createLayout(this.buildLayoutRequestDto("Third Layout", 1, 1));

        List<LayoutResponseDto> result = this.layoutServiceImpl.getAllLayouts();

        assertEquals(3, result.size());
    }


    @Test
    void shouldReturnEmptyListWhenNoLayoutsFound() {
        List<LayoutResponseDto> result = this.layoutServiceImpl.getAllLayouts();
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnTrueWhenLayoutExists() {
        LayoutRequestDto request = this.buildLayoutRequestDto("Main Layout", 1, 1);
        LayoutResponseDto created = this.layoutServiceImpl.createLayout(request);

        assertTrue(this.layoutServiceImpl.layoutExist(created.getId()));
    }

    @Test
    void shouldReturnFalseWhenLayoutNotExist() {
        assertFalse(this.layoutServiceImpl.layoutExist(10000L));
    }

    @Test
    void shouldCountLayouts() {

        this.layoutServiceImpl.createLayout(this.buildLayoutRequestDto("First Layout", 1, 1));
        this.layoutServiceImpl.createLayout(this.buildLayoutRequestDto("Second Layout", 1, 1));
        this.layoutServiceImpl.createLayout(this.buildLayoutRequestDto("Third Layout", 1, 1));

        assertEquals(3, this.layoutRepository.count());
    }

    @Test
    void shouldGetLayoutWithSlots() {

        LayoutRequestDto request = this.buildLayoutRequestDto("Main Layout", 2, 2);

        request.setLayoutSlotRequestDtoList(List.of(
                this.buildLayoutSlotRequestDto(null,1, 1, 1, 1, 0),
                this.buildLayoutSlotRequestDto(null,1, 2, 1, 1, 0)
        ));

        LayoutResponseDto created = this.layoutServiceImpl.createLayout(request);

        LayoutResponseDto result = this.layoutServiceImpl.getLayoutById(created.getId());

        assertEquals(2, result.getLayoutSlotList().size());
        assertEquals(result.getId(), result.getLayoutSlotList().get(0).getLayoutId());
        assertEquals(result.getId(), result.getLayoutSlotList().get(1).getLayoutId());

    }

    // Update

    @Test
    void shouldUpdateLayout() {
        LayoutResponseDto created = this.layoutServiceImpl.createLayout(
                this.buildLayoutRequestDto("Main Layout", 1,1)
        );

        LayoutRequestUpdateDto updateRequest = new LayoutRequestUpdateDto();
        updateRequest.setId(created.getId());
        updateRequest.setName("Updated Layout");
        updateRequest.setLayoutCol(created.getLayoutCol());
        updateRequest.setLayoutRow(created.getLayoutRow());

        LayoutResponseDto updated = this.layoutServiceImpl.updateLayout(created.getId(), updateRequest);

        assertEquals("Updated Layout", updated.getName());
        assertEquals(1, updated.getLayoutCol());
        assertEquals(1, updated.getLayoutRow());

        Layout fromDb = this.layoutRepository.findById(created.getId()).orElseThrow();
        assertEquals("Updated Layout", fromDb.getName());
    }

    @Test
    void shouldUpdateUpdatedAtOnUpdate() throws InterruptedException {
        LayoutResponseDto created = this.layoutServiceImpl.createLayout(
                this.buildLayoutRequestDto("Main Layout", 1,1)
        );

        Thread.sleep(200);

        LayoutRequestUpdateDto updateRequest = new LayoutRequestUpdateDto();
        updateRequest.setId(created.getId());
        updateRequest.setName("Updated Layout");
        updateRequest.setLayoutCol(2);
        updateRequest.setLayoutRow(2);

        LayoutResponseDto updated = this.layoutServiceImpl.updateLayout(created.getId(), updateRequest);

        System.out.println(updated.getUpdatedAt());
        System.out.println(created.getUpdatedAt());
        assertTrue(updated.getUpdatedAt().isAfter(created.getUpdatedAt()));

    }


    @Test
    void shouldNotChangeCreatedAtOnUpdate() throws InterruptedException {
        LayoutResponseDto created = this.layoutServiceImpl.createLayout(
                this.buildLayoutRequestDto("Main Layout", 1,1)
        );

        Thread.sleep(200);

        LayoutRequestUpdateDto updateRequest = new LayoutRequestUpdateDto();
        updateRequest.setId(created.getId());
        updateRequest.setName("Updated Layout");
        updateRequest.setLayoutCol(created.getLayoutCol());
        updateRequest.setLayoutRow(created.getLayoutRow());

        LayoutResponseDto updated = this.layoutServiceImpl.updateLayout(created.getId(), updateRequest);

        assertEquals(created.getCreatedAt(), updated.getCreatedAt());

    }

    @Test
    void shouldThrowWhenUpdatingNonExistentLayout() {
        LayoutRequestUpdateDto updateRequest = new LayoutRequestUpdateDto();
        updateRequest.setId(1000L);
        updateRequest.setName("Main Layout");
        updateRequest.setLayoutCol(1);
        updateRequest.setLayoutRow(1);

        assertThrows(LayoutNotFoundException.class, () -> this.layoutServiceImpl.updateLayout(1000L, updateRequest));
    }



    @Test
    void shouldThrowErrorWhenUpdateLayoutWithInvalidData(){
        LayoutResponseDto created = this.layoutServiceImpl.createLayout(
                this.buildLayoutRequestDto("Main Layout", 1,1)
        );

        LayoutRequestUpdateDto updateRequest = new LayoutRequestUpdateDto();
        updateRequest.setId(created.getId());
        updateRequest.setName("Updated Layout");
        updateRequest.setLayoutCol(0);
        updateRequest.setLayoutRow(created.getLayoutRow());


        assertThrows(InvalidLayoutException.class, () -> layoutServiceImpl.updateLayout(created.getId(), updateRequest));
    }


    // Delete

    @Test
    void shouldDeleteLayout() {
        LayoutResponseDto created = this.layoutServiceImpl.createLayout(
                this.buildLayoutRequestDto("Main Layout", 1, 1)
        );

        this.layoutServiceImpl.deleteLayout(created.getId());

        assertFalse(this.layoutRepository.existsById(created.getId()));

    }

    @Test
    void shouldDeleteLayoutAndCascadeSlots() {

        LayoutRequestDto request = this.buildLayoutRequestDto("Main Layout", 2, 2);

        request.setLayoutSlotRequestDtoList(List.of(
                this.buildLayoutSlotRequestDto(null,1, 1, 1, 1, 0),
                this.buildLayoutSlotRequestDto(null,1, 2, 1, 1, 0)
        ));

        LayoutResponseDto created = this.layoutServiceImpl.createLayout(request);

        List<Long> slotIds = created.getLayoutSlotList().stream().map(LayoutSlotResponseDto::getId).toList();

        this.layoutServiceImpl.deleteLayout(created.getId());

        assertFalse(this.layoutRepository.existsById(created.getId()));

        slotIds.forEach(slotId -> {
            assertFalse(this.layoutSlotRepository.existsById(slotId));
        });

    }

    @Test
    void shouldThrowErrorWhenDeletingNonExistingLayout() {
        assertThrows(LayoutNotFoundException.class, () -> this.layoutServiceImpl.deleteLayout(10000L));
    }


    // Dependencies - Slots

    @Test
    @Transactional
    void shouldAddSlotToExistingLayout() {
        LayoutResponseDto created = this.layoutServiceImpl.createLayout(
                this.buildLayoutRequestDto("Main Layout", 1, 1)
        );

        LayoutResponseDto result = this.layoutServiceImpl.addLayoutSlotToLayout(
                created.getId(),
                this.buildLayoutSlotRequestDto(1L, 1,1,1,1,0)
        );

        assertEquals(1, result.getLayoutSlotList().size());

        Layout fromDb = this.layoutRepository.findById(created.getId()).orElseThrow();
        assertEquals(1, fromDb.getLayoutSlotList().size());
    }

    @Test
    @Transactional
    void shouldRemoveSlotFromLayout() {
        LayoutRequestDto request = this.buildLayoutRequestDto("Main Layout", 3, 3);
        request.setLayoutSlotRequestDtoList(List.of(
                this.buildLayoutSlotRequestDto(1L, 1,1,1,1, 0),
                this.buildLayoutSlotRequestDto(1L, 1,1,1,1, 0)
        ));

        LayoutResponseDto created = this.layoutServiceImpl.createLayout(request);
        Long slotId = created.getLayoutSlotList().get(0).getId();

        this.layoutServiceImpl.removeLayoutSlotFromLayout(created.getId(), slotId);

        Layout fromDb = this.layoutRepository.findById(created.getId()).orElseThrow();
        assertEquals(1, fromDb.getLayoutSlotList().size());
        assertFalse(this.layoutSlotRepository.existsById(slotId));


    }

    @Test
    void shouldThrowWhenAddingSlotToNonExistingLayout() {
        assertThrows(LayoutNotFoundException.class, () -> this.layoutServiceImpl.addLayoutSlotToLayout(1000L,
                this.buildLayoutSlotRequestDto(1L, 1,1,1,1, 0)
                ));
    }

    @Test
    void shouldThrowWhenRemovingNonExistingLayoutSlot() {

        LayoutResponseDto created = this.layoutServiceImpl.createLayout(
                this.buildLayoutRequestDto("Main Layout", 1, 1)
        );

        assertThrows(LayoutSlotNotFoundException.class, () -> this.layoutServiceImpl.removeLayoutSlotFromLayout(created.getId(),100L));
    }



}
