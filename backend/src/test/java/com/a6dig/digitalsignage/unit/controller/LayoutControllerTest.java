package com.a6dig.digitalsignage.unit.controller;

import com.a6dig.digitalsignage.constant.AppConstant;
import com.a6dig.digitalsignage.controller.LayoutController;
import com.a6dig.digitalsignage.dto.*;
import com.a6dig.digitalsignage.entity.Layout;
import com.a6dig.digitalsignage.entity.LayoutSlot;
import com.a6dig.digitalsignage.exception.InvalidLayoutException;
import com.a6dig.digitalsignage.exception.LayoutNotFoundException;
import com.a6dig.digitalsignage.service.LayoutService;
import com.a6dig.digitalsignage.util.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LayoutController.class)
public class LayoutControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LayoutService layoutService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    private LayoutResponseDto buildLayoutResponseDto(Long id, String name, int cols, int rows) {
        LayoutResponseDto dto = new LayoutResponseDto();
        dto.setId(id);
        dto.setName(name);
        dto.setCols(cols);
        dto.setRows(rows);
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());
        dto.setSlots(new ArrayList<>());
        return dto;
    }



    private LayoutRequestDto buildLayoutRequestDto(String name, int cols, int rows) {
        LayoutRequestDto dto = new LayoutRequestDto();
        dto.setName(name);
        dto.setCols(cols);
        dto.setRows(rows);
        dto.setSlots(new ArrayList<>());
        return dto;
    }


    // GET
    @Test
    void shouldGetLayoutById() throws Exception {
        LayoutResponseDto responseDto = this.buildLayoutResponseDto(1L, "Main Layout", 1, 1);

        when(this.layoutService.getLayoutById(1L)).thenReturn(responseDto);

//        mockMvc.perform(get("/api/layouts/1")).andDo(print());

        mockMvc.perform(get("/api/layouts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(""))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("Main Layout"))
                .andExpect(jsonPath("$.data.cols").value(1))
                .andExpect(jsonPath("$.data.rows").value(1))
                .andExpect(jsonPath("$.errors").isEmpty());

        verify(this.layoutService, times(1)).getLayoutById(1L);

    }

    @Test
    void shouldReturn404WhenLayoutNotFound() throws Exception {
        when(this.layoutService.getLayoutById(1000L)).thenThrow(new LayoutNotFoundException(
                AppConstant.ExceptionMessage.LAYOUT_NOT_FOUND,
                List.of(ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.layoutIdDoesNotExist(1000L)))
        ));
//        mockMvc.perform(get("/api/layouts/1000")).andDo(print());

        mockMvc.perform(get("/api/layouts/1000"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(AppConstant.ExceptionMessage.LAYOUT_NOT_FOUND))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.errors").isNotEmpty());
        verify(this.layoutService, times(1)).getLayoutById(1000L);
    }
    @Test
    void shouldThrowInternalServerErrorWhenGetLayoutById() throws Exception {

        when(this.layoutService.getLayoutById(1000L)).thenThrow(new RuntimeException(
                "Some random error message"
        ));
        mockMvc.perform(get("/api/layouts/1000"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(jsonPath("$.message").value(AppConstant.ExceptionMessage.INTERNAL_SERVER_ERROR))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.errors").isNotEmpty());
        verify(this.layoutService, times(1)).getLayoutById(1000L);
    }
    @Test
    void shouldGetAllLayouts() throws Exception {
        List<LayoutResponseDto> layouts = List.of(
                this.buildLayoutResponseDto(1L, "Main Layout", 1, 1),
                this.buildLayoutResponseDto(1L, "Secondary Layout", 1, 1)
        );

        when(this.layoutService.getAllLayouts()).thenReturn(layouts);

//        mockMvc.perform(get("/api/layouts/1")).andDo(print());

        mockMvc.perform(get("/api/layouts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(""))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].name").value("Main Layout"))
                .andExpect(jsonPath("$.data[0].cols").value(1))
                .andExpect(jsonPath("$.data[0].rows").value(1))
                .andExpect(jsonPath("$.data[1].name").value("Secondary Layout"))
                .andExpect(jsonPath("$.data[1].cols").value(1))
                .andExpect(jsonPath("$.data[1].rows").value(1))
                .andExpect(jsonPath("$.errors").isEmpty());

        verify(this.layoutService, times(1)).getAllLayouts();

    }
    @Test
    void shouldGetEmptyListWhenNoLayouts() throws Exception {

        when(this.layoutService.getAllLayouts()).thenReturn(new ArrayList<>());

//        mockMvc.perform(get("/api/layouts/1")).andDo(print());

        mockMvc.perform(get("/api/layouts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(""))
                .andExpect(jsonPath("$.data.length()").value(0))
                .andExpect(jsonPath("$.errors").isEmpty());

        verify(this.layoutService, times(1)).getAllLayouts();

    }

    @Test
    void shouldThrowInternalServerErrorWhenGetAllLayouts() throws Exception {

        when(this.layoutService.getAllLayouts()).thenThrow(new RuntimeException(
                "Some random error message"
        ));
        mockMvc.perform(get("/api/layouts"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(jsonPath("$.message").value(AppConstant.ExceptionMessage.INTERNAL_SERVER_ERROR))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.errors").isNotEmpty());
        verify(this.layoutService, times(1)).getAllLayouts();
    }


    // post
    @Test
    void shouldCreateLayout() throws Exception {
        LayoutRequestDto request = this.buildLayoutRequestDto("Main Layout", 1, 1);
        LayoutResponseDto response = this.buildLayoutResponseDto(1L, "Main Layout", 1, 1);
        when(this.layoutService.createLayout(any(LayoutRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/layouts").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").value(AppConstant.SuccessMessage.LAYOUT_CREATED))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("Main Layout"))
                .andExpect(jsonPath("$.data.cols").value(1))
                .andExpect(jsonPath("$.data.rows").value(1))
                .andExpect(jsonPath("$.errors").isEmpty());

        verify(this.layoutService, times(1)).createLayout(any(LayoutRequestDto.class));

    }

    @Test
    void shouldReturn400WhenCreateLayoutWithInvalidRequest() throws Exception {
        LayoutRequestDto request = this.buildLayoutRequestDto("Main Layout", 0, 1);


        when(this.layoutService.createLayout(any(LayoutRequestDto.class))).thenThrow(new InvalidLayoutException(
                AppConstant.ExceptionMessage.LAYOUT_VALIDATION_FAILED,
                List.of(ErrorMessage.createErrorMessage("Layout column cannot be 0."))
        ));

        mockMvc.perform(post("/api/layouts").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))

                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(AppConstant.ExceptionMessage.LAYOUT_VALIDATION_FAILED))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.errors").isNotEmpty());


        verify(this.layoutService, times(1)).createLayout(any(LayoutRequestDto.class));
    }


    // update

    @Test
    void shouldUpdateLayout() throws Exception {
        LayoutRequestDto request = this.buildLayoutRequestDto("Main Layout", 1, 1);
        LayoutResponseDto response = this.buildLayoutResponseDto(1L, "Main Layout", 1, 1);

        when(this.layoutService.updateLayout(anyLong(), any(LayoutRequestDto.class))).thenReturn(response);

        mockMvc.perform(put("/api/layouts/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(AppConstant.SuccessMessage.LAYOUT_UPDATED))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("Main Layout"))
                .andExpect(jsonPath("$.data.cols").value(1))
                .andExpect(jsonPath("$.data.rows").value(1))
                .andExpect(jsonPath("$.errors").isEmpty());

        verify(this.layoutService, times(1)).updateLayout(anyLong(), any(LayoutRequestDto.class));
    }


    @Test
    void shouldReturn400WhenUpdateLayoutWithInvalidRequest() throws Exception {
        LayoutRequestDto request = this.buildLayoutRequestDto( "Main Layout", 0, 1);


        when(this.layoutService.updateLayout(anyLong(), any(LayoutRequestDto.class))).thenThrow(new InvalidLayoutException(
                AppConstant.ExceptionMessage.LAYOUT_VALIDATION_FAILED,
                List.of(ErrorMessage.createErrorMessage("Layout column cannot be 0."))
        ));

        mockMvc.perform(put("/api/layouts/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))

                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(AppConstant.ExceptionMessage.LAYOUT_VALIDATION_FAILED))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.errors").isNotEmpty());

        verify(this.layoutService, times(1)).updateLayout(anyLong(), any(LayoutRequestDto.class));
    }


    @Test
    void shouldReturn404WhenUpdateLayoutForNonExistingLayout() throws Exception {
        LayoutRequestDto request = this.buildLayoutRequestDto("Main Layout", 0, 1);


        when(this.layoutService.updateLayout(anyLong(), any(LayoutRequestDto.class))).thenThrow(new LayoutNotFoundException(
                AppConstant.ExceptionMessage.LAYOUT_NOT_FOUND,
                List.of(ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.layoutIdDoesNotExist(1L)))
        ));

        mockMvc.perform(put("/api/layouts/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))

                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(AppConstant.ExceptionMessage.LAYOUT_NOT_FOUND))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.errors").isNotEmpty());

        verify(this.layoutService, times(1)).updateLayout(anyLong(), any(LayoutRequestDto.class));
    }

    // delete

    @Test
    void shouldDeleteLayout() throws Exception {

        doNothing().when(this.layoutService).deleteLayout(1L);

        mockMvc.perform(delete("/api/layouts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(AppConstant.SuccessMessage.LAYOUT_DELETED))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.errors").isEmpty());

        verify(this.layoutService, times(1)).deleteLayout(anyLong());
    }



    @Test
    void shouldReturn404WhenDeletingLayoutForNonExistingLayout() throws Exception {
        doThrow(new LayoutNotFoundException(
                AppConstant.ExceptionMessage.LAYOUT_NOT_FOUND,
                List.of(ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.layoutIdDoesNotExist(1L)))
        )).when(this.layoutService).deleteLayout(1L);

        mockMvc.perform(delete("/api/layouts/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(AppConstant.ExceptionMessage.LAYOUT_NOT_FOUND))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.errors").isNotEmpty());

        verify(this.layoutService, times(1)).deleteLayout(anyLong());
    }


    @Test
    void shouldDeleteAllLayouts() throws Exception {

        doNothing().when(this.layoutService).deleteAllLayouts();

        mockMvc.perform(delete("/api/layouts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(AppConstant.SuccessMessage.LAYOUT_ALL_DELETED))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.errors").isEmpty());

        verify(this.layoutService, times(1)).deleteAllLayouts();
    }

}
