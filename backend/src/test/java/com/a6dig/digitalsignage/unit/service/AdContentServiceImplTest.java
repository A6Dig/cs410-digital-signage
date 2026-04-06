package com.a6dig.digitalsignage.unit.service;


import com.a6dig.digitalsignage.config.DomainCache;
import com.a6dig.digitalsignage.constant.AdContentTypeEnum;
import com.a6dig.digitalsignage.constant.ModuleTypeEnum;
import com.a6dig.digitalsignage.dto.*;
import com.a6dig.digitalsignage.entity.AdCollection;
import com.a6dig.digitalsignage.entity.AdContent;
import com.a6dig.digitalsignage.entity.Domain;
import com.a6dig.digitalsignage.entity.Module;
import com.a6dig.digitalsignage.exception.AdContentNotFoundException;
import com.a6dig.digitalsignage.mapper.AdContentMapper;
import com.a6dig.digitalsignage.repository.AdCollectionRepository;
import com.a6dig.digitalsignage.repository.AdContentRepository;
import com.a6dig.digitalsignage.service.AdContentServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdContentServiceImplTest {
    @Mock
    private AdCollectionRepository adCollectionRepository;

    @Mock
    private AdContentRepository adContentRepository;

    @Mock
    private AdContentMapper adContentMapper;

    @Mock
    private DomainCache domainCache;

    @InjectMocks
    private AdContentServiceImpl adContentService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    // helper
    private Domain mockDomain(String alphaNumCode, String name) {
        Domain domain = mock(Domain.class);
//        when(domain.getAlphaNumCode()).thenReturn(alphaNumCode);
//        when(domain.getName()).thenReturn(name);
//        when(domain.getDescription()).thenReturn("");
//        when(domain.getDisplayOrder()).thenReturn(1);
        return domain;
    }

    private AdContent buildContent(Long id, String name, String url, Domain domain) {
        AdContent adContent = new AdContent();
        adContent.setId(id);
        adContent.setName(name);
        adContent.setUrl(url);
        adContent.setDomain(domain);
        adContent.setUpdatedAt(LocalDateTime.now());
        adContent.setCreatedAt(LocalDateTime.now());
        return adContent;
    }

    private AdCollection buildAdCollection(Long id, String name, String url, Set<AdContent> adContents) {
        AdCollection adCollection = new AdCollection();
        adCollection.setId(id);
        adCollection.setName(name);
        adCollection.setUrl(url);
        adCollection.setAdContents(adContents);
        adCollection.setUpdatedAt(LocalDateTime.now());
        adCollection.setCreatedAt(LocalDateTime.now());
        return adCollection;
    }

    private Module buildModule(Long id, String name, String config, Domain domain, AdCollection adCollection) {
        Module module = new Module();
        module.setId(id);
        module.setName(name);
        module.setConfig(config);
        module.setDomain(domain);
        module.setAdCollection(adCollection);
        module.setUpdatedAt(LocalDateTime.now());
        module.setCreatedAt(LocalDateTime.now());
        return module;
    }

    private AdContentResponseDto buildAdContentResponseDto(Long id, String name, String url, AdContentTypeEnum type) {
        AdContentResponseDto dto = new AdContentResponseDto();
        dto.setId(id);
        dto.setName(name);
        dto.setUrl(url);
        dto.setType(type);
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());
        return dto;
    }

    private AdCollectionResponseDto buildAdCollectionResponseDto(Long id, String name, String url, List<AdContentResponseDto> adContents) {
        AdCollectionResponseDto dto = new AdCollectionResponseDto();
        dto.setId(id);
        dto.setName(name);
        dto.setUrl(url);
        dto.setAdContents(adContents);
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());
        return dto;
    }

    private ModuleResponseDto buildModuleResponseDto(Long id, String name, String config, ModuleTypeEnum type, AdCollectionResponseDto adCollection) throws JsonProcessingException {


        ModuleResponseDto dto = new ModuleResponseDto();
        dto.setId(id);
        dto.setName(name);
        dto.setConfig(this.objectMapper.readTree(config));
        dto.setType(type);
        dto.setAdCollection(adCollection);
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());
        return dto;
    }

    private ModuleRequestDto buildModuleRequestDto(String name, String config, ModuleTypeEnum type, AdCollectionRequestUpdateDto adCollection) throws JsonProcessingException {
        ModuleRequestDto dto = new ModuleRequestDto();
        dto.setName(name);
        dto.setConfig(this.objectMapper.readTree(config));
        dto.setType(type);
        dto.setAdCollection(adCollection);
        return dto;
    }

    private ModuleRequestUpdateDto buildModuleRequestUpdateDto(String name, String config, ModuleTypeEnum type, AdCollectionRequestUpdateDto adCollection) throws JsonProcessingException {
        ModuleRequestUpdateDto dto = new ModuleRequestUpdateDto();
        dto.setName(name);
        dto.setConfig(this.objectMapper.readTree(config));
        dto.setType(type);
        dto.setAdCollection(adCollection);
        return dto;
    }

    private AdCollectionRequestDto buildAdCollectionRequestDto(String name, String url, List<AdContentRequestUpdateDto> adContents) {
        AdCollectionRequestDto dto = new AdCollectionRequestDto();
        dto.setName(name);
        dto.setUrl(url);
        dto.setAdContents(adContents);
        return dto;
    }
    private AdCollectionRequestUpdateDto buildAdCollectionRequestUpdateDto(Long id, String name, String url, List<AdContentRequestUpdateDto> adContents) {
        AdCollectionRequestUpdateDto dto = new AdCollectionRequestUpdateDto();
        dto.setId(id);
        dto.setName(name);
        dto.setUrl(url);
        dto.setAdContents(adContents);
        return dto;
    }



    private AdContentRequestDto buildAdContentRequestDto(String name, String url, AdContentTypeEnum type) {
        AdContentRequestDto dto = new AdContentRequestDto();
        dto.setName(name);
        dto.setUrl(url);
        dto.setType(type);
        return dto;
    }

    private AdContentRequestUpdateDto buildAdContentRequestUpdateDto(Long id, String name, String url, AdContentTypeEnum type) {
        AdContentRequestUpdateDto dto = new AdContentRequestUpdateDto();
        dto.setId(id);
        dto.setName(name);
        dto.setUrl(url);
        dto.setType(type);
        return dto;
    }



    private void assertAdCollection(
            AdCollectionResponseDto adCollection,
            String expectedName,
            String expectedUrl
    ) {
        assertNotNull(adCollection.getId());
        assertNotNull(adCollection.getName());
        assertNotNull(adCollection.getCreatedAt());
        assertNotNull(adCollection.getUpdatedAt());
        assertEquals(adCollection.getName(), expectedName);
        assertEquals(adCollection.getUrl(), expectedUrl);
    }

    private void assertAdContent(
            AdContentResponseDto adContent,
            String expectedName,
            String expectedUrl
    ) {
        assertNotNull(adContent.getId());
        assertNotNull(adContent.getName());
        assertNotNull(adContent.getCreatedAt());
        assertNotNull(adContent.getUpdatedAt());
        assertEquals(adContent.getName(), expectedName);
        assertEquals(adContent.getUrl(), expectedUrl);
    }



    private void assertModule(
            ModuleResponseDto module,
            String expectedName,
            ModuleTypeEnum moduleTypeEnum,
            String expectedConfig) {
        assertNotNull(module.getId());
        assertNotNull(module.getName());
        assertNotNull(moduleTypeEnum);
        assertNotNull(module.getCreatedAt());
        assertNotNull(module.getUpdatedAt());
        assertEquals(module.getName(), expectedName);
        assertEquals(module.getConfig(), expectedConfig);
        assertEquals(module.getType().name(), moduleTypeEnum.name());
    }


    // get

    @Test
    void shouldGetAllAdContents() {
        Domain domain = this.mockDomain("IMAGE", "Image");

        AdContent adContent1 = this.buildContent(1L, "Ad Content 1", "http://localhost:3000/image1", domain);
        AdContent adContent2 = this.buildContent(2L, "Ad Content 2", "http://localhost:3000/image1", domain);
        List<AdContent> adContents = List.of(adContent1, adContent2);
        when(this.adContentRepository.findAll()).thenReturn(adContents);

        AdContentResponseDto dto1 = this.buildAdContentResponseDto(1L, "Ad Content 1", "http://localhost:3000/image1", AdContentTypeEnum.IMAGE);
        when(this.adContentMapper.toAdContentResponseDto(adContent1)).thenReturn(dto1);

        AdContentResponseDto dto2 = this.buildAdContentResponseDto(2L, "Ad Content 2", "http://localhost:3000/image1", AdContentTypeEnum.IMAGE);
        when(this.adContentMapper.toAdContentResponseDto(adContent2)).thenReturn(dto2);

        List<AdContentResponseDto> response = this.adContentService.getAllAdContents();

        assertEquals(2, response.size());
        assertAdContent(response.get(0), "Ad Content 1", "http://localhost:3000/image1");
        assertAdContent(response.get(1), "Ad Content 2", "http://localhost:3000/image1");

        verify(this.adContentRepository, times(1)).findAll();
        verify(this.adContentMapper, times(2)).toAdContentResponseDto(any(AdContent.class));

    }

    @Test
    void shouldReturnEmptyListWhenNoAdContentFound() {
        when(this.adContentRepository.findAll()).thenReturn(new ArrayList<>());
        List<AdContentResponseDto> response = this.adContentService.getAllAdContents();
        assertTrue(response.isEmpty());
        verify(this.adContentRepository, times(1)).findAll();
        verify(this.adContentMapper, times(0)).toAdContentResponseDto(any());
    }


    @Test
    void shouldGetAdContentById() {
        Domain contentDomain = this.mockDomain("IMAGE", "Image");
        AdContent adContent = this.buildContent(1L, "Default Ad Content", "http://localhost:3000/content1", contentDomain);
        when(this.adContentRepository.findById(1L)).thenReturn(Optional.of(adContent));

        AdContentResponseDto dto = this.buildAdContentResponseDto(1L, "Default Ad Content", "http://localhost:3000/content1", AdContentTypeEnum.IMAGE);
        when(this.adContentMapper.toAdContentResponseDto(adContent)).thenReturn(dto);

        AdContentResponseDto response = this.adContentService.getAdContentById(1L);

        assertAdContent(response, "Default Ad Content", "http://localhost:3000/content1");

        verify(this.adContentRepository, times(1)).findById(1L);
        verify(this.adContentMapper, times(1)).toAdContentResponseDto(adContent);

    }

    @Test
    void shouldThrowWhenAdContentByIdNotFound() {
        when(this.adContentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(AdContentNotFoundException.class, () -> this.adContentService.getAdContentById(1L));
        verify(this.adContentRepository, times(1)).findById(1L);
        verify(this.adContentMapper, never()).toAdContentResponseDto(any(AdContent.class));
    }

    // create

    @Test
    void shouldCreateAdContent() {
        Domain contentDomain = this.mockDomain("IMAGE", "Image");
        when(this.domainCache.buildDomain(AdContentTypeEnum.IMAGE)).thenReturn(contentDomain);

        AdContentRequestDto requestDto = this.buildAdContentRequestDto("Ad Content", "http://localhost/content1", AdContentTypeEnum.IMAGE);

        AdContent adContent = this.buildContent(1L, "Ad Content", "http://localhost/content1", contentDomain);
        when(this.adContentRepository.save(any(AdContent.class))).thenReturn(adContent);

        AdContentResponseDto responseDto = this.buildAdContentResponseDto(1L, "Ad Content", "http://localhost/content1", AdContentTypeEnum.IMAGE);
        when(this.adContentMapper.toAdContentResponseDto(adContent)).thenReturn(responseDto);

        AdContentResponseDto response = this.adContentService.createAdContent(requestDto);

        assertAdContent(response, "Ad Content", "http://localhost/content1");
        verify(this.adContentRepository, times(1)).save(any(AdContent.class));
        verify(this.adContentMapper, times(1)).toAdContentResponseDto(adContent);
    }

    // update

    @Test
    void shouldUpdateAdContent() {
        Domain contentDomain = this.mockDomain("IMAGE", "Image");
        when(this.domainCache.buildDomain(AdContentTypeEnum.IMAGE)).thenReturn(contentDomain);

        AdContent existing = this.buildContent(1L, "New Content", "http://localhost/content1", contentDomain);
        when(this.adContentRepository.findById(1L)).thenReturn(Optional.of(existing));


        AdContent updated = this.buildContent(1L, "Updated Content", "http://localhost/content1", contentDomain);
        when(this.adContentRepository.save(any(AdContent.class))).thenReturn(updated);

        AdContentResponseDto responseDto = this.buildAdContentResponseDto(1L, "Updated Content", "http://localhost/content1", AdContentTypeEnum.IMAGE);
        when(this.adContentMapper.toAdContentResponseDto(updated)).thenReturn(responseDto);

        AdContentRequestUpdateDto request = this.buildAdContentRequestUpdateDto(1L, "Updated Content", "http://localhost/content1", AdContentTypeEnum.IMAGE);

        AdContentResponseDto response = this.adContentService.updateAdContentById(1L, request);

        assertAdContent(response, "Updated Content", "http://localhost/content1");

        verify(this.adContentRepository, times(1)).findById(1L);
        verify(this.adContentRepository, times(1)).save(existing);
        verify(this.adContentMapper, times(1)).toAdContentResponseDto(updated);

    }

    @Test
    void shouldThrowWhenUpdatingNonExistentAdContent() {
        AdContentRequestUpdateDto request = this.buildAdContentRequestUpdateDto(1L, "Updated Content", "http://localhost/content1", AdContentTypeEnum.IMAGE);

        when(this.adContentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(AdContentNotFoundException.class, () -> this.adContentService.updateAdContentById(1L, request));


        verify(this.adContentRepository, times(1)).findById(1L);
        verify(this.adContentRepository, never()).save(any(AdContent.class));
        verify(this.adContentMapper, never()).toAdContentResponseDto(any(AdContent.class));
    }

    // delete
    @Test
    void shouldDeleteAdContentById() {
        Domain contetnDomain = this.mockDomain("IMAGE", "Image");
        AdContent existing = this.buildContent(1L, "New Ad Content", "http://localhost:3000/content", contetnDomain);
        when(this.adContentRepository.findById(1L)).thenReturn(Optional.of(existing));
        doNothing().when(this.adContentRepository).delete(existing);
        this.adContentService.deleteAdContentById(1L);
        verify(this.adContentRepository, times(1)).findById(1L);
        verify(this.adContentRepository, times(1)).delete(existing);
    }

    @Test
    void shouldThrowWhenDeleteNonExistentAdContent() {
        when(this.adContentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(AdContentNotFoundException.class, () -> this.adContentService.deleteAdContentById(1L));
        verify(this.adContentRepository, times(1)).findById(1L);
        verify(this.adContentRepository, never()).delete(any(AdContent.class));
    }

    @Test
    void shouldDeleteAllAdContents() {
        doNothing().when(this.adContentRepository).deleteAll();
        this.adContentService.deleteAllContents();
        verify(this.adContentRepository, times(1)).deleteAll();
    }

}
