package com.a6dig.digitalsignage.integration.repository;

import com.a6dig.digitalsignage.config.DatabaseInit;
import com.a6dig.digitalsignage.config.DomainCache;
import com.a6dig.digitalsignage.constant.ModuleTypeEnum;
import com.a6dig.digitalsignage.dto.LayoutRequestDto;
import com.a6dig.digitalsignage.dto.LayoutSlotRequestUpdateDto;
import com.a6dig.digitalsignage.entity.AdCollection;
import com.a6dig.digitalsignage.entity.Layout;
import com.a6dig.digitalsignage.entity.LayoutSlot;
import com.a6dig.digitalsignage.entity.Module;
import com.a6dig.digitalsignage.repository.LayoutSlotRepository;
import com.a6dig.digitalsignage.repository.LayoutRepository;
import com.a6dig.digitalsignage.repository.ModuleRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({DomainCache.class, DatabaseInit.class})
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
class LayoutSlotRepositoryTest {
    @Autowired
    private LayoutSlotRepository layoutSlotRepository;

    @Autowired
    private LayoutRepository layoutRepository;


    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private DomainCache domainCache;


    private Module buildModule(String name, String config, ModuleTypeEnum type, AdCollection adCollection) {
        Module module = new Module();
        module.setDomain(domainCache.buildDomain(type));
        module.setAdCollection(adCollection);
        module.setName(name);
        module.setConfig(config);
        return module;
    }

    @BeforeEach
    private void setUp() {
//        this.layresl
    }


    private Layout buildLayout(String name, int col, int row){
        Layout layout = new Layout();
        layout.setName(name);
        layout.setCols(col);
        layout.setRows(row);
        return layout;
    }



    private LayoutSlot buildLayoutSlot(Layout layout, Module module, int colPos, int rowPos, int colSpan, int rowSpan, int zIndex) {
        LayoutSlot slot = new LayoutSlot(layout);

        slot.setModule(module);
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
        assertEquals(expectedModuleId, slot.getModule() == null ? null : slot.getModule().getId());
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
        Module savedModule = this.moduleRepository.save(
                this.buildModule("Default Module", "{}", ModuleTypeEnum.WEATHER, null)
        );
        Layout savedLayout = layoutRepository.save(this.buildLayout("Default Layout", 2,2));
        LayoutSlot layoutSlot = this.buildLayoutSlot(savedLayout, savedModule, 2, 2, 2,1,0);



        LayoutSlot saved = layoutSlotRepository.save(layoutSlot);

        Optional<LayoutSlot> result = layoutSlotRepository.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals(savedModule.getId(), result.get().getModule().getId());
        assertEquals(2, result.get().getColPos());
        assertEquals(2, result.get().getRowPos());
        assertEquals(2, result.get().getColSpan());
        assertEquals(1, result.get().getRowSpan());
    }

    @Test
    @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldFindAllLayouts(){
        Module savedModule = this.moduleRepository.save(
                this.buildModule("Default Module 1", "{}", ModuleTypeEnum.WEATHER, null)
        );

        List<LayoutSlot> layoutSlots = new ArrayList<>();

        Layout savedLayout = layoutRepository.save(this.buildLayout("Default Layout", 2,2));

        LayoutSlot layoutSlot1 = this.buildLayoutSlot(savedLayout, savedModule,2, 2, 2,1,0);
        LayoutSlot layoutSlot2 = this.buildLayoutSlot(savedLayout,savedModule, 2, 2, 2,1,0);



        layoutSlots.add(layoutSlot1);
        layoutSlots.add(layoutSlot2);

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
        Module savedModule = this.moduleRepository.save(
                this.buildModule("Default Module", "{}", ModuleTypeEnum.WEATHER, null)
        );
        Layout savedLayout = layoutRepository.save(this.buildLayout("Default Layout", 2,2));

        LayoutSlot layoutSlot = this.buildLayoutSlot(savedLayout,savedModule, 2, 2, 2,1,0);




        LayoutSlot saved = layoutSlotRepository.save(layoutSlot);

        assertNotNull(saved.getId());
        assertEquals(savedModule.getId(), saved.getModule().getId());
        assertEquals(2, saved.getColPos());
        assertEquals(2, saved.getRowPos());
        assertEquals(2, saved.getColSpan());
        assertEquals(1, saved.getRowSpan());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());
    }



    // PUT
    @Test
    void shouldUpdateModule(){
        Module savedModule1 = this.moduleRepository.save(
                this.buildModule("Default Module 1", "{}", ModuleTypeEnum.WEATHER, null)
        );
        Module savedModule2 = this.moduleRepository.save(
                this.buildModule("Default Module 2", "{}", ModuleTypeEnum.WEATHER, null)
        );

        Layout savedLayout = layoutRepository.save(this.buildLayout("Default Layout", 2,2));

        LayoutSlot layoutSlot = this.buildLayoutSlot(savedLayout,savedModule1, 2, 2, 2,1,0);




        LayoutSlot saved = layoutSlotRepository.save(layoutSlot);

        saved.setModule(savedModule2);

        LayoutSlot updated = layoutSlotRepository.save(saved);

        assertNotNull(updated.getId());
        assertEquals(savedModule2, updated.getModule());
        assertEquals(2, saved.getColPos());
        assertEquals(2, saved.getRowPos());
        assertEquals(2, saved.getColSpan());
        assertEquals(1, saved.getRowSpan());
        assertEquals(saved.getCreatedAt(), updated.getCreatedAt());
        assertNotEquals(updated.getCreatedAt(), updated.getUpdatedAt());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());
    }

    @Test
    void shouldUpdateColRow(){

        Module savedModule = this.moduleRepository.save(
                this.buildModule("Default Module", "{}", ModuleTypeEnum.WEATHER, null)
        );
        Layout savedLayout = layoutRepository.save(this.buildLayout("Default Layout", 2,2));

        LayoutSlot layoutSlot = this.buildLayoutSlot(savedLayout,savedModule, 2, 2, 2,1,0);




        LayoutSlot saved = layoutSlotRepository.save(layoutSlot);

        saved.setColPos(2);
        saved.setRowPos(3);

        LayoutSlot updated = layoutSlotRepository.save(saved);


        assertNotNull(updated.getId());
        assertEquals(savedModule.getId(), updated.getModule().getId());
        assertEquals(2, saved.getColPos());
        assertEquals(3, saved.getRowPos());
        assertEquals(2, saved.getColSpan());
        assertEquals(1, saved.getRowSpan());
        assertEquals(saved.getCreatedAt(), updated.getCreatedAt());
        assertNotEquals(updated.getCreatedAt(), updated.getUpdatedAt());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());
    }

    @Test
    void shouldUpdateColSpanRowSpan(){
        Module savedModule = this.moduleRepository.save(
                this.buildModule("Default Module", "{}", ModuleTypeEnum.WEATHER, null)
        );
        Layout savedLayout = layoutRepository.save(this.buildLayout("Default Layout", 2,2));

        LayoutSlot layoutSlot = this.buildLayoutSlot(savedLayout, savedModule, 2, 2, 2,1,0);



        LayoutSlot saved = layoutSlotRepository.save(layoutSlot);

        saved.setColSpan(4);
        saved.setRowSpan(4);

        LayoutSlot updated = layoutSlotRepository.save(saved);


        assertNotNull(updated.getId());
        assertEquals(savedModule.getId(), updated.getModule().getId());
        assertEquals(2, saved.getColPos());
        assertEquals(2, saved.getRowPos());
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
        Module savedModule = this.moduleRepository.save(
                this.buildModule("Default Module", "{}", ModuleTypeEnum.WEATHER, null)
        );
        Layout savedLayout = layoutRepository.save(this.buildLayout("Default Layout", 2,2));

        LayoutSlot layoutSlot = this.buildLayoutSlot(savedLayout, savedModule, 2, 2, 2,1,0);




        LayoutSlot saved = layoutSlotRepository.save(layoutSlot);
        layoutSlotRepository.deleteById(saved.getId());
        assertFalse(layoutSlotRepository.findById(saved.getId()).isPresent());
    }
    @Test
    void shouldDeleteAll() {
        Module savedModule = this.moduleRepository.save(
                this.buildModule("Default Module", "{}", ModuleTypeEnum.WEATHER, null)
        );

        Layout layout = this.buildLayout("Default Layout", 2,2);
        LayoutSlot layoutSlot1 = this.buildLayoutSlot(layout, savedModule, 2, 2, 2,1,0);
        LayoutSlot layoutSlot2 = this.buildLayoutSlot(layout, savedModule, 22, 22, 2,1,0);

        layoutSlotRepository.deleteAll();
        assertEquals(0, layoutSlotRepository.count());
    }

    @Test
    @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldNotThrowExceptionWhenDeletingNonExistingId(){
        assertDoesNotThrow(() -> layoutSlotRepository.deleteById(1L));
    }

    @Test
    @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldDeleteAllLayoutSlotsByLayoutId() {

        Module savedModule = this.moduleRepository.save(
                this.buildModule("Default Module", "{}", ModuleTypeEnum.WEATHER, null)
        );
        // layout 1
        Layout layout1 = this.buildLayout("Default Layout", 2,2);
        LayoutSlot layoutSlot11 = this.buildLayoutSlot(layout1, savedModule, 2, 2, 2,1,0);
        LayoutSlot layoutSlot12 = this.buildLayoutSlot(layout1, savedModule, 22, 22, 2,1,0);

        layout1.setSlots(List.of(layoutSlot11, layoutSlot12));

        Layout saved1 = this.layoutRepository.saveAndFlush(layout1);
        List<LayoutSlot> savedSlots1 = new ArrayList<>(saved1.getSlots());
        savedSlots1.sort(Comparator.comparing(LayoutSlot::getColPos));

        assertLayout(layout1, "Default Layout", 2,2);
        assertEquals(2, saved1.getSlots().size());
        assertLayoutSlot(savedSlots1.get(0), saved1.getId(), savedModule.getId(), 2, 2, 2,1, 0);
        assertLayoutSlot(savedSlots1.get(1), saved1.getId(), savedModule.getId(), 22, 22, 2,1,0);


        // layout 2

        Layout layout2 = this.buildLayout("Secondary Layout", 2,2);
        LayoutSlot layoutSlot21 = this.buildLayoutSlot(layout2, savedModule, 2, 2, 2,1,0);
        LayoutSlot layoutSlot22 = this.buildLayoutSlot(layout2, savedModule, 22, 22, 2,1,0);



        layout2.setSlots(List.of(layoutSlot21, layoutSlot22));

        Layout saved2 = this.layoutRepository.saveAndFlush(layout2);
        List<LayoutSlot> savedSlots2 = new ArrayList<>(saved2.getSlots());
        savedSlots2.sort(Comparator.comparing(LayoutSlot::getColPos));

        assertLayout(layout2, "Secondary Layout", 2,2);
        assertEquals(2, saved2.getSlots().size());
        assertLayoutSlot(savedSlots2.get(0), saved2.getId(), savedModule.getId(), 2, 2, 2,1, 0);
        assertLayoutSlot(savedSlots2.get(1), saved2.getId(), savedModule.getId(), 22, 22, 2,1,0);



        // verify slots deleted from layout 1

        this.layoutSlotRepository.deleteAllLayoutSlotsByLayoutId(saved1.getId());
        this.layoutSlotRepository.flush();

        List<LayoutSlot> response1 = this.layoutSlotRepository.getAllLayoutSlotsByLayoutId(saved1.getId());

        assertEquals(0, response1.size());




        // verify  layout 2's slots didn't get delete accidentally

        List<LayoutSlot> response2 = this.layoutSlotRepository.getAllLayoutSlotsByLayoutId(saved2.getId());

        assertEquals(2, response2.size());
    }



    @Test
    @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldDeleteLayoutSlotsByLayoutId() {

        Module savedModule = this.moduleRepository.save(
                this.buildModule("Default Module", "{}", ModuleTypeEnum.WEATHER, null)
        );
        // layout 1
        Layout layout1 = this.buildLayout("Default Layout", 2,2);
        LayoutSlot layoutSlot11 = this.buildLayoutSlot(layout1, savedModule, 2, 2, 2,1,0);
        LayoutSlot layoutSlot12 = this.buildLayoutSlot(layout1, savedModule, 22, 22, 2,1,0);

        layout1.setSlots(List.of(layoutSlot11, layoutSlot12));

        Layout saved1 = this.layoutRepository.saveAndFlush(layout1);
        List<LayoutSlot> savedSlots1 = new ArrayList<>(saved1.getSlots());
        savedSlots1.sort(Comparator.comparing(LayoutSlot::getColPos));

        assertLayout(layout1, "Default Layout", 2,2);
        assertEquals(2, saved1.getSlots().size());
        assertLayoutSlot(savedSlots1.get(0), saved1.getId(), 1L, 2, 2, 2,1, 0);
        assertLayoutSlot(savedSlots1.get(1), saved1.getId(), 1L, 22, 22, 2,1,0);


        // layout 2

        Layout layout2 = this.buildLayout("Secondary Layout", 2,2);
        LayoutSlot layoutSlot21 = this.buildLayoutSlot(layout2, savedModule, 2, 2, 2,1,0);
        LayoutSlot layoutSlot22 = this.buildLayoutSlot(layout2, savedModule, 22, 22, 2,1,0);



        layout2.setSlots(List.of(layoutSlot21, layoutSlot22));

        Layout saved2 = this.layoutRepository.saveAndFlush(layout2);
        List<LayoutSlot> savedSlots2 = new ArrayList<>(saved2.getSlots());
        savedSlots2.sort(Comparator.comparing(LayoutSlot::getColPos));

        assertLayout(layout2, "Secondary Layout", 2,2);
        assertEquals(2, saved2.getSlots().size());
        assertLayoutSlot(savedSlots2.get(0), saved2.getId(), 1L, 2, 2, 2,1, 0);
        assertLayoutSlot(savedSlots2.get(1), saved2.getId(), 1L, 22, 22, 2,1,0);



        // verify slots deleted from layout 1

        List<Long> idsToDelete = new ArrayList<>(List.of(layoutSlot11.getId()));


        this.layoutSlotRepository.deleteAllByIdInBatch(idsToDelete);
        this.layoutSlotRepository.flush();

        List<LayoutSlot> response1 = this.layoutSlotRepository.getAllLayoutSlotsByLayoutId(saved1.getId());

        assertEquals(1, response1.size());




        // verify  layout 2's slots didn't get delete accidentally

        List<LayoutSlot> response2 = this.layoutSlotRepository.getAllLayoutSlotsByLayoutId(saved2.getId());

        assertEquals(2, response2.size());
    }

    // EXISTENCE
    @Test
    void shouldReturnTrueWhenExist() {

        Module savedModule = this.moduleRepository.save(
                this.buildModule("Default Module", "{}", ModuleTypeEnum.WEATHER, null)
        );
        Layout savedLayout = layoutRepository.save(this.buildLayout("Default Layout", 2,2));

        LayoutSlot layoutSlot = this.buildLayoutSlot(savedLayout, savedModule, 2, 2, 2,1,0);



        LayoutSlot saved = layoutSlotRepository.save(layoutSlot);
        assertTrue(layoutSlotRepository.findById(saved.getId()).isPresent());
    }

    @Test
    @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldReturnFalseWhenNotExist() {
        assertFalse(layoutSlotRepository.findById(1L).isPresent());
    }


}