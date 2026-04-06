package com.a6dig.digitalsignage.integration.service;


import com.a6dig.digitalsignage.config.DomainCache;
import com.a6dig.digitalsignage.constant.AdContentTypeEnum;
import com.a6dig.digitalsignage.constant.ModuleTypeEnum;
import com.a6dig.digitalsignage.dto.*;
import com.a6dig.digitalsignage.entity.AdCollection;
import com.a6dig.digitalsignage.entity.AdContent;
import com.a6dig.digitalsignage.entity.Domain;
import com.a6dig.digitalsignage.entity.Module;
import com.a6dig.digitalsignage.exception.AdContentNotFoundException;
import com.a6dig.digitalsignage.mapper.AdCollectionMapper;
import com.a6dig.digitalsignage.mapper.AdContentMapper;
import com.a6dig.digitalsignage.repository.AdCollectionRepository;
import com.a6dig.digitalsignage.repository.AdContentRepository;
import com.a6dig.digitalsignage.repository.DomainRepository;
import com.a6dig.digitalsignage.repository.ModuleRepository;
import com.a6dig.digitalsignage.service.AdCollectionService;
import com.a6dig.digitalsignage.service.AdContentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
public class AdContentServiceImplTest {


    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private AdCollectionRepository adCollectionRepository;
    @Autowired
    private AdContentRepository adContentRepository;;
    @Autowired
    private AdContentService adContentService;
    @Autowired
    private AdCollectionService adCollectionService;
    @Autowired
    private DomainRepository domainRepository;
    @Autowired
    private DomainCache domainCache;

    @Autowired
    private AdCollectionMapper adCollectionMapper;


    @Autowired
    private AdContentMapper adContentMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void cleanUp() {
        this.moduleRepository.deleteAll();
        this.adCollectionRepository.deleteAll();
        this.adContentRepository.deleteAll();
    }

    // helpers



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

    private AdContentRequestUpdateDto buildAdContentRequestUpdateDto(Long id, String name, String url, AdContentTypeEnum type) {
        AdContentRequestUpdateDto dto = new AdContentRequestUpdateDto();
        dto.setId(id);
        dto.setName(name);
        dto.setUrl(url);
        dto.setType(type);
        return dto;
    }




    private void assertDomain(Domain domain, Domain expectedDomain){

        assertNotNull(domain.getId());
        assertNotNull(domain.getName());
        assertNotNull(domain.getType());
        assertNotNull(domain.getDescription());
        assertNotNull(domain.getCreatedAt());
        assertNotNull(domain.getUpdatedAt());
        assertTrue(domainRepository.existsById(domain.getId()));

        assertEquals(domain.getType(), expectedDomain.getType());
        assertEquals(domain.getAlphaNumCode(), expectedDomain.getAlphaNumCode());
        assertEquals(domain.getDisplayOrder(), expectedDomain.getDisplayOrder());
        assertEquals(domain.getDescription(), expectedDomain.getDescription());
        assertEquals(domain.getName(), expectedDomain.getName());
    }

    private void assertModule(
            ModuleResponseDto module,
            String expectedName,
            String expectedConfig,
            ModuleTypeEnum moduleTypeEnum) {
        assertNotNull(module.getId());
        assertNotNull(module.getName());
        assertNotNull(moduleTypeEnum);
        assertNotNull(module.getCreatedAt());
        assertNotNull(module.getUpdatedAt());
        assertTrue(moduleRepository.existsById(module.getId()));

        assertEquals(module.getName(), expectedName);
        assertEquals(module.getConfig(), expectedConfig);
        assertEquals(module.getType().name(), moduleTypeEnum.name());

        assertEquals(module.getType(), moduleTypeEnum);
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

        assertTrue(adCollectionRepository.existsById(adCollection.getId()));
    }

    private void assertAdContent(
            AdContentResponseDto adContent,
            String expectedName,
            String expectedUrl,
            AdContentTypeEnum adContentTypeEnum
    ) {
        assertNotNull(adContent.getId());
        assertNotNull(adContent.getName());
        assertNotNull(adContent.getCreatedAt());
        assertNotNull(adContent.getUpdatedAt());
        assertEquals(adContent.getName(), expectedName);
        assertEquals(adContent.getUrl(), expectedUrl);
        assertEquals(adContent.getType(), adContentTypeEnum);


        assertTrue(adContentRepository.existsById(adContent.getId()));
    }

    // get

    @Test
    void shouldGetAllAdContents() {
        Domain contentDomain = this.domainCache.buildDomain(AdContentTypeEnum.IMAGE);
        AdContent adContent1 = this.buildContent(null, "Ad Content 1", "http://localhost:3000/conten1", contentDomain);
        AdContent adContent2 = this.buildContent(null, "Ad Content 2", "http://localhost:3000/conten1", contentDomain);
        List<AdContent> saved = this.adContentRepository.saveAll(List.of(adContent1, adContent2));
        List<AdContentResponseDto> response = this.adContentService.getAllAdContents();
        assertEquals(2, response.size());
        assertAdContent(response.get(0), "Ad Content 1", "http://localhost:3000/conten1", AdContentTypeEnum.IMAGE);
        assertAdContent(response.get(1), "Ad Content 2", "http://localhost:3000/conten1", AdContentTypeEnum.IMAGE);
    }

    @Test
    void shouldReturnEmptyListWhenNoAdContentsFound() {
        List<AdContentResponseDto> response = this.adContentService.getAllAdContents();
        assertTrue(response.isEmpty());
    }

    @Test
    void shouldGetAdContentById() {
        Domain contentDomain = this.domainCache.buildDomain(AdContentTypeEnum.IMAGE);
        AdContent adContent1 = this.buildContent(null, "Ad Content 1", "http://localhost:3000/conten1", contentDomain);
        AdContent adContent2 = this.buildContent(null, "Ad Content 2", "http://localhost:3000/conten1", contentDomain);
        List<AdContent> saved = this.adContentRepository.saveAll(List.of(adContent1, adContent2));
        saved.sort(Comparator.comparing(AdContent::getName));

        AdContentResponseDto response = this.adContentService.getAdContentById(saved.get(0).getId());
        assertAdContent(response, "Ad Content 1", "http://localhost:3000/conten1", AdContentTypeEnum.IMAGE);
    }
    
    @Test
    void shouldThrowWhenAdContentByIdNotFound() {
        assertThrows(AdContentNotFoundException.class, () -> this.adContentService.getAdContentById(1L));
    }

    // post

    @Test
    void shouldSaveAdContent() {
        Domain contentDomain = this.domainCache.buildDomain(AdContentTypeEnum.IMAGE);
        AdContent adContent1 = this.buildContent(null, "Ad Content 1", "http://localhost:3000/conten1", contentDomain);
        AdContent adContent2 = this.buildContent(null, "Ad Content 2", "http://localhost:3000/conten1", contentDomain);
        List<AdContent> saved = this.adContentRepository.saveAll(List.of(adContent1, adContent2));
        List<AdContentResponseDto> response = this.adContentService.getAllAdContents();
        assertEquals(2, response.size());
        assertAdContent(response.get(0), "Ad Content 1", "http://localhost:3000/conten1", AdContentTypeEnum.IMAGE);
        assertAdContent(response.get(1), "Ad Content 2", "http://localhost:3000/conten1", AdContentTypeEnum.IMAGE);
    }

    // update

    @Test
    void shouldUpdateAdContent() {
        Domain contentDomain = this.domainCache.buildDomain(AdContentTypeEnum.IMAGE);
        AdContent adContent1 = this.buildContent(null, "Ad Content 1", "http://localhost:3000/conten1", contentDomain);
        AdContent adContent2 = this.buildContent(null, "Ad Content 2", "http://localhost:3000/conten1", contentDomain);
        List<AdContent> saved = this.adContentRepository.saveAll(List.of(adContent1, adContent2));
        saved.sort(Comparator.comparing(AdContent::getName));

        AdContentRequestUpdateDto updatedRequest = this.buildAdContentRequestUpdateDto(
                saved.get(0).getId(),
                "Updated Ad Content 1", "http://localhost:3000/conten1",
                AdContentTypeEnum.IMAGE
        );

        this.adContentService.updateAdContentById(updatedRequest.getId(), updatedRequest);

        AdContentResponseDto adContent = this.adContentService.getAdContentById(updatedRequest.getId());
        assertAdContent(adContent, "Updated Ad Content 1", "http://localhost:3000/conten1", AdContentTypeEnum.IMAGE);
    }

    @Test
    void shouldThrowWhenUpdateNonExistentAdContent() {

        AdContentRequestUpdateDto updatedRequest = this.buildAdContentRequestUpdateDto(
                1L,
                "Updated Ad Content 1", "http://localhost:3000/conten1",
                AdContentTypeEnum.IMAGE
        );

        assertThrows(AdContentNotFoundException.class, () -> this.adContentService.updateAdContentById(
                updatedRequest.getId(), updatedRequest
        ));
    }


    // delete
    @Test
    void shouldDeleteAdContentById(){
        Domain contentDomain = this.domainCache.buildDomain(AdContentTypeEnum.IMAGE);
        AdContent adContent1 = this.buildContent(null, "Ad Content 1", "http://localhost:3000/conten1", contentDomain);
        AdContent adContent2 = this.buildContent(null, "Ad Content 2", "http://localhost:3000/conten1", contentDomain);
        List<AdContent> saved = this.adContentRepository.saveAll(List.of(adContent1, adContent2));
        this.adContentService.deleteAdContentById(saved.get(0).getId());
        List<AdContentResponseDto> response = this.adContentService.getAllAdContents();
        assertEquals(1, response.size());
    }

    @Test
    void shouldThrowWhenDeleteNonExistentAdContent() {
        assertThrows(AdContentNotFoundException.class, () -> this.adContentService.deleteAdContentById(1L));
    }

    @Test
    void shouldDeleteAllAdContents() {
        Domain contentDomain = this.domainCache.buildDomain(AdContentTypeEnum.IMAGE);
        AdContent adContent1 = this.buildContent(null, "Ad Content 1", "http://localhost:3000/conten1", contentDomain);
        AdContent adContent2 = this.buildContent(null, "Ad Content 2", "http://localhost:3000/conten1", contentDomain);
        List<AdContent> saved = this.adContentRepository.saveAll(List.of(adContent1, adContent2));
        this.adContentService.deleteAllContents();
        List<AdContentResponseDto> response = this.adContentService.getAllAdContents();
        assertTrue(response.isEmpty());
    }

}
