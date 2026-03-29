package com.a6dig.digitalsignage.integration.repository;

import com.a6dig.digitalsignage.entity.Layout;
import com.a6dig.digitalsignage.entity.LayoutSlot;
import com.a6dig.digitalsignage.repository.LayoutRepository;
import com.a6dig.digitalsignage.repository.LayoutSlotRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
class LayoutRepositoryTest {
    @Autowired
    private LayoutRepository layoutRepository;

    @Autowired
    private LayoutSlotRepository layoutSlotRepository;

    private Layout build(String name, int col, int row){
        Layout layout = new Layout();
        layout.setName(name);
        layout.setCols(col);
        layout.setRows(row);
        return layout;
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

    private void assertLayout(Layout layout, String expectedName, int expectedCols, int expectedRows) {
        assertNotNull(layout.getId());
        assertNotNull(layout.getName());
        assertEquals(expectedName, layout.getName());
        assertEquals(expectedCols, layout.getCols());
        assertEquals(expectedRows, layout.getRows());
        assertNotNull(layout.getCreatedAt());
        assertNotNull(layout.getUpdatedAt());
        assertTrue(layoutRepository.existsById(layout.getId()));
    }


    private void assertLayoutSlot(LayoutSlot slot
            , Long expectedLayoutId
            , Long expectedModuleId
            , int expectedColPos
            , int expectedRowPos
            , int expectedColSpan
            , int expectedRowSpan
            , int expectedzIndex) {
        assertNotNull(slot.getId());
        assertNotNull(slot.getLayout());
        assertEquals(expectedLayoutId, slot.getLayout().getId());
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
    // GET
    @Test
    void shouldFindById(){
        Layout layout = this.build("Default Layout", 2, 2);
        Layout saved = layoutRepository.save(layout);
        Optional<Layout> result = layoutRepository.findById(saved.getId());
        assertTrue(result.isPresent());
        assertLayout(result.get(), "Default Layout", 2, 2);
    }

    @Test
    @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldFindAllLayouts(){
        List<Layout> layouts = new ArrayList<>();
        layouts.add(this.build("Default Layout 1", 1, 1));
        layouts.add(this.build("Default Layout 2", 2, 2));

        List<Layout> saved = layoutRepository.saveAll(layouts);

        saved.sort(Comparator.comparing(Layout::getName));

        assertEquals(2, saved.size());
        assertLayout(saved.get(0), "Default Layout 1", 1, 1);
        assertLayout(saved.get(1), "Default Layout 2", 2, 2);

    }

    @Test
    @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldReturnEmptyWhenNotFound(){
        Optional<Layout> layout = layoutRepository.findById(1L);
        assertFalse(layout.isPresent());
    }


    @Test
    @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldReturnEmptyListWhenNotFound(){
        List<Layout> layout = layoutRepository.findAll();
        assertEquals(0, layout.size());
    }




    // POST

    @Test
    void shouldSave(){
        Layout layout = this.build("Default Layout", 2, 2);
        Layout saved = layoutRepository.save(layout);
        assertLayout(saved, "Default Layout", 2, 2);
    }

    // PUT
    @Test
    void shouldUpdateName(){
        Layout layout = this.build("Default Name", 2, 2);
        Layout saved = layoutRepository.save(layout);

        saved.setName("Updated Name");
        Layout updated = layoutRepository.save(saved);
        assertLayout(updated, "Updated Name", 2,2);

        Optional<Layout> result = layoutRepository.findById(updated.getId());
        assertTrue(result.isPresent());
        assertLayout(result.get(), "Updated Name", 2,2);
    }

    @Test
    void shouldUpdateColRow(){
        Layout layout = this.build("Default Name", 2, 2);
        Layout saved = layoutRepository.save(layout);

        saved.setCols(3);
        saved.setRows(3);

        Layout updated = layoutRepository.save(saved);
        assertLayout(updated, "Default Name", 3,3);

        Optional<Layout> result = layoutRepository.findById(updated.getId());
        assertTrue(result.isPresent());
        assertLayout(result.get(), "Default Name", 3,3);
    }


    // DELETE
    @Test
    void shouldDeleteById() {
        Layout saved = layoutRepository.save(build("Default Layout", 3, 3));

        layoutRepository.deleteById(saved.getId());
        assertFalse(layoutRepository.findById(saved.getId()).isPresent());
    }
    @Test
    void shouldDeleteAll() {
        layoutRepository.save(build("Default Layout", 3, 3));
        layoutRepository.save(build("Default Layout 2", 4, 4));
        layoutRepository.deleteAll();
        assertEquals(0, layoutRepository.count());
    }

    @Test
    @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldNotThrowExceptionWhenDeletingNonExistingId(){
        assertDoesNotThrow(() -> layoutRepository.deleteById(1L));
    }

    // EXISTENCE
    @Test
    void shouldReturnTrueWhenExist() {
        Layout saved = layoutRepository.save(build("Default Layout", 3, 3));
        assertTrue(layoutRepository.findById(saved.getId()).isPresent());
    }

    @Test
    @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldReturnFalseWhenNotExist() {
        assertFalse(layoutRepository.findById(1L).isPresent());
    }




    // ENTITY RELATIONSHIP
    @Test
    @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldSaveLayoutWithLayoutSlots(){
        Layout layout = this.build("Default Layout", 2, 2);
        LayoutSlot layoutSlot1 = this.buildLayoutSlot(layout,1L, 1,1,2,1,0);
        LayoutSlot layoutSlot2 = this.buildLayoutSlot(layout,2L, 1,2,2,1,0);
        layout.setSlots(List.of(layoutSlot1, layoutSlot2));

        Layout saved = layoutRepository.save(layout);
        assertLayout(saved, "Default Layout", 2, 2);
        assertEquals(2, saved.getSlots().size());

        List<LayoutSlot> savedSlots = new ArrayList<>(saved.getSlots());
        savedSlots.sort(Comparator.comparing(LayoutSlot::getModuleId));
        LayoutSlot savedLayoutSlot1 = savedSlots.get(0);
        LayoutSlot savedLayoutSlot2 = savedSlots.get(1);

        assertLayoutSlot(savedLayoutSlot1,saved.getId(),1L, 1,1,2,1,0);
        assertLayoutSlot(savedLayoutSlot2,saved.getId(),2L, 1,2,2,1,0);


    }
    @Test
    @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldCascadeDeleteLayoutSlotsWithLayout(){
        Layout layout = this.build("Default Layout", 2, 2);
        LayoutSlot layoutSlot1 = this.buildLayoutSlot(layout,1L, 1,1,2,1,0);
        LayoutSlot layoutSlot2 = this.buildLayoutSlot(layout,2L, 1,2,2,1,0);
        layout.setSlots(List.of(layoutSlot1, layoutSlot2));

        Layout saved = layoutRepository.save(layout);

        LayoutSlot savedLayoutSlot1 = saved.getSlots().get(0);
        LayoutSlot savedLayoutSlot2 = saved.getSlots().get(1);

        layoutRepository.deleteAll();

        assertEquals(0, layoutSlotRepository.count());
        assertFalse(layoutSlotRepository.findById(savedLayoutSlot1.getId()).isPresent());
        assertFalse(layoutSlotRepository.findById(savedLayoutSlot2.getId()).isPresent());

    }


    @Test
    @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldCascadeMergeLayoutSlotsWithLayout(){
        Layout layout = this.build("Default Layout", 2, 2);
        Layout saved = layoutRepository.save(layout);

        LayoutSlot layoutSlot1 = this.buildLayoutSlot(layout,1L, 1,1,2,1,0);
        LayoutSlot layoutSlot2 = this.buildLayoutSlot(layout,2L, 1,2,2,1,0);

        List<LayoutSlot> savedLayoutSlots = layoutSlotRepository.saveAll(List.of(layoutSlot1, layoutSlot2));


        savedLayoutSlots.sort(Comparator.comparing(LayoutSlot::getModuleId));

        LayoutSlot savedLayoutSlot1 = savedLayoutSlots.get(0);
        LayoutSlot savedLayoutSlot2 = savedLayoutSlots.get(1);

        assertLayoutSlot(savedLayoutSlot1, saved.getId(),1L, 1,1,2,1,0);
        assertLayoutSlot(savedLayoutSlot2, saved.getId(),2L, 1,2,2,1,0);


    }


    @Test
    @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldGetAllLayoutSlotsByLayoutId(){
        Layout layout = this.build("Default Layout", 2, 2);
        LayoutSlot layoutSlot1 = this.buildLayoutSlot(layout,1L, 1,1,2,1,0);
        LayoutSlot layoutSlot2 = this.buildLayoutSlot(layout,2L, 1,2,2,1,0);
        layout.setSlots(List.of(layoutSlot1, layoutSlot2));
        Layout saved = layoutRepository.save(layout);


        List<LayoutSlot> result = this.layoutSlotRepository.getAllLayoutSlotsByLayoutId(saved.getId());
        result.sort(Comparator.comparing(LayoutSlot::getModuleId));

        LayoutSlot savedSlot1 = result.get(0);
        LayoutSlot savedSlot2 = result.get(1);
        assertEquals(2, result.size());
        assertLayoutSlot(savedSlot1, saved.getId(), 1L, 1,1,2,1,0);
        assertLayoutSlot(savedSlot2, saved.getId(), 2L, 1,2,2,1,0);

    }

}