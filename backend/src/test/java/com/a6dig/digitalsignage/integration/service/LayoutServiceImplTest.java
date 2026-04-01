package com.a6dig.digitalsignage.integration.service;

import com.a6dig.digitalsignage.dto.*;
import com.a6dig.digitalsignage.entity.Layout;
import com.a6dig.digitalsignage.entity.LayoutSlot;
import com.a6dig.digitalsignage.entity.Module;
import com.a6dig.digitalsignage.exception.InvalidLayoutException;
import com.a6dig.digitalsignage.exception.InvalidLayoutSlotException;
import com.a6dig.digitalsignage.exception.LayoutNotFoundException;
import com.a6dig.digitalsignage.exception.LayoutSlotNotFoundException;
import com.a6dig.digitalsignage.repository.LayoutRepository;
import com.a6dig.digitalsignage.repository.LayoutSlotRepository;
import com.a6dig.digitalsignage.service.LayoutServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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


<<<<<<< HEAD
    private <T extends LayoutSlotRequestDto>LayoutRequestDto<T> buildLayoutRequestDto(String name, int col, int row) {
=======
    private <T extends LayoutSlotRequestDto>LayoutRequestDto<T> buildLayoutRequestDto(String name, Integer col, Integer row) {
>>>>>>> 28ec52b383dd2e358d2e5711391f9e0f9f3feb92
        LayoutRequestDto<T> dto = new LayoutRequestDto<>();
        dto.setName(name);
        dto.setCols(col);
        dto.setRows(row);
        dto.setSlots(new ArrayList<>());
        return dto;
    }


<<<<<<< HEAD
    private LayoutSlotRequestDto buildLayoutSlotRequestDto(Long moduleId, int colPos, int rowPos, int colSpan, int rowSpan, int zIndex){
=======
    private LayoutSlotRequestDto buildLayoutSlotRequestDto(Long moduleId, Integer colPos, Integer rowPos, Integer colSpan, Integer rowSpan, Integer zIndex){
>>>>>>> 28ec52b383dd2e358d2e5711391f9e0f9f3feb92
        LayoutSlotRequestDto slot = new LayoutSlotRequestDto();

        slot.setModuleId(moduleId);
        slot.setColPos(colPos);
        slot.setRowPos(rowPos);
        slot.setColSpan(colSpan);
        slot.setRowSpan(rowSpan);
        slot.setzIndex(zIndex);

        return slot;
    }


<<<<<<< HEAD
    private LayoutSlotRequestUpdateDto buildLayoutSlotRequestUpdateDto(Long id, Long moduleId, int colPos, int rowPos, int colSpan, int rowSpan, int zIndex){
=======
    private LayoutSlotRequestUpdateDto buildLayoutSlotRequestUpdateDto(Long id, Long moduleId, Integer colPos, Integer rowPos, Integer colSpan, Integer rowSpan, Integer zIndex){
>>>>>>> 28ec52b383dd2e358d2e5711391f9e0f9f3feb92
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

<<<<<<< HEAD
    private void assertLayout(LayoutResponseDto<LayoutSlotResponseDto> layout, String expectedName, int expectedCols, int expectedRows) {
        assertNotNull(layout.getId());
        assertNotNull(layout.getName());
=======
    private void assertLayout(LayoutResponseDto<LayoutSlotResponseDto> layout, String expectedName, Integer expectedCols, Integer expectedRows) {
        assertNotNull(layout.getId());
        assertNotNull(layout.getName());
        assertNotNull(layout.getCols());
        assertNotNull(layout.getRows());
>>>>>>> 28ec52b383dd2e358d2e5711391f9e0f9f3feb92
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
<<<<<<< HEAD
            , int expectedColPos
            , int expectedRowPos
            , int expectedColSpan
            , int expectedRowSpan
            , int expectedzIndex) {
        assertNotNull(slot.getId());
        assertNotNull(slot.getLayoutId());
=======
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
>>>>>>> 28ec52b383dd2e358d2e5711391f9e0f9f3feb92
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

    // create

    @Test
    void shouldCreateLayoutAndPersistToDatabase() {
        LayoutRequestDto<LayoutSlotRequestDto> requestDto = this.buildLayoutRequestDto("Main Layout", 2, 2);
        LayoutResponseDto<LayoutSlotResponseDto> result = this.layoutServiceImpl.createLayout(requestDto);
        assertLayout(result, "Main Layout", 2, 2);
    }


    @Test
    void shouldThrowErrorWhenCreateLayoutWithInvalidData(){
        LayoutRequestDto<LayoutSlotRequestDto> request = this.buildLayoutRequestDto("Main Layout", 0, 1);
        request.setSlots(null);
        assertThrows(InvalidLayoutException.class, () -> this.layoutServiceImpl.createLayout(request));
    }

    @Test
    void shouldCreateLayoutWIthSlotsAndPersistToDB() {
        LayoutRequestDto<LayoutSlotRequestDto> requestDto = this.buildLayoutRequestDto("Main Layout", 2, 2);

        requestDto.setSlots(List.of(
                this.buildLayoutSlotRequestDto(null, 1, 1, 1, 1, 0),
                this.buildLayoutSlotRequestDto(1L,1 , 2, 1, 1, 0)
        ));

        LayoutResponseDto<LayoutSlotResponseDto> result = this.layoutServiceImpl.createLayout(requestDto);

        List<LayoutSlotResponseDto> savedSlots = new ArrayList<>(result.getSlots());
        savedSlots.sort(Comparator.comparing(LayoutSlotResponseDto::getRowPos));
        LayoutSlotResponseDto savedSlot1 = savedSlots.get(0);
        LayoutSlotResponseDto savedSlot2 = savedSlots.get(1);

        assertLayout(result, "Main Layout", 2, 2);
        assertEquals(2, result.getSlots().size());
        assertLayoutSlot(savedSlot1, result.getId(), null,1, 1, 1, 1, 0);
<<<<<<< HEAD
        assertLayoutSlot(savedSlot2, result.getId(), 1L,1, 2, 1, 1, 0);
=======
        assertLayoutSlot(savedSlot2, result.getId(), null,1, 2, 1, 1, 0);

    }
>>>>>>> 28ec52b383dd2e358d2e5711391f9e0f9f3feb92


    @Test
    void shouldThrowErrorWhenCreateLayoutWithInvalidDataColValueNull(){
        assertThrows(InvalidLayoutException.class, () -> layoutServiceImpl.createLayout(this.buildLayoutRequestDto("Main Layout", null,1)));
    }

    @Test
    void shouldThrowErrorWhenCreateLayoutWithInvalidDataColValueNegative(){
        assertThrows(InvalidLayoutException.class, () -> layoutServiceImpl.createLayout(this.buildLayoutRequestDto("Main Layout", -1,1)));
    }


    @Test
    void shouldThrowErrorWhenCreateLayoutWithInvalidDataColValueZero(){
        assertThrows(InvalidLayoutException.class, () -> layoutServiceImpl.createLayout(this.buildLayoutRequestDto("Main Layout", 0,1)));
    }



    @Test
    void shouldThrowErrorWhenCreateLayoutWithInvalidDataRowValueNull(){
        assertThrows(InvalidLayoutException.class, () -> layoutServiceImpl.createLayout(this.buildLayoutRequestDto("Main Layout", 1,null)));
    }

    @Test
    void shouldThrowErrorWhenCreateLayoutWithInvalidDataRowValueNegative(){
        assertThrows(InvalidLayoutException.class, () -> layoutServiceImpl.createLayout(this.buildLayoutRequestDto("Main Layout", 1,-1)));
    }


    @Test
    void shouldThrowErrorWhenCreateLayoutWithInvalidDataRowValueZero(){
        assertThrows(InvalidLayoutException.class, () -> layoutServiceImpl.createLayout(this.buildLayoutRequestDto("Main Layout", 1,0)));
    }

    // Read

    @Test
    void shouldGetLayoutById(){
        LayoutRequestDto<LayoutSlotRequestDto> request = this.buildLayoutRequestDto("Main Layout", 1, 1);
        LayoutResponseDto<LayoutSlotResponseDto> created = this.layoutServiceImpl.createLayout(request);
        LayoutResponseDto<LayoutSlotResponseDto> result = this.layoutServiceImpl.getLayoutById(created.getId());
        assertLayout(result, "Main Layout", 1, 1);
    }

    @Test
    void shouldThrowErrorWhenLayoutNotFound() {
        assertThrows(LayoutNotFoundException.class, () -> this.layoutServiceImpl.getLayoutById(1000000L));
    }


    @Test
    void shouldGetAllLayouts() {
        this.layoutServiceImpl.createLayout(this.buildLayoutRequestDto("Layout 1", 1, 1));
        this.layoutServiceImpl.createLayout(this.buildLayoutRequestDto("Layout 2", 1, 1));
        this.layoutServiceImpl.createLayout(this.buildLayoutRequestDto("Layout 3", 1, 1));
        List<LayoutResponseDto<LayoutSlotResponseDto>> result = this.layoutServiceImpl.getAllLayouts();
        result.sort(Comparator.comparing(LayoutResponseDto::getName));

        assertEquals(3, result.size());
        assertLayout(result.get(0), "Layout 1", 1, 1);
        assertLayout(result.get(1), "Layout 2", 1, 1);
        assertLayout(result.get(2), "Layout 3", 1, 1);
    }


    @Test
    void shouldReturnEmptyListWhenNoLayoutsFound() {
        List<LayoutResponseDto<LayoutSlotResponseDto>> result = this.layoutServiceImpl.getAllLayouts();
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnTrueWhenLayoutExists() {
        LayoutRequestDto<LayoutSlotRequestDto> request = this.buildLayoutRequestDto("Main Layout", 1, 1);
        LayoutResponseDto<LayoutSlotResponseDto> created = this.layoutServiceImpl.createLayout(request);
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

        LayoutRequestDto<LayoutSlotRequestDto> request = this.buildLayoutRequestDto("Main Layout", 2, 2);

        request.setSlots(List.of(
                this.buildLayoutSlotRequestDto(null,1, 1, 1, 1, 0),
                this.buildLayoutSlotRequestDto(1L,1, 2, 1, 1, 0)
        ));

        LayoutResponseDto<LayoutSlotResponseDto> created = this.layoutServiceImpl.createLayout(request);
        LayoutResponseDto<LayoutSlotResponseDto> result = this.layoutServiceImpl.getLayoutById(created.getId());

        List<LayoutSlotResponseDto> savedSlots = new ArrayList<>(result.getSlots());
        savedSlots.sort(Comparator.comparing(LayoutSlotResponseDto::getRowPos));

        assertLayout(result, "Main Layout", 2, 2);
        assertLayoutSlot(savedSlots.get(0), result.getId(),null,1, 1, 1, 1, 0);
<<<<<<< HEAD
        assertLayoutSlot(savedSlots.get(1), result.getId(),1L,1, 2, 1, 1, 0);
=======
        assertLayoutSlot(savedSlots.get(1), result.getId(),null,1, 2, 1, 1, 0);
>>>>>>> 28ec52b383dd2e358d2e5711391f9e0f9f3feb92

    }

    // Update

    @Test
    void shouldUpdateLayout() {
        LayoutResponseDto<LayoutSlotResponseDto> created = this.layoutServiceImpl.createLayout(
                this.buildLayoutRequestDto("Main Layout", 1,1)
        );
        LayoutRequestDto<LayoutSlotRequestUpdateDto> updateRequest = this.buildLayoutRequestDto("Updated Layout", 1,1);
        LayoutResponseDto<LayoutSlotResponseDto> updated = this.layoutServiceImpl.updateLayout(created.getId(), updateRequest);
        assertLayout(updated, "Updated Layout", 1,1);
    }

//
//
//    @Test
//    void shouldUpdateLayoutWithNewLayoutSlot() {
//        LayoutResponseDto<LayoutSlotResponseDto> created = this.layoutServiceImpl.createLayout(
//                this.buildLayoutRequestDto("Main Layout", 1,1)
//        );
//
//        LayoutSlotRequestUpdateDto slot = this.buildLayoutSlotRequestDto(1L,1,1,1,1,0);
//
//
//        LayoutRequestDto<LayoutSlotRequestUpdateDto> updateRequest = this.buildLayoutRequestDto("Updated Layout", 1,1);
//        updateRequest.setSlots(List.of(slot));
//
//        LayoutResponseDto<LayoutSlotResponseDto> updated = this.layoutServiceImpl.updateLayout(created.getId(), updateRequest);
//        assertLayout(updated, "Updated Layout", 1,1);
//    }

    @Test
    void shouldOnlyUpdateNonNullableLayoutProperties() {
        LayoutResponseDto<LayoutSlotResponseDto> created = this.layoutServiceImpl.createLayout(
                this.buildLayoutRequestDto("Main Layout", 1, 1)
        );
        LayoutRequestDto<LayoutSlotRequestUpdateDto> updatedRequest = this.buildLayoutRequestDto("Updated Layout", null, null);
        LayoutResponseDto<LayoutSlotResponseDto> updated = this.layoutServiceImpl.updateLayout(created.getId(), updatedRequest);

        assertLayout(updated, "Updated Layout", 1, 1);
    }


//
//
//    @Test
//    void shouldUpdateLayoutWithNewLayoutSlot() {
//        LayoutResponseDto<LayoutSlotResponseDto> created = this.layoutServiceImpl.createLayout(
//                this.buildLayoutRequestDto("Main Layout", 1,1)
//        );
//
//        LayoutSlotRequestUpdateDto slot = this.buildLayoutSlotRequestDto(1L,1,1,1,1,0);
//
//
//        LayoutRequestDto<LayoutSlotRequestUpdateDto> updateRequest = this.buildLayoutRequestDto("Updated Layout", 1,1);
//        updateRequest.setSlots(List.of(slot));
//
//        LayoutResponseDto<LayoutSlotResponseDto> updated = this.layoutServiceImpl.updateLayout(created.getId(), updateRequest);
//        assertLayout(updated, "Updated Layout", 1,1);
//    }

    @Test
    void shouldUpdateUpdatedAtOnUpdate() throws InterruptedException {
        LayoutResponseDto<LayoutSlotResponseDto> created = this.layoutServiceImpl.createLayout(
                this.buildLayoutRequestDto("Main Layout", 1,1)
        );
        Thread.sleep(200);
        LayoutRequestDto<LayoutSlotRequestUpdateDto> updateRequest = this.buildLayoutRequestDto("Updated Layout", 1,1);
        LayoutResponseDto<LayoutSlotResponseDto> updated = this.layoutServiceImpl.updateLayout(created.getId(), updateRequest);
        assertTrue(updated.getUpdatedAt().isAfter(created.getUpdatedAt()));

    }


    @Test
    void shouldNotChangeCreatedAtOnUpdate() throws InterruptedException {
        LayoutResponseDto<LayoutSlotResponseDto> created = this.layoutServiceImpl.createLayout(
                this.buildLayoutRequestDto("Main Layout", 1,1)
        );

        Thread.sleep(200);
        LayoutRequestDto<LayoutSlotRequestUpdateDto> updateRequest = this.buildLayoutRequestDto("Updated Layout", 1,1);
        LayoutResponseDto<LayoutSlotResponseDto> updated = this.layoutServiceImpl.updateLayout(created.getId(), updateRequest);
        assertEquals(created.getCreatedAt(), updated.getCreatedAt());

    }

    @Test
    void shouldThrowWhenUpdatingNonExistentLayout() {
        LayoutRequestDto<LayoutSlotRequestUpdateDto> updateRequest = this.buildLayoutRequestDto("Updated Layout", 1,1);
        assertThrows(LayoutNotFoundException.class, () -> this.layoutServiceImpl.updateLayout(1000L, updateRequest));
    }



    @Test
    void shouldThrowErrorWhenUpdateLayoutWithInvalidDataColValueZero(){
        LayoutResponseDto<LayoutSlotResponseDto> created = this.layoutServiceImpl.createLayout(
                this.buildLayoutRequestDto("Main Layout", 1,1)
        );
        LayoutRequestDto<LayoutSlotRequestUpdateDto> updateRequest = this.buildLayoutRequestDto("Updated Layout", 0,1);
        assertThrows(InvalidLayoutException.class, () -> layoutServiceImpl.updateLayout(created.getId(), updateRequest));
    }

    @Test
    void shouldThrowErrorWhenUpdateLayoutWithInvalidDataColValueNegative(){
        LayoutResponseDto<LayoutSlotResponseDto> created = this.layoutServiceImpl.createLayout(
                this.buildLayoutRequestDto("Main Layout", 1,1)
        );
        LayoutRequestDto<LayoutSlotRequestUpdateDto> updateRequest = this.buildLayoutRequestDto("Updated Layout", -1,1);
        assertThrows(InvalidLayoutException.class, () -> layoutServiceImpl.updateLayout(created.getId(), updateRequest));
    }
    @Test
    void shouldThrowErrorWhenUpdateLayoutWithInvalidDataRowValueZero(){
        LayoutResponseDto<LayoutSlotResponseDto> created = this.layoutServiceImpl.createLayout(
                this.buildLayoutRequestDto("Main Layout", 1,1)
        );
        LayoutRequestDto<LayoutSlotRequestUpdateDto> updateRequest = this.buildLayoutRequestDto("Updated Layout", 1,0);
        assertThrows(InvalidLayoutException.class, () -> layoutServiceImpl.updateLayout(created.getId(), updateRequest));
    }

    @Test
    void shouldThrowErrorWhenUpdateLayoutWithInvalidDataRowValueNegative(){
        LayoutResponseDto<LayoutSlotResponseDto> created = this.layoutServiceImpl.createLayout(
                this.buildLayoutRequestDto("Main Layout", 1,1)
        );
        LayoutRequestDto<LayoutSlotRequestUpdateDto> updateRequest = this.buildLayoutRequestDto("Updated Layout", 1,-1);
        assertThrows(InvalidLayoutException.class, () -> layoutServiceImpl.updateLayout(created.getId(), updateRequest));
    }

    @Test void shouldThrowErrorWhenUpdateLayoutWithLayoutSlotsThatDontBelongToTheLayout() {
        LayoutRequestDto<LayoutSlotRequestDto> layout1 = this.buildLayoutRequestDto("Main Layout", 1, 1);
<<<<<<< HEAD
        layout1.setSlots(List.of(this.buildLayoutSlotRequestDto(1L, 1,1,1,1,0)));
        LayoutResponseDto<LayoutSlotResponseDto> savedLayout1 = this.layoutServiceImpl.createLayout(layout1);

        LayoutRequestDto<LayoutSlotRequestDto> layout2 = this.buildLayoutRequestDto("Secondary Layout", 1, 1);
        layout2.setSlots(List.of(this.buildLayoutSlotRequestDto(2L, 1,1,1,1,0)));
=======
        layout1.setSlots(List.of(this.buildLayoutSlotRequestDto(null, 1,1,1,1,0)));
        LayoutResponseDto<LayoutSlotResponseDto> savedLayout1 = this.layoutServiceImpl.createLayout(layout1);

        LayoutRequestDto<LayoutSlotRequestDto> layout2 = this.buildLayoutRequestDto("Secondary Layout", 1, 1);
        layout2.setSlots(List.of(this.buildLayoutSlotRequestDto(null, 1,1,1,1,0)));
>>>>>>> 28ec52b383dd2e358d2e5711391f9e0f9f3feb92
        LayoutResponseDto<LayoutSlotResponseDto> savedLayout2 = this.layoutServiceImpl.createLayout(layout2);


        LayoutRequestDto<LayoutSlotRequestUpdateDto> request = this.buildLayoutRequestDto("Updated Layout", 1, 1);
        List<LayoutSlotRequestUpdateDto> slots = new ArrayList<>();
<<<<<<< HEAD
        slots.add(this.buildLayoutSlotRequestUpdateDto(savedLayout1.getSlots().get(0).getId(), 1L, 1,1,1,1,0));
        slots.add(this.buildLayoutSlotRequestUpdateDto(savedLayout2.getSlots().get(0).getId(), 1L, 1,1,1,1,0));
=======
        slots.add(this.buildLayoutSlotRequestUpdateDto(savedLayout1.getSlots().get(0).getId(), null, 1,1,1,1,0));
        slots.add(this.buildLayoutSlotRequestUpdateDto(savedLayout2.getSlots().get(0).getId(), null, 1,1,1,1,0));
>>>>>>> 28ec52b383dd2e358d2e5711391f9e0f9f3feb92
        request.setSlots(slots);

        assertThrows(InvalidLayoutSlotException.class, () -> this.layoutServiceImpl.updateLayout(savedLayout2.getId(), request));


    }

    // Delete

    @Test
    void shouldDeleteLayout() {
        LayoutResponseDto<LayoutSlotResponseDto> created = this.layoutServiceImpl.createLayout(
                this.buildLayoutRequestDto("Main Layout", 1, 1)
        );
        this.layoutServiceImpl.deleteLayout(created.getId());
        assertFalse(this.layoutRepository.existsById(created.getId()));

    }

    @Test
    void shouldDeleteLayoutAndCascadeSlots() {

        LayoutRequestDto<LayoutSlotRequestDto> request = this.buildLayoutRequestDto("Main Layout", 2, 2);
        request.setSlots(List.of(
                this.buildLayoutSlotRequestDto(null,1, 1, 1, 1, 0),
                this.buildLayoutSlotRequestDto(1L,1, 2, 1, 1, 0)
        ));

        LayoutResponseDto<LayoutSlotResponseDto> created = this.layoutServiceImpl.createLayout(request);
        List<Long> slotIds = created.getSlots().stream().map(LayoutSlotResponseDto::getId).toList();
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
        LayoutResponseDto<LayoutSlotResponseDto> created = this.layoutServiceImpl.createLayout(
                this.buildLayoutRequestDto("Main Layout", 1, 1)
        );

        LayoutResponseDto<LayoutSlotResponseDto> result = this.layoutServiceImpl.addLayoutSlotToLayout(
                created.getId(),
                this.buildLayoutSlotRequestDto(null, 1,1,1,1,0)
        );

        assertEquals(1, result.getSlots().size());
<<<<<<< HEAD
        assertLayoutSlot(result.getSlots().get(0), result.getId(), 1L, 1,1,1,1,0);
=======
        assertLayoutSlot(result.getSlots().get(0), result.getId(), null, 1,1,1,1,0);
>>>>>>> 28ec52b383dd2e358d2e5711391f9e0f9f3feb92
    }

    @Test
    @Transactional
    void shouldRemoveSlotFromLayout() {
        LayoutRequestDto<LayoutSlotRequestDto> request = this.buildLayoutRequestDto("Main Layout", 3, 3);
        request.setSlots(List.of(
<<<<<<< HEAD
                this.buildLayoutSlotRequestDto(1L, 1,1,1,1, 0),
                this.buildLayoutSlotRequestDto(1L, 1,1,1,1, 0)
=======
                this.buildLayoutSlotRequestDto(null, 1,1,1,1, 0),
                this.buildLayoutSlotRequestDto(null, 1,1,1,1, 0)
>>>>>>> 28ec52b383dd2e358d2e5711391f9e0f9f3feb92
        ));

        LayoutResponseDto<LayoutSlotResponseDto> created = this.layoutServiceImpl.createLayout(request);
        Long slotId = created.getSlots().get(0).getId();

        this.layoutServiceImpl.removeLayoutSlotFromLayout(created.getId(), slotId);

        Layout fromDb = this.layoutRepository.findById(created.getId()).orElseThrow();
        assertEquals(1, fromDb.getSlots().size());
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

        LayoutResponseDto<LayoutSlotResponseDto> created = this.layoutServiceImpl.createLayout(
                this.buildLayoutRequestDto("Main Layout", 1, 1)
        );

        assertThrows(LayoutSlotNotFoundException.class, () -> this.layoutServiceImpl.removeLayoutSlotFromLayout(created.getId(),100L));
    }



    @Test
    void shouldOnlyUpdateNonNullableLayoutSlotsProperties() {

        LayoutRequestDto<LayoutSlotRequestDto> layout = this.buildLayoutRequestDto("Main Layout", 1, 1);
        layout.setSlots(List.of(this.buildLayoutSlotRequestDto(null, 2,3,4,1,0)));
        LayoutResponseDto<LayoutSlotResponseDto> savedLayout = this.layoutServiceImpl.createLayout(layout);
        LayoutSlotResponseDto savedSlot = savedLayout.getSlots().get(0);

        LayoutRequestDto<LayoutSlotRequestUpdateDto> updatedRequest = this.buildLayoutRequestDto("Updated Layout", 1, 1);
        updatedRequest.setSlots(
                List.of(
                        this.buildLayoutSlotRequestUpdateDto(savedSlot.getId(),null,null,1,1,1,1)
                )
        );

        LayoutResponseDto<LayoutSlotResponseDto> updated = this.layoutServiceImpl.updateLayout(savedLayout.getId(), updatedRequest);
        LayoutSlotResponseDto updatedLayoutSlot = updated.getSlots().get(0);

        assertLayout(updated, "Updated Layout", 1, 1);
        assertLayoutSlot(updatedLayoutSlot, updated.getId(), null,2,1,1,1,1);


    }



    // slot col pos
    @Test
    void shouldThrowErrorWhenCreateOrUpdateLayoutSlotWithInvalidDataColPosValueNull(){
        LayoutResponseDto<LayoutSlotResponseDto> created = this.layoutServiceImpl.createLayout(
                this.buildLayoutRequestDto("Main Layout", 1,1)
        );
        LayoutRequestDto<LayoutSlotRequestUpdateDto> updateRequest = this.buildLayoutRequestDto("Updated Layout", 1,1);


        updateRequest.setSlots(
                List.of(
                        this.buildLayoutSlotRequestUpdateDto(null,null,null,1,1,1,1)
                )
        );

        assertThrows(InvalidLayoutSlotException.class, () -> layoutServiceImpl.updateLayout(created.getId(), updateRequest));
    }


    @Test
    void shouldThrowErrorWhenCreateOrUpdateLayoutSlotWithInvalidDataColPosValueNegative(){
        LayoutResponseDto<LayoutSlotResponseDto> created = this.layoutServiceImpl.createLayout(
                this.buildLayoutRequestDto("Main Layout", 1,1)
        );
        LayoutRequestDto<LayoutSlotRequestUpdateDto> updateRequest = this.buildLayoutRequestDto("Updated Layout", 1,1);


        updateRequest.setSlots(
                List.of(
                        this.buildLayoutSlotRequestUpdateDto(null,null,-1,1,1,1,1)
                )
        );

        assertThrows(InvalidLayoutSlotException.class, () -> layoutServiceImpl.updateLayout(created.getId(), updateRequest));
    }


    @Test
    void shouldThrowErrorWhenCreateOrUpdateLayoutSlotWithInvalidDataColPosValueZero(){
        LayoutResponseDto<LayoutSlotResponseDto> created = this.layoutServiceImpl.createLayout(
                this.buildLayoutRequestDto("Main Layout", 1,1)
        );
        LayoutRequestDto<LayoutSlotRequestUpdateDto> updateRequest = this.buildLayoutRequestDto("Updated Layout", 1,1);


        updateRequest.setSlots(
                List.of(
                        this.buildLayoutSlotRequestUpdateDto(null,null,0,1,1,1,1)
                )
        );

        assertThrows(InvalidLayoutSlotException.class, () -> layoutServiceImpl.updateLayout(created.getId(), updateRequest));
    }



    // slot row pos
    @Test
    void shouldThrowErrorWhenCreateOrUpdateLayoutSlotWithInvalidDataRowPosValueNull(){
        LayoutResponseDto<LayoutSlotResponseDto> created = this.layoutServiceImpl.createLayout(
                this.buildLayoutRequestDto("Main Layout", 1,1)
        );
        LayoutRequestDto<LayoutSlotRequestUpdateDto> updateRequest = this.buildLayoutRequestDto("Updated Layout", 1,1);


        updateRequest.setSlots(
                List.of(
                        this.buildLayoutSlotRequestUpdateDto(null,null,1,null,1,1,1)
                )
        );

        assertThrows(InvalidLayoutSlotException.class, () -> layoutServiceImpl.updateLayout(created.getId(), updateRequest));
    }


    @Test
    void shouldThrowErrorWhenCreateOrUpdateLayoutSlotWithInvalidDataRowPosValueNegative(){
        LayoutResponseDto<LayoutSlotResponseDto> created = this.layoutServiceImpl.createLayout(
                this.buildLayoutRequestDto("Main Layout", 1,1)
        );
        LayoutRequestDto<LayoutSlotRequestUpdateDto> updateRequest = this.buildLayoutRequestDto("Updated Layout", 1,1);


        updateRequest.setSlots(
                List.of(
                        this.buildLayoutSlotRequestUpdateDto(null,null,1,-1,1,1,1)
                )
        );

        assertThrows(InvalidLayoutSlotException.class, () -> layoutServiceImpl.updateLayout(created.getId(), updateRequest));
    }


    @Test
    void shouldThrowErrorWhenCreateOrUpdateLayoutSlotWithInvalidDataRowPosValueZero(){
        LayoutResponseDto<LayoutSlotResponseDto> created = this.layoutServiceImpl.createLayout(
                this.buildLayoutRequestDto("Main Layout", 1,1)
        );
        LayoutRequestDto<LayoutSlotRequestUpdateDto> updateRequest = this.buildLayoutRequestDto("Updated Layout", 1,1);


        updateRequest.setSlots(
                List.of(
                        this.buildLayoutSlotRequestUpdateDto(null,null,1,0,1,1,1)
                )
        );

        assertThrows(InvalidLayoutSlotException.class, () -> layoutServiceImpl.updateLayout(created.getId(), updateRequest));
    }





    // slot col span
    @Test
    void shouldThrowErrorWhenCreateOrUpdateLayoutSlotWithInvalidDataColSpanValueNull(){
        LayoutResponseDto<LayoutSlotResponseDto> created = this.layoutServiceImpl.createLayout(
                this.buildLayoutRequestDto("Main Layout", 1,1)
        );
        LayoutRequestDto<LayoutSlotRequestUpdateDto> updateRequest = this.buildLayoutRequestDto("Updated Layout", 1,1);


        updateRequest.setSlots(
                List.of(
                        this.buildLayoutSlotRequestUpdateDto(null,null,1,1,null,1,1)
                )
        );

        assertThrows(InvalidLayoutSlotException.class, () -> layoutServiceImpl.updateLayout(created.getId(), updateRequest));
    }


    @Test
    void shouldThrowErrorWhenCreateOrUpdateLayoutSlotWithInvalidDataColSpanValueNegative(){
        LayoutResponseDto<LayoutSlotResponseDto> created = this.layoutServiceImpl.createLayout(
                this.buildLayoutRequestDto("Main Layout", 1,1)
        );
        LayoutRequestDto<LayoutSlotRequestUpdateDto> updateRequest = this.buildLayoutRequestDto("Updated Layout", 1,1);


        updateRequest.setSlots(
                List.of(
                        this.buildLayoutSlotRequestUpdateDto(null,null,1,1,-1,1,1)
                )
        );

        assertThrows(InvalidLayoutSlotException.class, () -> layoutServiceImpl.updateLayout(created.getId(), updateRequest));
    }


    @Test
    void shouldThrowErrorWhenCreateOrUpdateLayoutSlotWithInvalidDataColSpanValueZero(){
        LayoutResponseDto<LayoutSlotResponseDto> created = this.layoutServiceImpl.createLayout(
                this.buildLayoutRequestDto("Main Layout", 1,1)
        );
        LayoutRequestDto<LayoutSlotRequestUpdateDto> updateRequest = this.buildLayoutRequestDto("Updated Layout", 1,1);


        updateRequest.setSlots(
                List.of(
                        this.buildLayoutSlotRequestUpdateDto(null,null,1,1,0,1,1)
                )
        );

        assertThrows(InvalidLayoutSlotException.class, () -> layoutServiceImpl.updateLayout(created.getId(), updateRequest));
    }



    // slot row span
    @Test
    void shouldThrowErrorWhenCreateOrUpdateLayoutSlotWithInvalidDataRowSpanValueNull(){
        LayoutResponseDto<LayoutSlotResponseDto> created = this.layoutServiceImpl.createLayout(
                this.buildLayoutRequestDto("Main Layout", 1,1)
        );
        LayoutRequestDto<LayoutSlotRequestUpdateDto> updateRequest = this.buildLayoutRequestDto("Updated Layout", 1,1);


        updateRequest.setSlots(
                List.of(
                        this.buildLayoutSlotRequestUpdateDto(null,null,1,1,1,null,1)
                )
        );

        assertThrows(InvalidLayoutSlotException.class, () -> layoutServiceImpl.updateLayout(created.getId(), updateRequest));
    }


    @Test
    void shouldThrowErrorWhenCreateOrUpdateLayoutSlotWithInvalidDataRowSpanValueNegative(){
        LayoutResponseDto<LayoutSlotResponseDto> created = this.layoutServiceImpl.createLayout(
                this.buildLayoutRequestDto("Main Layout", 1,1)
        );
        LayoutRequestDto<LayoutSlotRequestUpdateDto> updateRequest = this.buildLayoutRequestDto("Updated Layout", 1,1);


        updateRequest.setSlots(
                List.of(
                        this.buildLayoutSlotRequestUpdateDto(null,null,1,1,1,-1,1)
                )
        );

        assertThrows(InvalidLayoutSlotException.class, () -> layoutServiceImpl.updateLayout(created.getId(), updateRequest));
    }


    @Test
    void shouldThrowErrorWhenCreateOrUpdateLayoutSlotWithInvalidDataRowSpanValueZero(){
        LayoutResponseDto<LayoutSlotResponseDto> created = this.layoutServiceImpl.createLayout(
                this.buildLayoutRequestDto("Main Layout", 1,1)
        );
        LayoutRequestDto<LayoutSlotRequestUpdateDto> updateRequest = this.buildLayoutRequestDto("Updated Layout", 1,1);


        updateRequest.setSlots(
                List.of(
                        this.buildLayoutSlotRequestUpdateDto(null,null,1,1,1,0,1)
                )
        );

        assertThrows(InvalidLayoutSlotException.class, () -> layoutServiceImpl.updateLayout(created.getId(), updateRequest));
    }




    // slot row span
    @Test
    void shouldThrowErrorWhenCreateOrUpdateLayoutSlotWithInvalidDataZIndexValueNull(){
        LayoutResponseDto<LayoutSlotResponseDto> created = this.layoutServiceImpl.createLayout(
                this.buildLayoutRequestDto("Main Layout", 1,1)
        );
        LayoutRequestDto<LayoutSlotRequestUpdateDto> updateRequest = this.buildLayoutRequestDto("Updated Layout", 1,1);


        updateRequest.setSlots(
                List.of(
                        this.buildLayoutSlotRequestUpdateDto(null,null,1,1,1,1,null)
                )
        );

        assertThrows(InvalidLayoutSlotException.class, () -> layoutServiceImpl.updateLayout(created.getId(), updateRequest));
    }



    // Create layout slot on Layout creation




    // slot col pos
    @Test
    void shouldThrowErrorWhenOnCreateLayoutCreateLayoutSlotWithInvalidDataColPosValueNull(){
        LayoutRequestDto<LayoutSlotRequestDto> layout = this.buildLayoutRequestDto("Main Layout", 1,1);
        layout.setSlots(
                List.of(
                        this.buildLayoutSlotRequestDto(null,null,1,1,1,1)
                )
        );
        assertThrows(InvalidLayoutSlotException.class, () -> layoutServiceImpl.createLayout(layout));
    }


    @Test
    void shouldThrowErrorWhenOnCreateLayoutCreateLayoutSlotWithInvalidDataColPosValueNegative(){
        LayoutRequestDto<LayoutSlotRequestDto> layout = this.buildLayoutRequestDto("Main Layout", 1,1);
        layout.setSlots(
                List.of(
                        this.buildLayoutSlotRequestDto(null,-1,1,1,1,1)
                )
        );
        assertThrows(InvalidLayoutSlotException.class, () -> layoutServiceImpl.createLayout(layout));
    }


    @Test
    void shouldThrowErrorWhenOnCreateLayoutCreateLayoutSlotWithInvalidDataColPosValueZero(){
        LayoutRequestDto<LayoutSlotRequestDto> layout = this.buildLayoutRequestDto("Main Layout", 1,1);
        layout.setSlots(
                List.of(
                        this.buildLayoutSlotRequestDto(null,0,1,1,1,1)
                )
        );
        assertThrows(InvalidLayoutSlotException.class, () -> layoutServiceImpl.createLayout(layout));
    }



    // slot row pos
    @Test
    void shouldThrowErrorWhenOnCreateLayoutCreateLayoutSlotWithInvalidDataRowPosValueNull(){
        LayoutRequestDto<LayoutSlotRequestDto> layout = this.buildLayoutRequestDto("Main Layout", 1,1);
        layout.setSlots(
                List.of(
                        this.buildLayoutSlotRequestDto(null,1,null,1,1,1)
                )
        );
        assertThrows(InvalidLayoutSlotException.class, () -> layoutServiceImpl.createLayout(layout));
    }


    @Test
    void shouldThrowErrorWhenOnCreateLayoutCreateLayoutSlotWithInvalidDataRowPosValueNegative(){
        LayoutRequestDto<LayoutSlotRequestDto> layout = this.buildLayoutRequestDto("Main Layout", 1,1);
        layout.setSlots(
                List.of(
                        this.buildLayoutSlotRequestDto(null,1,-1,1,1,1)
                )
        );
        assertThrows(InvalidLayoutSlotException.class, () -> layoutServiceImpl.createLayout(layout));
    }


    @Test
    void shouldThrowErrorWhenOnCreateLayoutCreateLayoutSlotWithInvalidDataRowPosValueZero(){
        LayoutRequestDto<LayoutSlotRequestDto> layout = this.buildLayoutRequestDto("Main Layout", 1,1);
        layout.setSlots(
                List.of(
                        this.buildLayoutSlotRequestDto(null,1,0,1,1,1)
                )
        );
        assertThrows(InvalidLayoutSlotException.class, () -> layoutServiceImpl.createLayout(layout));
    }





    // slot col span
    @Test
    void shouldThrowErrorWhenOnCreateLayoutCreateLayoutSlotWithInvalidDataColSpanValueNull(){
        LayoutRequestDto<LayoutSlotRequestDto> layout = this.buildLayoutRequestDto("Main Layout", 1,1);
        layout.setSlots(
                List.of(
                        this.buildLayoutSlotRequestDto(null,1,1,null,1,1)
                )
        );
        assertThrows(InvalidLayoutSlotException.class, () -> layoutServiceImpl.createLayout(layout));
    }


    @Test
    void shouldThrowErrorWhenOnCreateLayoutCreateLayoutSlotWithInvalidDataColSpanValueNegative(){
        LayoutRequestDto<LayoutSlotRequestDto> layout = this.buildLayoutRequestDto("Main Layout", 1,1);
        layout.setSlots(
                List.of(
                        this.buildLayoutSlotRequestDto(null,1,1,-1,1,1)
                )
        );
        assertThrows(InvalidLayoutSlotException.class, () -> layoutServiceImpl.createLayout(layout));
    }


    @Test
    void shouldThrowErrorWhenOnCreateLayoutCreateLayoutSlotWithInvalidDataColSpanValueZero(){
        LayoutRequestDto<LayoutSlotRequestDto> layout = this.buildLayoutRequestDto("Main Layout", 1,1);
        layout.setSlots(
                List.of(
                        this.buildLayoutSlotRequestDto(null,1,1,0,1,1)
                )
        );
        assertThrows(InvalidLayoutSlotException.class, () -> layoutServiceImpl.createLayout(layout));
    }



    // slot row span
    @Test
    void shouldThrowErrorWhenOnCreateLayoutCreateLayoutSlotWithInvalidDataRowSpanValueNull(){
        LayoutRequestDto<LayoutSlotRequestDto> layout = this.buildLayoutRequestDto("Main Layout", 1,1);
        layout.setSlots(
                List.of(
                        this.buildLayoutSlotRequestDto(null,1,1,1,null,1)
                )
        );
        assertThrows(InvalidLayoutSlotException.class, () -> layoutServiceImpl.createLayout(layout));
    }


    @Test
    void shouldThrowErrorWhenOnCreateLayoutCreateLayoutSlotWithInvalidDataRowSpanValueNegative(){
        LayoutRequestDto<LayoutSlotRequestDto> layout = this.buildLayoutRequestDto("Main Layout", 1,1);
        layout.setSlots(
                List.of(
                        this.buildLayoutSlotRequestDto(null,1,1,1,-1,1)
                )
        );
        assertThrows(InvalidLayoutSlotException.class, () -> layoutServiceImpl.createLayout(layout));
    }


    @Test
    void shouldThrowErrorWhenOnCreateLayoutCreateLayoutSlotWithInvalidDataRowSpanValueZero(){
        LayoutRequestDto<LayoutSlotRequestDto> layout = this.buildLayoutRequestDto("Main Layout", 1,1);
        layout.setSlots(
                List.of(
                        this.buildLayoutSlotRequestDto(null,1,1,1,0,1)
                )
        );
        assertThrows(InvalidLayoutSlotException.class, () -> layoutServiceImpl.createLayout(layout));
    }




    // slot row span
    @Test
    void shouldThrowErrorWhenOnCreateLayoutCreateLayoutSlotWithInvalidDataZIndexValueNull(){
        LayoutRequestDto<LayoutSlotRequestDto> layout = this.buildLayoutRequestDto("Main Layout", 1,1);
        layout.setSlots(
                List.of(
                        this.buildLayoutSlotRequestDto(null,1,1,1,1,null)
                )
        );
        assertThrows(InvalidLayoutSlotException.class, () -> layoutServiceImpl.createLayout(layout));
    }
}
