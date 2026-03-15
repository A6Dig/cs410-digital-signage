package com.a6dig.digitalsignage.integration.repository;

import com.a6dig.digitalsignage.entity.LayoutSlot;
import com.a6dig.digitalsignage.repository.LayoutRespository;
import com.a6dig.digitalsignage.repository.LayoutSlotRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
class LayoutRespositoryTest {
    @Autowired
    private LayoutSlotRepository layoutSlotRepository;

    private LayoutSlot build(Long moduleId, int col, int row, int colSpan, int rowSpan){
        LayoutSlot layoutSlot = new LayoutSlot();
        layoutSlot.setModuleId(moduleId);
        layoutSlot.setGridCol(col);
        layoutSlot.setGridRow(row);
        layoutSlot.setColSpan(colSpan);
        layoutSlot.setRowSpan(rowSpan);

        return layoutSlot;
    }

    // GET
    @Test
    void shouldFindById(){
        LayoutSlot layoutSlot = this.build(1L, 2, 2, 2,1);

        LayoutSlot saved = layoutSlotRepository.save(layoutSlot);

        Optional<LayoutSlot> result = layoutSlotRepository.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getModuleId());
        assertEquals(2, result.get().getGridCol());
        assertEquals(2, result.get().getGridRow());
        assertEquals(2, result.get().getColSpan());
        assertEquals(1, result.get().getRowSpan());
    }

    @Test
    @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldFindAllLayouts(){
        List<LayoutSlot> layoutSlots = new ArrayList<>();
        layoutSlots.add(this.build(1L, 2, 2, 2,1));
        layoutSlots.add(this.build(1L, 2, 2, 2,1));

        List<LayoutSlot> saved = layoutSlotRepository.saveAll(layoutSlots);

        assertEquals(2, saved.size());

    }

    @Test
    @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldReturnEmptyWhenNotFound(){
        Optional<LayoutSlot> layoutSlot = layoutSlotRepository.findById(1L);
        assertFalse(layoutSlot.isPresent());
    }


    @Test
    @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldReturnEmptyListWhenNotFound(){
        List<LayoutSlot> layoutSlot = layoutSlotRepository.findAll();
        assertEquals(0, layoutSlot.size());
    }




    // POST

    @Test
    void shouldSave(){
        LayoutSlot layoutSlot = this.build(1L, 2, 2, 2,1);

        LayoutSlot saved = layoutSlotRepository.save(layoutSlot);

        assertNotNull(saved.getId());
        assertEquals(1L, saved.getModuleId());
        assertEquals(2, saved.getGridCol());
        assertEquals(2, saved.getGridRow());
        assertEquals(2, saved.getColSpan());
        assertEquals(1, saved.getRowSpan());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());
    }



    // PUT
    @Test
    void shouldUpdateModule(){
        LayoutSlot layoutSlot = this.build(1L, 2, 2, 2,1);

        LayoutSlot saved = layoutSlotRepository.save(layoutSlot);

        saved.setModuleId(2L);

        LayoutSlot updated = layoutSlotRepository.save(saved);

        assertNotNull(updated.getId());
        assertEquals(2L, updated.getModuleId());
        assertEquals(2, saved.getGridCol());
        assertEquals(2, saved.getGridRow());
        assertEquals(2, saved.getColSpan());
        assertEquals(1, saved.getRowSpan());
        assertEquals(saved.getCreatedAt(), updated.getCreatedAt());
        assertNotEquals(updated.getCreatedAt(), updated.getUpdatedAt());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());
    }

    @Test
    void shouldUpdateColRow(){
        LayoutSlot layoutSlot = this.build(1L, 2, 2, 2,1);

        LayoutSlot saved = layoutSlotRepository.save(layoutSlot);

        saved.setGridCol(2);
        saved.setGridRow(3);

        LayoutSlot updated = layoutSlotRepository.save(saved);


        assertNotNull(updated.getId());
        assertEquals(1L, updated.getModuleId());
        assertEquals(2, saved.getGridCol());
        assertEquals(3, saved.getGridRow());
        assertEquals(2, saved.getColSpan());
        assertEquals(1, saved.getRowSpan());
        assertEquals(saved.getCreatedAt(), updated.getCreatedAt());
        assertNotEquals(updated.getCreatedAt(), updated.getUpdatedAt());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());
    }

    @Test
    void shouldUpdateColSpanRowSpan(){
        LayoutSlot layoutSlot = this.build(1L, 2, 2, 2,1);

        LayoutSlot saved = layoutSlotRepository.save(layoutSlot);

        saved.setColSpan(4);
        saved.setRowSpan(4);

        LayoutSlot updated = layoutSlotRepository.save(saved);


        assertNotNull(updated.getId());
        assertEquals(1L, updated.getModuleId());
        assertEquals(2, saved.getGridCol());
        assertEquals(2, saved.getGridRow());
        assertEquals(4, saved.getColSpan());
        assertEquals(4, saved.getRowSpan());
        assertEquals(saved.getCreatedAt(), updated.getCreatedAt());
        assertNotEquals(updated.getCreatedAt(), updated.getUpdatedAt());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());
    }


    // DELETE
    @Test
    void shouldDeleteById() {
        LayoutSlot saved = layoutSlotRepository.save(this.build(1L, 2, 2, 2,1));
        layoutSlotRepository.deleteById(saved.getId());
        assertFalse(layoutSlotRepository.findById(saved.getId()).isPresent());
    }
    @Test
    void shouldDeleteAll() {
        layoutSlotRepository.save(this.build(1L, 2, 2, 2,1));
        layoutSlotRepository.save(this.build(1L, 22, 22, 2,1));

        layoutSlotRepository.deleteAll();
        assertEquals(0, layoutSlotRepository.count());
    }

    @Test
    @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldNotThrowExceptionWhenDeletingNonExistingId(){
        assertDoesNotThrow(() -> layoutSlotRepository.deleteById(1L));
    }

    // EXISTENCE
    @Test
    void shouldReturnTrueWhenExist() {
        LayoutSlot saved = layoutSlotRepository.save(this.build(1L, 2, 2, 2,1));
        assertTrue(layoutSlotRepository.findById(saved.getId()).isPresent());
    }

    @Test
    @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldReturnFalseWhenNotExist() {
        assertFalse(layoutSlotRepository.findById(1L).isPresent());
    }

}