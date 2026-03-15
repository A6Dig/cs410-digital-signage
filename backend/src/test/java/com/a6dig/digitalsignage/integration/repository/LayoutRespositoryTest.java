package com.a6dig.digitalsignage.integration.repository;

import com.a6dig.digitalsignage.entity.Layout;
import com.a6dig.digitalsignage.repository.LayoutRespository;
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
class LayoutSlotRespositoryTest {
    @Autowired
    private LayoutRespository layoutRespository;

    private Layout build(String name, int col, int row){
        Layout layout = new Layout();
        layout.setName(name);
        layout.setLayoutCol(col);
        layout.setLayoutRow(row);
        return layout;
    }

    // GET
    @Test
    void shouldFindById(){
        Layout layout = this.build("Default Layout", 2, 2);

        Layout saved = layoutRespository.save(layout);

        Optional<Layout> result = layoutRespository.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals(2, result.get().getLayoutCol());
        assertEquals(2, result.get().getLayoutRow());
    }

    @Test
    @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldFindAllLayouts(){
        List<Layout> layouts = new ArrayList<>();
        layouts.add(this.build("Default Layout", 1, 1));
        layouts.add(this.build("Default Layout 2", 2, 2));

        List<Layout> saved = layoutRespository.saveAll(layouts);

        assertEquals(2, saved.size());

    }

    @Test
    @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldReturnEmptyWhenNotFound(){
        Optional<Layout> layout = layoutRespository.findById(1L);
        assertFalse(layout.isPresent());
    }


    @Test
    @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldReturnEmptyListWhenNotFound(){
        List<Layout> layout = layoutRespository.findAll();
        assertEquals(0, layout.size());
    }




    // POST

    @Test
    void shouldSave(){
        Layout layout = this.build("Default Layout", 2, 2);

        Layout saved = layoutRespository.save(layout);

        assertNotNull(saved.getId());
        assertEquals("Default Layout", saved.getName());
        assertEquals(2, saved.getLayoutCol());
        assertEquals(2, saved.getLayoutRow());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());
    }

    @Test
    void shouldSaveLayoutWithEmptyName(){
        Layout layout = this.build("", 2, 2);

        Layout saved = layoutRespository.save(layout);

        assertNotNull(saved.getId());
        assertEquals("", saved.getName());
        assertEquals(2, saved.getLayoutCol());
        assertEquals(2, saved.getLayoutRow());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());
    }

    @Test
    void shouldSaveLayoutWithNullName(){
        Layout layout = this.build(null, 2, 2);

        Layout saved = layoutRespository.save(layout);

        assertNotNull(saved.getId());
        assertNull(saved.getName());
        assertEquals(2, saved.getLayoutCol());
        assertEquals(2, saved.getLayoutRow());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());
    }


    // PUT
    @Test
    void shouldUpdateName(){
        Layout layout = this.build(null, 2, 2);

        Layout saved = layoutRespository.save(layout);

        saved.setName("Default Name");

        Layout updated = layoutRespository.save(saved);

        assertNotNull(updated.getId());
        assertNotNull(updated.getName());
        assertEquals("Default Name", updated.getName());
        assertEquals(2, updated.getLayoutCol());
        assertEquals(2, updated.getLayoutRow());
        assertEquals(saved.getCreatedAt(), updated.getCreatedAt());
        assertNotEquals(updated.getCreatedAt(), updated.getUpdatedAt());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());
    }

    @Test
    void shouldUpdateColRow(){
        Layout layout = this.build(null, 2, 2);

        Layout saved = layoutRespository.save(layout);

        saved.setLayoutCol(3);
        saved.setLayoutRow(3);

        Layout updated = layoutRespository.save(saved);

        assertNotNull(updated.getId());
        assertNull(updated.getName());
        assertEquals(3, updated.getLayoutCol());
        assertEquals(3, updated.getLayoutRow());
        assertEquals(saved.getCreatedAt(), updated.getCreatedAt());
        assertNotEquals(updated.getCreatedAt(), updated.getUpdatedAt());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());
    }


    // DELETE
    @Test
    void shouldDeleteById() {
        Layout saved = layoutRespository.save(build("Default Layout", 3, 3));
        layoutRespository.deleteById(saved.getId());
        assertFalse(layoutRespository.findById(saved.getId()).isPresent());
    }
    @Test
    void shouldDeleteAll() {
        layoutRespository.save(build("Default Layout", 3, 3));
        layoutRespository.save(build("Default Layout 2", 4, 4));

        layoutRespository.deleteAll();
        assertEquals(0, layoutRespository.count());
    }

    @Test
    @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldNotThrowExceptionWhenDeletingNonExistingId(){
        assertDoesNotThrow(() -> layoutRespository.deleteById(1L));
    }

    // EXISTENCE
    @Test
    void shouldReturnTrueWhenExist() {
        Layout saved = layoutRespository.save(build("Default Layout", 3, 3));
        assertTrue(layoutRespository.findById(saved.getId()).isPresent());
    }

    @Test
    @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldReturnFalseWhenNotExist() {
        assertFalse(layoutRespository.findById(1L).isPresent());
    }

}