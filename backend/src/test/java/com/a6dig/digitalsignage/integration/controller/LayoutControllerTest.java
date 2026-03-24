package com.a6dig.digitalsignage.integration.controller;

import com.a6dig.digitalsignage.dto.*;
import com.a6dig.digitalsignage.entity.Layout;
import com.a6dig.digitalsignage.entity.LayoutSlot;
import com.a6dig.digitalsignage.repository.LayoutRepository;
import com.a6dig.digitalsignage.repository.LayoutSlotRepository;
import com.a6dig.digitalsignage.service.LayoutServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
public class LayoutControllerTest {



    @Autowired
    private TestRestTemplate restTemplate;

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


    private void assertLayout(LayoutResponseDto layout, String expectedName, int expectedCols, int expectedRows) {
        assertNotNull(layout.getId());
        assertNotNull(layout.getName());
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
        assertTrue(layoutSlotRepository.existsById(slot.getId()));
    }

    // GET
//
//    @Test
//    void shouldGetLayoutById() {
//        LayoutRequestDto request = this.buildLayoutRequestDto("Main Layout", 1, 1);
//        ResponseEntity<APIResponse<LayoutResponseDto>> created = restTemplate.exchange(
//                "/api/layouts",
//                HttpMethod.POST,
//                new HttpEntity<>(buildLayoutRequestDto("Main Layout", 1, 1)),
//                new ParameterizedTypeReference<APIResponse<LayoutResponseDto>>() {}
//        );
//
//
//        Long id = Objects.requireNonNull(created.getBody()).getData().getId();
//
//
//        ResponseEntity<APIResponse<LayoutResponseDto>> response = restTemplate.exchange(
//                "/api/layouts/" + id,
//                HttpMethod.GET,
//                new HttpEntity<>(buildLayoutRequestDto("Main Layout", 1, 1)),
//                new ParameterizedTypeReference<APIResponse<LayoutResponseDto>>() {}
//        );
//
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertNotNull(response.getBody().getData());
//        assertEquals(200, response.getBody().getStatus());
//        assertEquals("", response.getBody().getMessage());
//        assertEquals("Main Layout", response.getBody().getData().getName());
//        assertEquals(1, response.getBody().getData().getLayoutCol());
//        assertEquals(1, response.getBody().getData().getLayoutCol());
//        assertTrue(response.getBody().getErrors().isEmpty());
//
//
//    }
}
