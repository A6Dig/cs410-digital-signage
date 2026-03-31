package com.a6dig.digitalsignage.unit.service;

import ch.qos.logback.core.model.ImplicitModel;
import com.a6dig.digitalsignage.config.DomainCache;
import com.a6dig.digitalsignage.constant.AdContentTypeEnum;
import com.a6dig.digitalsignage.constant.ModuleTypeEnum;
import com.a6dig.digitalsignage.dto.*;
import com.a6dig.digitalsignage.entity.AdCollection;
import com.a6dig.digitalsignage.entity.AdContent;
import com.a6dig.digitalsignage.entity.Domain;
import com.a6dig.digitalsignage.entity.Module;
import com.a6dig.digitalsignage.exception.ModuleNotFoundException;
import com.a6dig.digitalsignage.mapper.ModuleMapper;
import com.a6dig.digitalsignage.repository.AdCollectionRepository;
import com.a6dig.digitalsignage.repository.AdContentRepository;
import com.a6dig.digitalsignage.repository.ModuleRepository;
import com.a6dig.digitalsignage.service.ModuleServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.org.bouncycastle.math.raw.Mod;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ModuleServiceImplTest {
    @Mock
    private ModuleRepository moduleRepository;

    @Mock
    private DomainCache domainCache;

    @Mock
    private ModuleMapper moduleMapper;

    @InjectMocks
    private ModuleServiceImpl moduleService;

    @Mock
    private ObjectMapper mockObjectMapper;

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
        try {
            module.setConfig(this.objectMapper.writeValueAsString(config));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
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
        try {
            dto.setConfig(this.objectMapper.readTree(config));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        dto.setType(type);
        dto.setAdCollectionResponseDto(adCollection);
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());
        return dto;
    }

    private ModuleRequestDto buildModuleRequestDto(String name, String config, ModuleTypeEnum type, AdCollectionRequestUpdateDto adCollection) {
        ModuleRequestDto dto = new ModuleRequestDto();
        dto.setName(name);
        try {
            dto.setConfig(this.objectMapper.readTree(config));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        dto.setType(type);
        dto.setAdCollectionRequestUpdateDto(adCollection);
        return dto;
    }

    private ModuleRequestUpdateDto buildModuleRequestUpdateDto(String name, String config, ModuleTypeEnum type, AdCollectionRequestUpdateDto adCollection) {
        ModuleRequestUpdateDto dto = new ModuleRequestUpdateDto();
        dto.setName(name);
        try {
            dto.setConfig(this.objectMapper.readTree(config));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        dto.setType(type);
        dto.setAdCollectionRequestUpdateDto(adCollection);
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
        try {
            assertEquals(module.getConfig(), this.objectMapper.readTree(expectedConfig));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        assertEquals(module.getType().name(), moduleTypeEnum.name());
    }

    @Test
    void shouldGetAllModules() {
//        Domain domain = this.mockDomain("WEATHER", "Weather");
        Domain domain = new Domain();
        AdCollection adCollection = this.buildAdCollection(1L, "Default Ad Collection", "http://localhost:3000/collection", new HashSet<>());

        List<Module> modules = List.of(
                this.buildModule(1L, "Module 1", "{}", domain, adCollection),
                this.buildModule(2L, "Module 2", "{}", domain, adCollection)
        );

        ModuleResponseDto dto1 = this.buildModuleResponseDto(1L, "Module 1", "{}", ModuleTypeEnum.WEATHER, null);
        ModuleResponseDto dto2 = this.buildModuleResponseDto(2L, "Module 2", "{}", ModuleTypeEnum.WEATHER, null);

        when(this.moduleRepository.findAll()).thenReturn(modules);
        when(this.moduleMapper.toModuleResponseDto(modules.get(0))).thenReturn(dto1);
        when(this.moduleMapper.toModuleResponseDto(modules.get(1))).thenReturn(dto2);

        List<ModuleResponseDto> response = this.moduleService.getAllModules();

        assertEquals(2, response.size());
        assertModule(response.get(0), "Module 1", ModuleTypeEnum.WEATHER, "{}");
        assertModule(response.get(1), "Module 2", ModuleTypeEnum.WEATHER, "{}");

        verify(this.moduleRepository, times(1)).findAll();
        verify(this.moduleMapper, times(2)).toModuleResponseDto(any(Module.class));
    }

    @Test
    void shouldReturnEmptyListWHenNoModules() {
        when(this.moduleRepository.findAll()).thenReturn(new ArrayList<>());
        List<ModuleResponseDto> response = this.moduleService.getAllModules();

        assertEquals(0, response.size());

        verify(this.moduleRepository, times(1)).findAll();
        verify(this.moduleMapper, never()).toModuleResponseDto(any(Module.class));
    }



    @Test
    void shouldGetModuleById() {
        Domain domain = this.mockDomain("WEATHER", "Weather");
        Domain contentDomain = this.mockDomain("IMAGE", "Image");

        AdContent adContent = this.buildContent(1L, "Ad Content", "http://localhost/content", contentDomain);
        AdCollection adCollection = this.buildAdCollection(1L, "Ad Collection", "http://localhost/collection", Set.of(adContent));
        Module module = this.buildModule(1L, "Default Module", "{}", domain, adCollection);

        AdContentResponseDto adContentDto = this.buildAdContentResponseDto(1L, "Ad Content", "http://localhost/content", AdContentTypeEnum.IMAGE);
        AdCollectionResponseDto adCollectionDto = this.buildAdCollectionResponseDto(1L, "Ad Collection", "http://localhost/collection", List.of(adContentDto));
        ModuleResponseDto moduleDto = this.buildModuleResponseDto(1L, "Default Module", "{}", ModuleTypeEnum.WEATHER, adCollectionDto);

        when(this.moduleRepository.findById(1L)).thenReturn(Optional.of(module));
        when(this.moduleMapper.toModuleResponseDto(module)).thenReturn(moduleDto);


        ModuleResponseDto response = this.moduleService.getModuleById(1L);

        assertModule(response, "Default Module", ModuleTypeEnum.WEATHER, "{}");
        assertAdCollection(response.getAdCollectionResponseDto(), "Ad Collection", "http://localhost/collection");
        assertAdContent(response.getAdCollectionResponseDto().getAdContents().get(0), "Ad Content", "http://localhost/content");

        verify(this.moduleRepository, times(1)).findById(1L);
        verify(this.moduleMapper, times(1)).toModuleResponseDto(module);


    }
    
    
    
    @Test
    void shouldThrowWhenModuleNotFoundById() {
        when(this.moduleRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ModuleNotFoundException.class, () -> this.moduleService.getModuleById(1L));


        verify(this.moduleRepository, times(1)).findById(1L);
        verify(this.moduleMapper, never()).toModuleResponseDto(any(Module.class));

    }

    @Test
    void shouldGetModuleByIdWithoutAdCollection() {
        Domain domain = this.mockDomain("WEATHER", "Weather");
        Module module = this.buildModule(1L, "Default Module", "{}", domain, null);
        ModuleResponseDto moduleDto = this.buildModuleResponseDto(1L, "Default Module", "{}", ModuleTypeEnum.WEATHER, null);


        when(this.moduleRepository.findById(1L)).thenReturn(Optional.of(module));
        when(this.moduleMapper.toModuleResponseDto(module)).thenReturn(moduleDto);

        ModuleResponseDto response = this.moduleService.getModuleById(1L);

        assertModule(response, "Default Module", ModuleTypeEnum.WEATHER, "{}");
        assertNull(response.getAdCollectionResponseDto());

        verify(this.moduleRepository, times(1)).findById(1L);
        verify(this.moduleMapper, times(1)).toModuleResponseDto(module);

    }


    // create

    @Test
    void shouldCreateModule() {
        Domain domain = this.mockDomain("WEATHER", "Weather");
        Domain contentDomain = this.mockDomain("IMAGE", "Image");

        AdContentRequestUpdateDto adContentRequest = this.buildAdContentRequestUpdateDto(null, "New Content", "http://localhost/content", AdContentTypeEnum.IMAGE);
        AdCollectionRequestUpdateDto adCollectionRequest = this.buildAdCollectionRequestUpdateDto(null, "New Collection", "http://localhost/collection", List.of(adContentRequest));
        ModuleRequestDto moduleRequest = this.buildModuleRequestDto("New Module", "{}", ModuleTypeEnum.WEATHER, adCollectionRequest);


        AdContent adContent = this.buildContent(1L, "New Content", "http://localhost/content", contentDomain);
        AdCollection adCollection = this.buildAdCollection(1L, "New Collection", "http://localhost/collection", Set.of(adContent));
        Module module = this.buildModule(1L, "New Module", "{}", domain, adCollection);



        AdContentResponseDto adContentDto = this.buildAdContentResponseDto(1L, "New Content", "http://localhost/content", AdContentTypeEnum.IMAGE);
        AdCollectionResponseDto adCollectionDto = this.buildAdCollectionResponseDto(1L, "New Collection", "http://localhost/collection", List.of(adContentDto));
        ModuleResponseDto moduleDto = this.buildModuleResponseDto(1L, "New Module", "{}", ModuleTypeEnum.WEATHER, adCollectionDto);


        when(this.domainCache.buildDomain(ModuleTypeEnum.WEATHER)).thenReturn(domain);
//        when(this.domainCache.buildDomain(AdContentTypeEnum.IMAGE)).thenReturn(contentDomain);
        when(this.moduleRepository.saveAndFlush(any(Module.class))).thenReturn(module);
        when(this.moduleMapper.toModuleResponseDto(module)).thenReturn(moduleDto);
        try {
            when(mockObjectMapper.writeValueAsString(any())).thenReturn("{}");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        ModuleResponseDto response = this.moduleService.createModule(moduleRequest);
        assertModule(moduleDto, "New Module", ModuleTypeEnum.WEATHER, "{}");
        assertAdCollection(response.getAdCollectionResponseDto(), "New Collection", "http://localhost/collection");
        assertAdContent(response.getAdCollectionResponseDto().getAdContents().get(0), "New Content", "http://localhost/content");


        verify(this.domainCache, times(1)).buildDomain(ModuleTypeEnum.WEATHER);
//        verify(this.domainCache, times(1)).buildDomain(AdContentTypeEnum.IMAGE);
        verify(this.moduleRepository, times(1)).saveAndFlush(any(Module.class));
        verify(this.moduleMapper, times(1)).toModuleResponseDto(module);
    }



    @Test
    void shouldCreateModuleWithoutAdCollection() {
        Domain domain = this.mockDomain("WEATHER", "Weather");

        ModuleRequestDto moduleRequest = this.buildModuleRequestDto("New Module", "{}", ModuleTypeEnum.WEATHER, null);
        Module module = this.buildModule(1L, "New Module", "{}", domain, null);
        ModuleResponseDto moduleDto = this.buildModuleResponseDto(1L, "New Module", "{}", ModuleTypeEnum.WEATHER, null);


        when(this.domainCache.buildDomain(ModuleTypeEnum.WEATHER)).thenReturn(domain);
        when(this.moduleRepository.saveAndFlush(any(Module.class))).thenReturn(module);
        when(this.moduleMapper.toModuleResponseDto(module)).thenReturn(moduleDto);
        try {
            when(mockObjectMapper.writeValueAsString(any())).thenReturn("{}");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        ModuleResponseDto response = this.moduleService.createModule(moduleRequest);
        assertModule(moduleDto, "New Module", ModuleTypeEnum.WEATHER, "{}");
        assertNull(response.getAdCollectionResponseDto());

        verify(this.domainCache, times(1)).buildDomain(ModuleTypeEnum.WEATHER);
        verify(this.moduleRepository, times(1)).saveAndFlush(any(Module.class));
        verify(this.moduleMapper, times(1)).toModuleResponseDto(module);
    }



    @Test
    void shouldCreateModuleWithAdCollectionButNoAdContents() {
        Domain domain = this.mockDomain("WEATHER", "Weather");

        AdCollectionRequestUpdateDto adCollectionRequest = this.buildAdCollectionRequestUpdateDto(null, "New Collection", "http://localhost/collection", null);
        ModuleRequestDto moduleRequest = this.buildModuleRequestDto("New Module", "{}", ModuleTypeEnum.WEATHER, adCollectionRequest);


        AdCollection adCollection = this.buildAdCollection(1L, "New Collection", "http://localhost/collection", null);
        Module module = this.buildModule(1L, "New Module", "{}", domain, adCollection);



        AdCollectionResponseDto adCollectionDto = this.buildAdCollectionResponseDto(1L, "New Collection", "http://localhost/collection", null);
        ModuleResponseDto moduleDto = this.buildModuleResponseDto(1L, "New Module", "{}", ModuleTypeEnum.WEATHER, adCollectionDto);


        when(this.domainCache.buildDomain(ModuleTypeEnum.WEATHER)).thenReturn(domain);
        when(this.moduleRepository.saveAndFlush(any(Module.class))).thenReturn(module);
        when(this.moduleMapper.toModuleResponseDto(module)).thenReturn(moduleDto);
        try {
            when(mockObjectMapper.writeValueAsString(any())).thenReturn("{}");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        ModuleResponseDto response = this.moduleService.createModule(moduleRequest);
        assertModule(moduleDto, "New Module", ModuleTypeEnum.WEATHER, "{}");
        assertAdCollection(response.getAdCollectionResponseDto(), "New Collection", "http://localhost/collection");
        assertNull(response.getAdCollectionResponseDto().getAdContents());


        verify(this.domainCache, times(1)).buildDomain(ModuleTypeEnum.WEATHER);
        verify(this.moduleRepository, times(1)).saveAndFlush(any(Module.class));
        verify(this.moduleMapper, times(1)).toModuleResponseDto(module);
    }


    @Test
    void shouldCreateModuleWithMultipleAdContents() {
        Domain domain = this.mockDomain("WEATHER", "Weather");
        Domain contentDomain = this.mockDomain("IMAGE", "Image");
        Domain videoDomain = this.mockDomain("VIDEO", "Video");

        AdContentRequestUpdateDto adContentRequest1 = this.buildAdContentRequestUpdateDto(null, "New Content 1", "http://localhost/content", AdContentTypeEnum.IMAGE);
        AdContentRequestUpdateDto adContentRequest2 = this.buildAdContentRequestUpdateDto(null, "New Content 2", "http://localhost/content", AdContentTypeEnum.VIDEO);
        AdCollectionRequestUpdateDto adCollectionRequest = this.buildAdCollectionRequestUpdateDto(null, "New Collection", "http://localhost/collection", List.of(adContentRequest1, adContentRequest2));
        ModuleRequestDto moduleRequest = this.buildModuleRequestDto("New Module", "{}", ModuleTypeEnum.WEATHER, adCollectionRequest);


        AdContent adContent1 = this.buildContent(1L, "New Content 1", "http://localhost/content", contentDomain);
        AdContent adContent2 = this.buildContent(2L, "New Content 2", "http://localhost/content", videoDomain);
        AdCollection adCollection = this.buildAdCollection(1L, "New Collection", "http://localhost/collection", Set.of(adContent1, adContent2));
        Module module = this.buildModule(1L, "New Module", "{}", domain, adCollection);



        AdContentResponseDto adContentDto1 = this.buildAdContentResponseDto(1L, "New Content 1", "http://localhost/content", AdContentTypeEnum.IMAGE);
        AdContentResponseDto adContentDto2 = this.buildAdContentResponseDto(2L, "New Content 2", "http://localhost/content", AdContentTypeEnum.VIDEO);
        AdCollectionResponseDto adCollectionDto = this.buildAdCollectionResponseDto(1L, "New Collection", "http://localhost/collection", List.of(adContentDto1, adContentDto2));
        ModuleResponseDto moduleDto = this.buildModuleResponseDto(1L, "New Module", "{}", ModuleTypeEnum.WEATHER, adCollectionDto);


        when(this.domainCache.buildDomain(ModuleTypeEnum.WEATHER)).thenReturn(domain);
//        when(this.domainCache.buildDomain(AdContentTypeEnum.IMAGE)).thenReturn(contentDomain);
//        when(this.domainCache.buildDomain(AdContentTypeEnum.VIDEO)).thenReturn(videoDomain);
        when(this.moduleRepository.saveAndFlush(any(Module.class))).thenReturn(module);
        when(this.moduleMapper.toModuleResponseDto(module)).thenReturn(moduleDto);
        try {
            when(mockObjectMapper.writeValueAsString(any())).thenReturn("{}");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        ModuleResponseDto response = this.moduleService.createModule(moduleRequest);
        assertModule(moduleDto, "New Module", ModuleTypeEnum.WEATHER, "{}");
        assertAdCollection(response.getAdCollectionResponseDto(), "New Collection", "http://localhost/collection");
        assertAdContent(response.getAdCollectionResponseDto().getAdContents().get(0), "New Content 1", "http://localhost/content");
        assertAdContent(response.getAdCollectionResponseDto().getAdContents().get(1), "New Content 2", "http://localhost/content");


        verify(this.domainCache, times(1)).buildDomain(ModuleTypeEnum.WEATHER);
//        verify(this.domainCache, times(1)).buildDomain(AdContentTypeEnum.IMAGE);
//        verify(this.domainCache, times(1)).buildDomain(AdContentTypeEnum.VIDEO);
        verify(this.moduleRepository, times(1)).saveAndFlush(any(Module.class));
        verify(this.moduleMapper, times(1)).toModuleResponseDto(module);

    }



    @Test
    void shouldUpdateModuleById() {
        Domain domain = mockDomain("WEATHER", "Weather");

        AdCollection adCollection = this.buildAdCollection(1L, "New Collection", "http://localhost/collection", new HashSet<>());
        Module existing = this.buildModule(1L, "New Module", "{}", domain, adCollection);

        ModuleRequestUpdateDto moduleRequest = this.buildModuleRequestUpdateDto("Updated Module", "{}", ModuleTypeEnum.WEATHER, null);
        Module updated = this.buildModule(1L, "Updated Module", "{}", domain, null);
        ModuleResponseDto responseDto = this.buildModuleResponseDto(1L, "Updated Module", "{}", ModuleTypeEnum.WEATHER, null);


        when(this.moduleRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(this.domainCache.buildDomain(ModuleTypeEnum.WEATHER)).thenReturn(domain);
        when(this.moduleRepository.saveAndFlush(any(Module.class))).thenReturn(updated);
        when(this.moduleMapper.toModuleResponseDto(updated)).thenReturn(responseDto);

        ModuleResponseDto response = this.moduleService.updateModuleById(1L, moduleRequest);

        verify(this.domainCache, times(1)).buildDomain(ModuleTypeEnum.WEATHER);
        verify(this.moduleRepository, times(1)).findById(1L);
        verify(this.moduleRepository, times(1)).saveAndFlush(any(Module.class));
        verify(this.moduleMapper, times(1)).toModuleResponseDto(updated);



    }

    @Test
    void shouldThrowWhenUpdatingNonExistentModule() {
        ModuleRequestUpdateDto request = this.buildModuleRequestUpdateDto("New Module", "{}", ModuleTypeEnum.WEATHER, null);
        when(this.moduleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ModuleNotFoundException.class, () -> this.moduleService.updateModuleById(1L, request));

        verify(this.moduleRepository, never()).saveAndFlush(any(Module.class));
        verify(this.moduleMapper, never()).toModuleResponseDto(any(Module.class));
    }

    @Test
    void shouldDeleteModuleById() {

        Domain domain = this.mockDomain("WEATHER", "Weather");
        Domain contentDomain = this.mockDomain("IMAGE", "Image");

        AdContent adContent = this.buildContent(1L, "Ad Content", "http://localhost/content", contentDomain);
        AdCollection adCollection = this.buildAdCollection(1L, "Ad Collection", "http://localhost/collection", Set.of(adContent));
        Module module = this.buildModule(1L, "Default Module", "{}", domain, adCollection);

        when(this.moduleRepository.findById(1L)).thenReturn(Optional.of(module));
        doNothing().when(this.moduleRepository).delete(module);

        this.moduleService.deleteModuleById(1L);

        verify(this.moduleRepository, times(1)).findById(1L);
        verify(this.moduleRepository, times(1)).delete(module);
    }

    @Test
    void shouldThrowWhenDeletingNonExistingModule() {
        when(this.moduleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ModuleNotFoundException.class, () -> this.moduleService.deleteModuleById(1L));

        verify(this.moduleRepository, times(1)).findById(1L);
        verify(this.moduleRepository, never()).deleteById(1L);
    }

    @Test
    void shouldDeleteAllModules() {
        doNothing().when(this.moduleRepository).deleteAll();
        this.moduleService.deleteAllModules();
        verify(this.moduleRepository, times(1)).deleteAll();
    }

}
