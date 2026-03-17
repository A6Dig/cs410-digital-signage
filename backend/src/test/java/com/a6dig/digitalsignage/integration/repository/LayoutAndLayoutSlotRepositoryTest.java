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


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
class LayoutAndLayoutSlotRepositoryTest {
    @Autowired
    private LayoutRepository layoutRepository;

    @Autowired
    private LayoutSlotRepository layoutSlotRepository;

    private Layout build(String name, int col, int row){
        Layout layout = new Layout();
        layout.setName(name);
        layout.setLayoutCol(col);
        layout.setLayoutRow(row);
        return layout;
    }

    // ENTITY RELATIONSHIP
    @Test
    @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldSaveLayoutWithLayoutSlots(){
        Layout layout = new Layout();
        layout.setName("Default Layout");
        layout.setLayoutCol(2);
        layout.setLayoutRow(2);

        LayoutSlot layoutSlot1 = new LayoutSlot(layout);
        layoutSlot1.setModuleId(1L);
        layoutSlot1.setGridCol(1);
        layoutSlot1.setGridRow(1);
        layoutSlot1.setColSpan(2);
        layoutSlot1.setRowSpan(1);

        LayoutSlot layoutSlot2 = new LayoutSlot(layout);
        layoutSlot2.setModuleId(1L);
        layoutSlot2.setGridCol(1);
        layoutSlot2.setGridRow(2);
        layoutSlot2.setColSpan(2);
        layoutSlot2.setRowSpan(1);

        layout.setLayoutSlotList(List.of(layoutSlot1, layoutSlot2));

        Layout saved = layoutRepository.save(layout);

        assertNotNull(saved.getId());
        assertEquals(2, saved.getLayoutSlotList().size());
        assertTrue(layoutSlotRepository.findById(saved.getLayoutSlotList().get(0).getId()).isPresent());
        assertTrue(layoutSlotRepository.findById(saved.getLayoutSlotList().get(1).getId()).isPresent());

    }
    @Test
    @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldCascadeDeleteLayoutSlotsWithLayout(){
        Layout layout = new Layout();
        layout.setName("Default Layout");
        layout.setLayoutCol(2);
        layout.setLayoutRow(2);

        LayoutSlot layoutSlot1 = new LayoutSlot(layout);
        layoutSlot1.setModuleId(1L);
        layoutSlot1.setGridCol(1);
        layoutSlot1.setGridRow(1);
        layoutSlot1.setColSpan(2);
        layoutSlot1.setRowSpan(1);

        LayoutSlot layoutSlot2 = new LayoutSlot(layout);
        layoutSlot2.setModuleId(1L);
        layoutSlot2.setGridCol(1);
        layoutSlot2.setGridRow(2);
        layoutSlot2.setColSpan(2);
        layoutSlot2.setRowSpan(1);

        layout.setLayoutSlotList(List.of(layoutSlot1, layoutSlot2));

        Layout saved = layoutRepository.save(layout);

        assertNotNull(saved.getId());
        assertEquals(2, saved.getLayoutSlotList().size());
        assertTrue(layoutSlotRepository.findById(saved.getLayoutSlotList().get(0).getId()).isPresent());
        assertTrue(layoutSlotRepository.findById(saved.getLayoutSlotList().get(1).getId()).isPresent());


        layoutRepository.deleteAll();

        assertEquals(0, layoutSlotRepository.count());
        assertFalse(layoutSlotRepository.findById(saved.getLayoutSlotList().get(0).getId()).isPresent());
        assertFalse(layoutSlotRepository.findById(saved.getLayoutSlotList().get(1).getId()).isPresent());

    }


    @Test
    @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldCascadeMergeLayoutSlotsWithLayout(){
        Layout layout = new Layout();
        layout.setName("Default Layout");
        layout.setLayoutCol(2);
        layout.setLayoutRow(2);

        Layout savedLayout = layoutRepository.save(layout);

        LayoutSlot layoutSlot1 = new LayoutSlot(savedLayout);
        layoutSlot1.setModuleId(1L);
        layoutSlot1.setGridCol(1);
        layoutSlot1.setGridRow(1);
        layoutSlot1.setColSpan(2);
        layoutSlot1.setRowSpan(1);

        LayoutSlot layoutSlot2 = new LayoutSlot(savedLayout);
        layoutSlot2.setModuleId(1L);
        layoutSlot2.setGridCol(1);
        layoutSlot2.setGridRow(2);
        layoutSlot2.setColSpan(2);
        layoutSlot2.setRowSpan(1);

        List<LayoutSlot> savedLayoutSlots = layoutSlotRepository.saveAll(List.of(layoutSlot1, layoutSlot2));

        Long id = savedLayoutSlots.get(0).getId();

        savedLayoutSlots.get(0).setModuleId(33L);

        layout.setLayoutSlotList(savedLayoutSlots);


        Layout saved = layoutRepository.save(layout);

        assertNotNull(saved.getId());
        assertEquals(2, saved.getLayoutSlotList().size());
        assertTrue(layoutSlotRepository.findById(saved.getLayoutSlotList().get(0).getId()).isPresent());
        assertTrue(layoutSlotRepository.findById(saved.getLayoutSlotList().get(1).getId()).isPresent());
        assertTrue(layoutSlotRepository.findById(id).isPresent());
        assertEquals(33L, layoutSlotRepository.findById(id).get().getModuleId());

    }



}