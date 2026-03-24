package com.a6dig.digitalsignage.unit.service;

import com.a6dig.digitalsignage.dto.*;
import com.a6dig.digitalsignage.entity.Layout;
import com.a6dig.digitalsignage.entity.LayoutSlot;
import com.a6dig.digitalsignage.exception.InvalidLayoutException;
import com.a6dig.digitalsignage.exception.LayoutNotFoundException;
import com.a6dig.digitalsignage.exception.LayoutSlotNotFoundException;
import com.a6dig.digitalsignage.mapper.LayoutMapper;
import com.a6dig.digitalsignage.repository.LayoutRepository;
import com.a6dig.digitalsignage.repository.LayoutSlotRepository;
import com.a6dig.digitalsignage.service.LayoutServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LayoutServiceImplTest {

    @Mock
    private LayoutRepository layoutRepository;

    @Mock
    private LayoutSlotRepository layoutSlotRepository;

    @Mock
    private LayoutMapper layoutMapper;

    @InjectMocks
    private LayoutServiceImpl layoutServiceImpl;


    // helper methods
    private Layout buildLayout(Long id, String name, int col, int row) {
        Layout layout = new Layout();
        layout.setId(id);
        layout.setName(name);
        layout.setCols(col);
        layout.setRows(row);
        layout.setCreatedAt(LocalDateTime.now());
        layout.setUpdatedAt(LocalDateTime.now());
        layout.setSlots(new ArrayList<>());
        return layout;
    }

    private LayoutResponseDto<LayoutSlotResponseDto> buildLayoutResponseDto(Long id, String name, int col, int row, List<LayoutSlotResponseDto> slots) {
        LayoutResponseDto<LayoutSlotResponseDto> dto = new LayoutResponseDto<>();
        dto.setId(id);
        dto.setName(name);
        dto.setCols(col);
        dto.setRows(row);
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());
        dto.setSlots(slots);
        return dto;
    }



    private <T extends LayoutSlotRequestDto>LayoutRequestDto<T> buildLayoutRequestDto(String name, int col, int row) {
        LayoutRequestDto<T> dto = new LayoutRequestDto<>();
        dto.setName(name);
        dto.setCols(col);
        dto.setRows(row);
        dto.setSlots(new ArrayList<>());
        return dto;
    }


    private LayoutSlotRequestDto buildLayoutSlotRequestDto(Long moduleId, int col, int row, int colSpan, int rowSpan, int zIndex) {
        LayoutSlotRequestDto dto = new LayoutSlotRequestDto();
        dto.setModuleId(moduleId);
        dto.setColPos(col);
        dto.setRowPos(row);
        dto.setColSpan(colSpan);
        dto.setRowSpan(rowSpan);
        dto.setzIndex(zIndex);
        return dto;
    }


    private LayoutSlot buildLayoutSlot(Layout layout, Long moduleId, int colPos, int rowPos, int colSpan, int rowSpan, int zIndex) {
        LayoutSlot slot = new LayoutSlot(layout);
        slot.setModuleId(moduleId);
        slot.setColPos(colPos);
        slot.setRowPos(rowPos);
        slot.setColSpan(colSpan);
        slot.setRowSpan(rowSpan);
        slot.setzIndex(zIndex);
        return slot;
    }


    private LayoutSlotResponseDto buildLayoutSlotResponseDto(Long id, Long layoutId,
                                                             Long moduleId, int colPos, int rowPos, int colSpan, int rowSpan, int zIndex) {
        LayoutSlotResponseDto dto = new LayoutSlotResponseDto();
        dto.setId(id);
        dto.setLayoutId(layoutId);
        dto.setModuleId(moduleId);
        dto.setColPos(colPos);
        dto.setRowPos(rowPos);
        dto.setColSpan(colSpan);
        dto.setRowSpan(rowSpan);
        dto.setzIndex(zIndex);
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());

        return dto;
    }

    private void assertLayout(LayoutResponseDto<LayoutSlotResponseDto> layout, String expectedName, int expectedCols, int expectedRows) {
        assertNotNull(layout.getId());
        assertNotNull(layout.getName());
        assertEquals(expectedName, layout.getName());
        assertEquals(expectedCols, layout.getCols());
        assertEquals(expectedRows, layout.getRows());
        assertNotNull(layout.getCreatedAt());
        assertNotNull(layout.getUpdatedAt());
    }


    private void assertLayoutSlot(LayoutSlotResponseDto slot
            , Long expectedLayoutId
            , Long expectedModuleId
            , int expectedColPos
            , int expectedRowPos
            , int expectedColSpan
            , int expectedRowSpan
            , int expectedzIndex) {
        assertNotNull(slot.getId());
        assertNotNull(slot.getLayoutId());
        assertEquals(expectedLayoutId, slot.getLayoutId());
        assertEquals(expectedModuleId, slot.getModuleId());
        assertEquals(expectedColPos, slot.getColPos());
        assertEquals(expectedRowPos, slot.getRowPos());
        assertEquals(expectedColSpan, slot.getColSpan());
        assertEquals(expectedRowSpan, slot.getRowSpan());
        assertEquals(expectedzIndex, slot.getzIndex());
        assertNotNull(slot.getCreatedAt());
        assertNotNull(slot.getUpdatedAt());
    }

    // GET
    @Test
    void shouldGetLayoutById() {
        Layout layout = this.buildLayout(1L, "Main Layout", 2, 2);
        LayoutResponseDto<LayoutSlotResponseDto> layoutResponseDto = this.buildLayoutResponseDto(1L, "Main Layout", 2, 2, new ArrayList<>());

        when(layoutRepository.findById(1L)).thenReturn(Optional.of(layout));
        when(layoutMapper.toLayoutResponseDto(layout)).thenReturn(layoutResponseDto);

        LayoutResponseDto<LayoutSlotResponseDto> result = layoutServiceImpl.getLayoutById(1L);
        assertLayout(result, "Main Layout", 2, 2);
        verify(layoutRepository, times(1)).findById(1L);

    }

    @Test
    void shouldThrowWhenLayoutNotFound() {
        when(layoutRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(LayoutNotFoundException.class, () -> layoutServiceImpl.getLayoutById(1L));
        verify(layoutRepository, times(1)).findById(1L);
    }

    @Test
    void shouldGetAllLayouts(){
        List<Layout> layouts = List.of(
                this.buildLayout(1L, "Layout 1", 2, 2)
                ,this.buildLayout(2L, "Layout 2", 2, 2)
        );

        when(layoutRepository.findAll()).thenReturn(layouts);
        when(layoutMapper.toLayoutResponseDto(any(Layout.class)))
                .thenReturn(this.buildLayoutResponseDto(1L, "Layout 1", 2, 2, new ArrayList<>()))
                .thenReturn(this.buildLayoutResponseDto(2L, "Layout 2", 2,2, new ArrayList<>()));

        List<LayoutResponseDto<LayoutSlotResponseDto>> result = layoutServiceImpl.getAllLayouts();
        result.sort(Comparator.comparing(LayoutResponseDto::getName));

        assertEquals(2, result.size());
        assertLayout(result.get(0), "Layout 1", 2, 2);
        assertLayout(result.get(1), "Layout 2", 2, 2);
        verify(layoutRepository, times(1)).findAll();
    }

    @Test
    void shouldGetEmptyListWhenNotLayouts(){
        when(layoutRepository.findAll()).thenReturn(new ArrayList<>());
        List<LayoutResponseDto<LayoutSlotResponseDto>> result = layoutServiceImpl.getAllLayouts();
        assertTrue(result.isEmpty());
        verify(layoutRepository, times(1)).findAll();
    }


    // Create
    @Test
    void shouldCreateLayoutWithoutSlots(){
        LayoutRequestDto<LayoutSlotRequestDto> request = this.buildLayoutRequestDto("Main Layout", 3, 1);
        request.setSlots(null);

        Layout layout = this.buildLayout(1L, "Main Layout", 3, 1);
        LayoutResponseDto<LayoutSlotResponseDto> layoutResponse = this.buildLayoutResponseDto(1L, "Main Layout", 3, 1, new ArrayList<>());

        when(layoutRepository.save(any(Layout.class))).thenReturn(layout);
        when(layoutMapper.toLayoutResponseDto(layout)).thenReturn(layoutResponse);

        LayoutResponseDto<LayoutSlotResponseDto> result = layoutServiceImpl.createLayout(request);

        assertLayout(result, "Main Layout", 3, 1);
        assertEquals(0, result.getSlots().size());
        verify(layoutRepository, times(1)).save(any(Layout.class));
        verify(layoutMapper, times(1)).toLayoutResponseDto(any(Layout.class));

    }

    @Test
    void shouldThrowErrorWhenCreateLayoutWithInvalidData(){
        LayoutRequestDto<LayoutSlotRequestDto> request = this.buildLayoutRequestDto("Main Layout", 0, 1);
        request.setSlots(null);
        assertThrows(InvalidLayoutException.class, () -> layoutServiceImpl.createLayout(request));

        verify(layoutRepository, never()).save(any(Layout.class));
        verify(layoutMapper, never()).toLayoutResponseDto(any(Layout.class));
    }
    // Update

    @Test
    void shouldUpdateLayout() {
        Layout existing = this.buildLayout(1L, "Main Layout", 3, 3);
        Layout updated = this.buildLayout(1L, "Updated Main Layout", 4, 2);
        LayoutResponseDto<LayoutSlotResponseDto> responseDto = this.buildLayoutResponseDto(1L, "Updated Main Layout", 4, 2, new ArrayList<>());

        LayoutRequestDto<LayoutSlotRequestUpdateDto> request = this.buildLayoutRequestDto("Updated Main Layout", 4, 2);

        when(layoutRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(layoutRepository.saveAndFlush(any(Layout.class))).thenReturn(updated);
        when(layoutMapper.toLayoutResponseDto(updated)).thenReturn(responseDto);

        LayoutResponseDto<LayoutSlotResponseDto> result = layoutServiceImpl.updateLayout(1L, request);

        assertLayout(result, "Updated Main Layout", 4, 2);
        verify(layoutRepository, times(1)).findById(1L);
        verify(layoutRepository, times(1)).saveAndFlush(any(Layout.class));

    }


    @Test
    void shouldThrowErrorWhenUpdateLayoutWithInvalidData(){
        Layout existing = this.buildLayout(1L, "Main Layout", 3, 3);
        LayoutRequestDto<LayoutSlotRequestUpdateDto> updated = this.buildLayoutRequestDto( "Updated Main Layout", 0, 2);
        assertThrows(InvalidLayoutException.class, () -> layoutServiceImpl.updateLayout(1L, updated));

        verify(layoutRepository, never()).save(any(Layout.class));
        verify(layoutMapper, never()).toLayoutResponseDto(any(Layout.class));
    }



    @Test
    void shouldThrowErrorWhenUpdatingNonExistingLayout() {

        LayoutRequestDto<LayoutSlotRequestUpdateDto> request = new LayoutRequestDto<>();
        request.setName("Updated Main Layout");
        request.setCols(4);
        request.setRows(2);

        when(layoutRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(LayoutNotFoundException.class, () -> layoutServiceImpl.updateLayout(1L, request));

        verify(layoutRepository, times(1)).findById(1L);
        verify(layoutRepository, never()).save(any(Layout.class));
    }

    // Delete
    @Test
    void shouldDeleteLayout(){
        Layout layout = this.buildLayout(1L, "Main Layout", 3, 1);
        when(layoutRepository.findById(1L)).thenReturn(Optional.of(layout));
        layoutServiceImpl.deleteLayout(1L);

        verify(layoutRepository, times(1)).findById(any(Long.class));
        verify(layoutRepository, times(1)).delete(any(Layout.class));
    }

    @Test
    void shouldThrowErrorWhenDeletingNonExistentLayout(){
        when(layoutRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(LayoutNotFoundException.class, () -> layoutServiceImpl.deleteLayout(1L));

        verify(layoutRepository, times(1)).findById(any(Long.class));
        verify(layoutRepository, never()).delete(any(Layout.class));
    }

    @Test
    void shouldDeleteAllLayouts() {
        layoutServiceImpl.deleteAllLayouts();
        verify(layoutRepository, times(1)).deleteAll();
    }

    // Relation
    @Test
    void shouldAddSlotToLayout() {

        LayoutSlotRequestDto request = this.buildLayoutSlotRequestDto(1L, 2,2,1,1,0);
        Layout layout = this.buildLayout(1L, "Main Layout", 2, 2);
        LayoutSlotResponseDto slot = this.buildLayoutSlotResponseDto(1L,1L,1L, 2,2,1,1,0);
        LayoutResponseDto<LayoutSlotResponseDto> responseDto = this.buildLayoutResponseDto(1L, "Main Layout", 2, 2, List.of(slot));

        when(layoutRepository.findById(1L)).thenReturn(Optional.of(layout));
        when(layoutRepository.save(any(Layout.class))).thenReturn(layout);
        when(layoutMapper.toLayoutResponseDto(layout)).thenReturn(responseDto);

        LayoutResponseDto<LayoutSlotResponseDto> result = layoutServiceImpl.addLayoutSlotToLayout(1L, request);

        assertLayoutSlot(result.getSlots().get(0), result.getId(), 1L, 2,2,1,1,0);
        verify(layoutRepository, times(1)).findById(1L);
        verify(layoutRepository, times(1)).save(any(Layout.class));

    }
    @Test
    void shouldThrowErrorWhenAddingSlotToNonExistentLayout() {
        LayoutSlotRequestDto request = this.buildLayoutSlotRequestDto(1L, 2,2,1,1,0);

        when(layoutRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(LayoutNotFoundException.class, () -> layoutServiceImpl.addLayoutSlotToLayout(1L, request));

        verify(layoutRepository, times(1)).findById(1L);
        verify(layoutRepository, never()).save(any(Layout.class));

    }

    @Test
    void shouldRemoveSlotFromLayout() {
        Layout layout = this.buildLayout(1L, "Main Layout", 2, 2);
        LayoutSlot layoutSlot = this.buildLayoutSlot(layout,1L, 2,2,1,1,0);
        layoutSlot.setId(1L);

        layout.addLayoutSlot(layoutSlot);

        LayoutResponseDto<LayoutSlotResponseDto> responseDto = this.buildLayoutResponseDto(1L, "Main Layout", 2, 2, new ArrayList<>());

        when(layoutRepository.findById(1L)).thenReturn(Optional.of(layout));
        when(layoutSlotRepository.findById(1L)).thenReturn(Optional.of(layoutSlot));
        when(layoutRepository.save(any(Layout.class))).thenReturn(layout);
        when(layoutMapper.toLayoutResponseDto(layout)).thenReturn(responseDto);

        LayoutResponseDto<LayoutSlotResponseDto> result = layoutServiceImpl.removeLayoutSlotFromLayout(1L, 1L);

        verify(layoutRepository, times(1)).findById(1L);
        verify(layoutSlotRepository, times(1)).findById(1L);
        verify(layoutRepository, times(1)).save(any(Layout.class));

        assertEquals(0, result.getSlots().size());
    }

    @Test
    void shouldThrowErrorWhenRemovingSlotFromNonExistentLayout() {

        when(layoutRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(LayoutNotFoundException.class, () -> layoutServiceImpl.removeLayoutSlotFromLayout(1L, 1L));

        verify(layoutRepository, times(1)).findById(1L);
        verify(layoutSlotRepository, never()).findById(1L);
        verify(layoutRepository, never()).save(any(Layout.class));
    }

    @Test
    void shouldThrowErrorWhenRemovingNonExistentSlotFromLayout() {
        Layout layout = this.buildLayout(1L, "Main Layout", 2, 2);
        when(layoutRepository.findById(1L)).thenReturn(Optional.of(layout));

        assertThrows(LayoutSlotNotFoundException.class, () -> layoutServiceImpl.removeLayoutSlotFromLayout(1L, 1L));

        verify(layoutRepository, times(1)).findById(1L);
        verify(layoutSlotRepository, times(1)).findById(1L);
        verify(layoutRepository, never()).save(any(Layout.class));
    }

    @Test
    void shouldThrowErrorWhenRemovingFromLayoutWithInvalidLayoutId() {
        Layout layout = this.buildLayout(1L, "Main Layout", 2, 2);
        LayoutSlot layoutSlot = this.buildLayoutSlot(layout,1L, 2,2,1,1,0);
        layoutSlot.setId(1L);
        layout.setId(2L);

        layout.addLayoutSlot(layoutSlot);

        LayoutResponseDto responseDto = this.buildLayoutResponseDto(1L, "Main Layout", 2, 2, new ArrayList<>());

        when(layoutRepository.findById(1L)).thenReturn(Optional.of(layout));
        when(layoutSlotRepository.findById(1L)).thenReturn(Optional.of(layoutSlot));


        assertThrows(InvalidLayoutException.class, () -> layoutServiceImpl.removeLayoutSlotFromLayout(1L, 1L));


        verify(layoutRepository, times(1)).findById(1L);
        verify(layoutSlotRepository, times(1)).findById(1L);
        verify(layoutRepository, never()).save(any(Layout.class));
    }


    // Layout slots
    @Test
    void shouldGetAllLayoutSlotByLayoutId() {
        Layout layout = this.buildLayout(1L, "Main Layout", 2, 2);
        LayoutSlot ls1 = this.buildLayoutSlot(layout, 1L, 2, 2, 1, 1, 0);
        LayoutSlot ls2 = this.buildLayoutSlot(layout, 2L, 1, 1, 1, 1, 0);
        List<LayoutSlot> layoutSlots = List.of(ls1,ls2);

        layout.addLayoutSlots(layoutSlots);

        LayoutSlotResponseDto ls1response = this.buildLayoutSlotResponseDto(1L, 1L, 1L, 2, 2, 1, 1, 0);
        LayoutSlotResponseDto ls2response = this.buildLayoutSlotResponseDto(2L, 1L, 2L, 1, 1, 1, 1, 0);


        when(layoutSlotRepository.getAllLayoutSlotsByLayoutId(1L)).thenReturn(layoutSlots);
        when(layoutMapper.toLayoutSlotResponseDto(any(LayoutSlot.class)))
                .thenReturn(ls1response)
                .thenReturn(ls2response);

        List<LayoutSlotResponseDto> result = layoutServiceImpl.getAllLayoutSlotsByLayoutId(1L);
        result.sort(Comparator.comparing(LayoutSlotResponseDto::getId));

        verify(layoutSlotRepository, times(1)).getAllLayoutSlotsByLayoutId(anyLong());

        assertEquals(2, result.size());
        assertLayoutSlot(result.get(0), layout.getId(), 1L, 2, 2, 1, 1, 0);
        assertLayoutSlot(result.get(1), layout.getId(), 2L, 1, 1, 1, 1, 0);



    }
}
