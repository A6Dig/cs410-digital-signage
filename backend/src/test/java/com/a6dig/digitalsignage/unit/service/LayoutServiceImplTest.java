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

    private LayoutSlotRequestDto buildLayoutSlotRequestDto(Long moduleId, int col, int row, int colSpan, int rowSpan, int zIndex) {
        LayoutSlotRequestDto dto = new LayoutSlotRequestDto();
        dto.setModuleId(moduleId);
        dto.setGridCol(col);
        dto.setGridRow(row);
        dto.setColSpan(colSpan);
        dto.setRowSpan(rowSpan);
        dto.setzIndex(zIndex);
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


    // GET
    @Test
    void shouldGetLayoutById() {
        Layout layout = this.buildLayout(1L, "Main Layout", 2, 2);
        LayoutResponseDto layoutResponseDto = this.buildLayoutResponseDto(1L, "Main Layout", 2, 2);

        when(layoutRepository.findById(1L)).thenReturn(Optional.of(layout));
        when(layoutMapper.toLayoutResponseDto(layout)).thenReturn(layoutResponseDto);

        LayoutResponseDto result = layoutServiceImpl.getLayoutById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
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
                this.buildLayout(1L, "Main Layout", 2, 2)
                ,this.buildLayout(2L, "Secondary Layout", 2, 2)
        );

        when(layoutRepository.findAll()).thenReturn(layouts);
        when(layoutMapper.toLayoutResponseDto(any(Layout.class)))
                .thenReturn(this.buildLayoutResponseDto(1L, "Main Layout", 2, 2))
                .thenReturn(this.buildLayoutResponseDto(2L, "Secondary Layout", 2,2));

        List<LayoutResponseDto> result = layoutServiceImpl.getAllLayouts();

        assertEquals(2, result.size());
        verify(layoutRepository, times(1)).findAll();
    }

    @Test
    void shouldGetEmptyListWhenNotLayouts(){
        when(layoutRepository.findAll()).thenReturn(new ArrayList<>());
        List<LayoutResponseDto> result = layoutServiceImpl.getAllLayouts();
        assertTrue(result.isEmpty());
        verify(layoutRepository, times(1)).findAll();
    }


    // Create
    @Test
    void shouldCreateLayoutWithoutSlots(){
        LayoutRequestDto request = this.buildLayoutRequestDto("Main Layout", 3, 1);
        request.setLayoutSlotRequestDtoList(null);

        Layout layout = this.buildLayout(1L, "Main Layout", 3, 1);
        LayoutResponseDto layoutResponse = this.buildLayoutResponseDto(1L, "Main Layout", 3, 1);

        when(layoutRepository.save(any(Layout.class))).thenReturn(layout);
        when(layoutMapper.toLayoutResponseDto(layout)).thenReturn(layoutResponse);

        LayoutResponseDto result = layoutServiceImpl.createLayout(request);

        assertNotNull(result);
        assertEquals(0, result.getLayoutSlotList().size());
        assertEquals("Main Layout", result.getName());
        assertEquals(1L, result.getId());
        verify(layoutRepository, times(1)).save(any(Layout.class));
        verify(layoutMapper, times(1)).toLayoutResponseDto(any(Layout.class));

    }

    @Test
    void shouldThrowErrorWhenCreateLayoutWithInvalidData(){
        LayoutRequestDto request = this.buildLayoutRequestDto("Main Layout", 0, 1);
        request.setLayoutSlotRequestDtoList(null);
        assertThrows(InvalidLayoutException.class, () -> layoutServiceImpl.createLayout(request));

        verify(layoutRepository, never()).save(any(Layout.class));
        verify(layoutMapper, never()).toLayoutResponseDto(any(Layout.class));
    }
    // Update

    @Test
    void shouldUpdateLayout() {
        Layout existing = this.buildLayout(1L, "Main Layout", 3, 3);
        Layout updated = this.buildLayout(1L, "Updated Main Layout", 4, 2);
        LayoutResponseDto responseDto = this.buildLayoutResponseDto(1L, "Updated Main Layout", 4, 2);

        LayoutRequestUpdateDto request = this.buildLayoutRequestUpdateDto(1L, "Updated Main Layout", 4, 2);

        when(layoutRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(layoutRepository.saveAndFlush(any(Layout.class))).thenReturn(updated);
        when(layoutMapper.toLayoutResponseDto(updated)).thenReturn(responseDto);

        LayoutResponseDto result = layoutServiceImpl.updateLayout(1L, request);

        assertEquals("Updated Main Layout", result.getName());
        assertEquals(2, result.getLayoutRow());
        assertEquals(4, result.getLayoutCol());
        verify(layoutRepository, times(1)).findById(1L);
        verify(layoutRepository, times(1)).saveAndFlush(any(Layout.class));

    }


    @Test
    void shouldThrowErrorWhenUpdateLayoutWithInvalidData(){
        Layout existing = this.buildLayout(1L, "Main Layout", 3, 3);
        LayoutRequestUpdateDto updated = this.buildLayoutRequestUpdateDto(1L, "Updated Main Layout", 0, 2);
        assertThrows(InvalidLayoutException.class, () -> layoutServiceImpl.updateLayout(1L, updated));

        verify(layoutRepository, never()).save(any(Layout.class));
        verify(layoutMapper, never()).toLayoutResponseDto(any(Layout.class));
    }



    @Test
    void shouldThrowErrorWhenUpdatingNonExistingLayout() {

        LayoutRequestUpdateDto request = new LayoutRequestUpdateDto();
        request.setId(1L);
        request.setName("Updated Main Layout");
        request.setLayoutCol(4);
        request.setLayoutRow(2);

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
        Layout layout = this.buildLayout(1L, "Main Layout", 2, 2);
        LayoutResponseDto responseDto = this.buildLayoutResponseDto(1L, "Main Layout", 2, 2);

        LayoutSlotRequestDto request = this.buildLayoutSlotRequestDto(1L, 2,2,1,1,0);
        when(layoutRepository.findById(1L)).thenReturn(Optional.of(layout));
        when(layoutRepository.save(any(Layout.class))).thenReturn(layout);
        when(layoutMapper.toLayoutResponseDto(layout)).thenReturn(responseDto);

        LayoutResponseDto result = layoutServiceImpl.addLayoutSlotToLayout(1L, request);

        assertNotNull(result);
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

        LayoutResponseDto responseDto = this.buildLayoutResponseDto(1L, "Main Layout", 2, 2);

        when(layoutRepository.findById(1L)).thenReturn(Optional.of(layout));
        when(layoutSlotRepository.findById(1L)).thenReturn(Optional.of(layoutSlot));
        when(layoutRepository.save(any(Layout.class))).thenReturn(layout);
        when(layoutMapper.toLayoutResponseDto(layout)).thenReturn(responseDto);

        LayoutResponseDto result = layoutServiceImpl.removeLayoutSlotFromLayout(1L, 1L);

        verify(layoutRepository, times(1)).findById(1L);
        verify(layoutSlotRepository, times(1)).findById(1L);
        verify(layoutRepository, times(1)).save(any(Layout.class));

        assertEquals(0, result.getLayoutSlotList().size());
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

        LayoutResponseDto responseDto = this.buildLayoutResponseDto(1L, "Main Layout", 2, 2);

        when(layoutRepository.findById(1L)).thenReturn(Optional.of(layout));
        when(layoutSlotRepository.findById(1L)).thenReturn(Optional.of(layoutSlot));


        assertThrows(InvalidLayoutException.class, () -> layoutServiceImpl.removeLayoutSlotFromLayout(1L, 1L));


        verify(layoutRepository, times(1)).findById(1L);
        verify(layoutSlotRepository, times(1)).findById(1L);
        verify(layoutRepository, never()).save(any(Layout.class));
    }
}
