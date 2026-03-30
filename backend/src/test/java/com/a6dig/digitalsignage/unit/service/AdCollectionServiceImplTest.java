package com.a6dig.digitalsignage.unit.service;

import com.a6dig.digitalsignage.constant.AdContentTypeEnum;
import com.a6dig.digitalsignage.constant.ModuleTypeEnum;
import com.a6dig.digitalsignage.dto.*;
import com.a6dig.digitalsignage.entity.AdCollection;
import com.a6dig.digitalsignage.entity.AdContent;
import com.a6dig.digitalsignage.entity.Domain;
import com.a6dig.digitalsignage.entity.Module;
import com.a6dig.digitalsignage.exception.AdCollectionNotFoundException;
import com.a6dig.digitalsignage.exception.AdContentNotFoundException;
import com.a6dig.digitalsignage.mapper.AdCollectionMapper;
import com.a6dig.digitalsignage.repository.AdCollectionRepository;
import com.a6dig.digitalsignage.repository.AdContentRepository;
import com.a6dig.digitalsignage.service.AdCollectionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class AdCollectionServiceImplTest {
    @Mock
    private AdCollectionRepository adCollectionRepository;

    @Mock
    private AdContentRepository adContentRepository;

    @Mock
    private AdCollectionMapper adCollectionMapper;

    @InjectMocks
    private AdCollectionServiceImpl adCollectionService;


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

    private ModuleResponseDto buildModuleResponseDto(Long id, String name, String config, ModuleTypeEnum type, AdCollectionResponseDto adCollection) {
        ModuleResponseDto dto = new ModuleResponseDto();
        dto.setId(id);
        dto.setName(name);
        dto.setConfig(config);
        dto.setType(type);
        dto.setAdCollectionResponseDto(adCollection);
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());
        return dto;
    }

    private ModuleRequestDto buildModuleRequestDto(String name, String config, ModuleTypeEnum type, AdCollectionRequestUpdateDto adCollection) {
        ModuleRequestDto dto = new ModuleRequestDto();
        dto.setName(name);
        dto.setConfig(config);
        dto.setType(type);
        dto.setAdCollectionRequestUpdateDto(adCollection);
        return dto;
    }

    private ModuleRequestUpdateDto buildModuleRequestUpdateDto(String name, String config, ModuleTypeEnum type, AdCollectionRequestUpdateDto adCollection) {
        ModuleRequestUpdateDto dto = new ModuleRequestUpdateDto();
        dto.setName(name);
        dto.setConfig(config);
        dto.setType(type);
        dto.setAdCollectionRequestUpdateDto(adCollection);
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

    @Test
    void shouldGetAllAdCollection() {
//        Domain domain = this.mockDomain("WEATHER", "Weather");
        Domain domain = new Domain();
        AdCollection adCollection1 = this.buildAdCollection(1L, "Default Ad Collection 1", "http://localhost:3000/collection", new HashSet<>());
        AdCollection adCollection2 = this.buildAdCollection(2L, "Default Ad Collection 2", "http://localhost:3000/collection", new HashSet<>());

        List<AdCollection> adCollections = List.of(adCollection1, adCollection2);

        AdCollectionResponseDto dto1 = this.buildAdCollectionResponseDto(1L, "Default Ad Collection 1", "http://localhost:3000/collection", new ArrayList<>());
        AdCollectionResponseDto dto2 = this.buildAdCollectionResponseDto(2L, "Default Ad Collection 2", "http://localhost:3000/collection", new ArrayList<>());

        when(this.adCollectionRepository.findAll()).thenReturn(adCollections);
        when(this.adCollectionMapper.toAdCollectionResponseDto(adCollections.get(0))).thenReturn(dto1);
        when(this.adCollectionMapper.toAdCollectionResponseDto(adCollections.get(1))).thenReturn(dto2);

        List<AdCollectionResponseDto> response = this.adCollectionService.getAllAdCollections();

        assertEquals(2, response.size());
        assertAdCollection(response.get(0), "Default Ad Collection 1", "http://localhost:3000/collection");
        assertAdCollection(response.get(1), "Default Ad Collection 2", "http://localhost:3000/collection");

        verify(this.adCollectionRepository, times(1)).findAll();
        verify(this.adCollectionMapper, times(2)).toAdCollectionResponseDto(any(AdCollection.class));
    }


    @Test
    void shouldReturnEmptyListWHenNoAdCollection() {
        when(this.adCollectionRepository.findAll()).thenReturn(new ArrayList<>());
        List<AdCollectionResponseDto> response = this.adCollectionService.getAllAdCollections();

        assertEquals(0, response.size());

        verify(this.adCollectionRepository, times(1)).findAll();
        verify(this.adCollectionMapper, never()).toAdCollectionResponseDto(any(AdCollection.class));
    }





    @Test
    void shouldGetAdCollectionById() {
        Domain contentDomain = this.mockDomain("IMAGE", "Image");

        AdContent adContent = this.buildContent(1L, "Ad Content", "http://localhost/content", contentDomain);
        AdCollection adCollection = this.buildAdCollection(1L, "Ad Collection", "http://localhost/collection", Set.of(adContent));

        AdContentResponseDto adContentDto = this.buildAdContentResponseDto(1L, "Ad Content", "http://localhost/content", AdContentTypeEnum.IMAGE);
        AdCollectionResponseDto adCollectionDto = this.buildAdCollectionResponseDto(1L, "Ad Collection", "http://localhost/collection", List.of(adContentDto));

        when(this.adCollectionRepository.findById(1L)).thenReturn(Optional.of(adCollection));
        when(this.adCollectionMapper.toAdCollectionResponseDto(adCollection)).thenReturn(adCollectionDto);


        AdCollectionResponseDto response = this.adCollectionService.getAdCollectionById(1L);


        assertAdCollection(response, "Ad Collection", "http://localhost/collection");
        assertAdContent(response.getAdContents().get(0), "Ad Content", "http://localhost/content");

        verify(this.adCollectionRepository, times(1)).findById(1L);
        verify(this.adCollectionMapper, times(1)).toAdCollectionResponseDto(adCollection);


    }



    @Test
    void shouldThrowWhenAdCollectionNotFoundById() {
        when(this.adCollectionRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(AdCollectionNotFoundException.class, () -> this.adCollectionService.getAdCollectionById(1L));


        verify(this.adCollectionRepository, times(1)).findById(1L);
        verify(this.adCollectionMapper, never()).toAdCollectionResponseDto(any(AdCollection.class));

    }




    @Test
    void shouldGetAdCollectionByIdWithoutAdContent() {
        AdCollection adCollection = this.buildAdCollection(1L, "Ad Collection", "http://localhost/collection", null);

        AdCollectionResponseDto adCollectionDto = this.buildAdCollectionResponseDto(1L, "Ad Collection", "http://localhost/collection", null);


        when(this.adCollectionRepository.findById(1L)).thenReturn(Optional.of(adCollection));
        when(this.adCollectionMapper.toAdCollectionResponseDto(adCollection)).thenReturn(adCollectionDto);

        AdCollectionResponseDto response = this.adCollectionService.getAdCollectionById(1L);

        assertAdCollection(response, "Ad Collection", "http://localhost/collection");
        assertNull(response.getAdContents());

        verify(this.adCollectionRepository, times(1)).findById(1L);
        verify(this.adCollectionMapper, times(1)).toAdCollectionResponseDto(adCollection);

    }




    // create

    @Test
    void shouldCreateAdCollectionWithExistingAdContent() {
        Domain contentDomain = this.mockDomain("IMAGE", "Image");

        AdContentRequestUpdateDto adContentRequest = this.buildAdContentRequestUpdateDto(1L, "New Content", "http://localhost/content", AdContentTypeEnum.IMAGE);
        AdCollectionRequestDto adCollectionRequest = this.buildAdCollectionRequestDto( "New Collection", "http://localhost/collection", List.of(adContentRequest));


        AdContent adContent = this.buildContent(1L, "New Content", "http://localhost/content", contentDomain);
        AdCollection adCollection = this.buildAdCollection(1L, "New Collection", "http://localhost/collection", Set.of(adContent));



        AdContentResponseDto adContentDto = this.buildAdContentResponseDto(1L, "New Content", "http://localhost/content", AdContentTypeEnum.IMAGE);
        AdCollectionResponseDto adCollectionDto = this.buildAdCollectionResponseDto(1L, "New Collection", "http://localhost/collection", List.of(adContentDto));

        when(this.adContentRepository.findAllById(anyCollection())).thenReturn(List.of(adContent));
        when(this.adCollectionRepository.save(any(AdCollection.class))).thenReturn(adCollection);
        when(this.adCollectionMapper.toAdCollectionResponseDto(any(AdCollection.class))).thenReturn(adCollectionDto);

//        when(this.domainCache.buildDomain(AdContentTypeEnum.IMAGE)).thenReturn(contentDomain);
        AdCollectionResponseDto response = this.adCollectionService.createAdCollection(adCollectionRequest);

        assertAdCollection(response,"New Collection", "http://localhost/collection");
        assertAdContent(response.getAdContents().get(0), "New Content", "http://localhost/content");


        verify(this.adContentRepository, times(1)).findAllById(anyCollection());
        verify(this.adCollectionRepository, times(1)).save(any(AdCollection.class));
        verify(this.adContentRepository, never()).save(any(AdContent.class));
        verify(this.adCollectionMapper, times(1)).toAdCollectionResponseDto(adCollection);
    }



    @Test
    void shouldThrowWHenCreateAdCollectionWithNewAdContent() {
        Domain contentDomain = this.mockDomain("IMAGE", "Image");

        AdContentRequestUpdateDto adContentRequest = this.buildAdContentRequestUpdateDto(null, "New Content", "http://localhost/content", AdContentTypeEnum.IMAGE);
        AdCollectionRequestDto adCollectionRequest = this.buildAdCollectionRequestDto( "New Collection", "http://localhost/collection", List.of(adContentRequest));


        AdContent adContent = this.buildContent(1L, "New Content", "http://localhost/content", contentDomain);
        AdCollection adCollection = this.buildAdCollection(1L, "New Collection", "http://localhost/collection", Set.of(adContent));

        assertThrows(AdContentNotFoundException.class, () -> this.adCollectionService.createAdCollection(adCollectionRequest));

        verify(this.adContentRepository, never()).findAllById(anyCollection());
        verify(this.adCollectionRepository, never()).save(any(AdCollection.class));
        verify(this.adContentRepository, never()).save(any(AdContent.class));
        verify(this.adCollectionMapper, never()).toAdCollectionResponseDto(adCollection);
    }




    @Test
    void shouldThrowWHenCreateAdCollectionWithNonExistentAdContent() {
        Domain contentDomain = this.mockDomain("IMAGE", "Image");

        AdContentRequestUpdateDto adContentRequest = this.buildAdContentRequestUpdateDto(2L, "New Content", "http://localhost/content", AdContentTypeEnum.IMAGE);
        AdCollectionRequestDto adCollectionRequest = this.buildAdCollectionRequestDto( "New Collection", "http://localhost/collection", List.of(adContentRequest));


        AdContent adContent = this.buildContent(1L, "New Content", "http://localhost/content", contentDomain);


        when(this.adContentRepository.findAllById(anyCollection())).thenReturn(List.of(adContent));

        assertThrows(AdContentNotFoundException.class, () -> this.adCollectionService.createAdCollection(adCollectionRequest));

        verify(this.adContentRepository, times(1)).findAllById(anyCollection());
        verify(this.adCollectionRepository, never()).save(any(AdCollection.class));
        verify(this.adContentRepository, never()).save(any(AdContent.class));
        verify(this.adCollectionMapper, never()).toAdCollectionResponseDto(any(AdCollection.class));
    }

    // update


    @Test
    void shouldUpdateAdContentById() {
        AdCollection existing = this.buildAdCollection(1L, "New Collection", "http://localhost/collection", new HashSet<>());

        AdCollectionRequestUpdateDto request = this.buildAdCollectionRequestUpdateDto(1L, "Updated Collection", "http://localhost/collection", new ArrayList<>());

        when(adCollectionRepository.findById(1L)).thenReturn(Optional.of(existing));

        AdCollection updated = this.buildAdCollection(1L, "Updated Collection", "http://localhost/collection", new HashSet<>());
        when(adCollectionRepository.save(any(AdCollection.class))).thenReturn(updated);

        AdCollectionResponseDto responseDto = this.buildAdCollectionResponseDto(1L, "Updated Collection", "http://localhost/collection", new ArrayList<>());
        when(adCollectionMapper.toAdCollectionResponseDto(any(AdCollection.class))).thenReturn(responseDto);

        AdCollectionResponseDto response = adCollectionService.updateAdCollectionById(1L, request);

        assertAdCollection(response, "Updated Collection", "http://localhost/collection");
        assertEquals(0, response.getAdContents().size());


        verify(adCollectionRepository, times(1)).findById(1L);
        verify(adCollectionRepository, times(1)).save(any(AdCollection.class));
        verify(adCollectionMapper, times(1)).toAdCollectionResponseDto(any(AdCollection.class));
        verify(adContentRepository, never()).findAllById(anyCollection());
        verify(adContentRepository, never()).save(any(AdContent.class));

    }





    @Test
    void shouldThrowWHenUpdateAdCollectionWithNewAdContent() {
        Domain contentDomain = this.mockDomain("IMAGE", "Image");

        AdCollection existing = this.buildAdCollection(1L, "New Collection", "http://localhost/collection", new HashSet<>());
        AdContentRequestUpdateDto adContentRequest = this.buildAdContentRequestUpdateDto(null, "New Content", "http://localhost/content", AdContentTypeEnum.IMAGE);

        AdCollectionRequestUpdateDto adCollectionRequest = this.buildAdCollectionRequestUpdateDto(1L, "Updated Collection", "http://localhost/collection", List.of(adContentRequest));

        when(adCollectionRepository.findById(1L)).thenReturn(Optional.of(existing));

        AdContent adContent = this.buildContent(null, "New Content", "http://localhost/content", contentDomain);
        AdCollection adCollection = this.buildAdCollection(1L, "New Collection", "http://localhost/collection", Set.of(adContent));

        assertThrows(AdContentNotFoundException.class, () -> this.adCollectionService.updateAdCollectionById(1L, adCollectionRequest));


        verify(this.adCollectionRepository, times(1)).findById(1L);
        verify(this.adContentRepository, never()).findAllById(anyCollection());
        verify(this.adCollectionRepository, never()).save(any(AdCollection.class));
        verify(this.adContentRepository, never()).save(any(AdContent.class));
        verify(this.adCollectionMapper, never()).toAdCollectionResponseDto(adCollection);
    }

    @Test
    void shouldThrowWHenUpdateAdCollectionWithNonExistentAdContent() {
        Domain contentDomain = this.mockDomain("IMAGE", "Image");


        AdContent adContent = this.buildContent(1L, "New Content", "http://localhost/content", contentDomain);
        AdCollection existing = this.buildAdCollection(1L, "New Collection", "http://localhost/collection", new HashSet<>());
        AdContentRequestUpdateDto adContentRequest = this.buildAdContentRequestUpdateDto(2L, "New Content", "http://localhost/content", AdContentTypeEnum.IMAGE);

        AdCollectionRequestUpdateDto adCollectionRequest = this.buildAdCollectionRequestUpdateDto(1L, "Updated Collection", "http://localhost/collection", List.of(adContentRequest));

        when(adCollectionRepository.findById(1L)).thenReturn(Optional.of(existing));

        when(this.adContentRepository.findAllById(anyCollection())).thenReturn(List.of(adContent));

        assertThrows(AdContentNotFoundException.class, () -> this.adCollectionService.updateAdCollectionById(1L, adCollectionRequest));

        verify(this.adCollectionRepository, times(1)).findById(anyLong());
        verify(this.adContentRepository, times(1)).findAllById(anyCollection());
        verify(this.adCollectionRepository, never()).save(any(AdCollection.class));
        verify(this.adContentRepository, never()).save(any(AdContent.class));
        verify(this.adCollectionMapper, never()).toAdCollectionResponseDto(any(AdCollection.class));
    }


    @Test
    void shouldThrowWhenUpdatingNonExistentModule() {
        AdCollectionRequestUpdateDto request = this.buildAdCollectionRequestUpdateDto(1L, "New Ad Collection", "http://localhost/collection", null);
        when(this.adCollectionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AdCollectionNotFoundException.class, () -> this.adCollectionService.updateAdCollectionById(1L, request));

        verify(this.adCollectionRepository, times(1)).findById(1L);
        verify(this.adContentRepository, never()).findAllById(anyCollection());
        verify(this.adCollectionRepository, never()).save(any(AdCollection.class));
        verify(this.adContentRepository, never()).save(any(AdContent.class));
        verify(this.adCollectionMapper, never()).toAdCollectionResponseDto(any(AdCollection.class));
    }


    // delete

    @Test
    void shouldDeleteAdCollectionById() {

        Domain contentDomain = this.mockDomain("IMAGE", "Image");

        AdContent adContent = this.buildContent(1L, "Ad Content", "http://localhost/content", contentDomain);
        AdCollection adCollection = this.buildAdCollection(1L, "Ad Collection", "http://localhost/collection", Set.of(adContent));

        when(this.adCollectionRepository.findById(1L)).thenReturn(Optional.of(adCollection));
        doNothing().when(this.adCollectionRepository).delete(adCollection);

        this.adCollectionService.deleteAdCollectionById(1L);

        verify(this.adCollectionRepository, times(1)).findById(1L);
        verify(this.adCollectionRepository, times(1)).delete(adCollection);
    }

    @Test
    void shouldThrowWhenDeletingNonExistingAdCollection() {
        when(this.adCollectionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AdCollectionNotFoundException.class, () -> this.adCollectionService.deleteAdCollectionById(1L));

        verify(this.adCollectionRepository, times(1)).findById(1L);
        verify(this.adCollectionRepository, never()).deleteById(1L);
    }

    @Test
    void shouldDeleteAllAdCollections() {
        doNothing().when(this.adCollectionRepository).deleteAll();
        this.adCollectionService.deleteAllAdCollections();
        verify(this.adCollectionRepository, times(1)).deleteAll();
    }



}
