package com.a6dig.digitalsignage.integration.service;


import com.a6dig.digitalsignage.config.DomainCache;
import com.a6dig.digitalsignage.constant.AdContentTypeEnum;
import com.a6dig.digitalsignage.constant.ModuleTypeEnum;
import com.a6dig.digitalsignage.dto.*;
import com.a6dig.digitalsignage.entity.AdCollection;
import com.a6dig.digitalsignage.entity.AdContent;
import com.a6dig.digitalsignage.entity.Domain;
import com.a6dig.digitalsignage.entity.Module;
import com.a6dig.digitalsignage.exception.AdCollectionNotFoundException;
import com.a6dig.digitalsignage.exception.ModuleNotFoundException;
import com.a6dig.digitalsignage.mapper.AdCollectionMapper;
import com.a6dig.digitalsignage.mapper.AdContentMapper;
import com.a6dig.digitalsignage.repository.AdCollectionRepository;
import com.a6dig.digitalsignage.repository.AdContentRepository;
import com.a6dig.digitalsignage.repository.DomainRepository;
import com.a6dig.digitalsignage.repository.ModuleRepository;
import com.a6dig.digitalsignage.service.ModuleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
public class ModuleServiceImplTest {

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private AdCollectionRepository adCollectionRepository;
    @Autowired
    private AdContentRepository adContentRepository;;
    @Autowired
    private ModuleService moduleService;
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
        try {
            module.setConfig(config == null ? null : this.objectMapper.writeValueAsString(config));
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
            dto.setConfig(config == null ? null : this.objectMapper.readTree(config));
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
            dto.setConfig(config == null ? null : this.objectMapper.readTree(config));
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
            dto.setConfig(config == null ? null : this.objectMapper.readTree(config));
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
        try {
            assertEquals(module.getConfig(), this.objectMapper.readTree(expectedConfig));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
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
    void shouldGetAllModules() {

        ModuleRequestDto dto1 = this.buildModuleRequestDto("Module 1", "{}", ModuleTypeEnum.WEATHER, null);
        ModuleRequestDto dto2 = this.buildModuleRequestDto("Module 2", "{}", ModuleTypeEnum.WEATHER, null);

        ModuleResponseDto created1 = this.moduleService.createModule(dto1);
        ModuleResponseDto created2 = this.moduleService.createModule(dto2);

        List<ModuleResponseDto> response = new ArrayList<>(this.moduleService.getAllModules());
        response.sort(Comparator.comparing(ModuleResponseDto::getName));

        assertModule(response.get(0), "Module 1", "{}", ModuleTypeEnum.WEATHER);
        assertModule(response.get(1), "Module 2", "{}", ModuleTypeEnum.WEATHER);


    }

    @Test
    void shouldReturnEmptyListWhenNoModules() {
        List<ModuleResponseDto> response = this.moduleService.getAllModules();
        assertTrue(response.isEmpty());
    }

    @Test
    void shouldGetModuleById() {
        ModuleResponseDto created = this.moduleService.createModule(
                this.buildModuleRequestDto("Default Module", "{}", ModuleTypeEnum.WEATHER, null)
        );
        ModuleResponseDto response = this.moduleService.getModuleById(created.getId());
        assertModule(response, "Default Module", "{}", ModuleTypeEnum.WEATHER);
    }

    @Test
    void shouldGetModulesByIdWithAdCollection() {

        AdContent adContent = this.adContentRepository.save(this.buildContent(null, "New Content", "http://localhost/content", this.domainCache.buildDomain(AdContentTypeEnum.IMAGE)));
        AdContentRequestUpdateDto adContentRequest = this.buildAdContentRequestUpdateDto(adContent.getId(), "New Content", "http://localhost/content", AdContentTypeEnum.IMAGE);

        AdCollection adCollection = this.adCollectionRepository.save(this.buildAdCollection(null, "New Collection", "http://localhost/collection", Set.of(adContent)));
        AdCollectionRequestUpdateDto adCollectionRequest = this.buildAdCollectionRequestUpdateDto(adCollection.getId(), "New Collection", "http://localhost/collection", List.of(adContentRequest));

        ModuleRequestDto moduleRequest = this.buildModuleRequestDto("New Module", "{}", ModuleTypeEnum.WEATHER, adCollectionRequest);

        ModuleResponseDto created = this.moduleService.createModule(moduleRequest);

        ModuleResponseDto response = this.moduleService.getModuleById(created.getId());

        assertModule(response, "New Module", "{}", ModuleTypeEnum.WEATHER);
        assertAdCollection(response.getAdCollectionResponseDto(), "New Collection", "http://localhost/collection");
        assertAdContent(response.getAdCollectionResponseDto().getAdContents().get(0), "New Content", "http://localhost/content", AdContentTypeEnum.IMAGE);
    }

    @Test
    void shouldThrowWhenModuleNotFoundById() {
        assertThrows(ModuleNotFoundException.class, () -> this.moduleService.getModuleById(1L));
    }


    // create

    @Test
    void shouldCreateModule() {
        ModuleRequestDto request = this.buildModuleRequestDto("New Module", "{}", ModuleTypeEnum.WEATHER, null);
        ModuleResponseDto response = this.moduleService.createModule(request);
        assertModule(response, "New Module", "{}", ModuleTypeEnum.WEATHER);
        assertNull(response.getAdCollectionResponseDto());
    }



    @Test
    void shouldCreateModuleWithExistingAdCollection() {

        AdContent adContent = this.adContentRepository.save(this.buildContent(null, "New Content", "http://localhost/content", this.domainCache.buildDomain(AdContentTypeEnum.IMAGE)));
        AdContentRequestUpdateDto adContentRequest = this.buildAdContentRequestUpdateDto(adContent.getId(), "New Content", "http://localhost/content", AdContentTypeEnum.IMAGE);

        AdCollection adCollection = this.adCollectionRepository.save(this.buildAdCollection(null, "New Collection", "http://localhost/collection", Set.of(adContent)));
        AdCollectionRequestUpdateDto adCollectionRequest = this.buildAdCollectionRequestUpdateDto(adCollection.getId(), "New Collection", "http://localhost/collection", List.of(adContentRequest));

        ModuleRequestDto moduleRequest = this.buildModuleRequestDto("New Module", "{}", ModuleTypeEnum.WEATHER, adCollectionRequest);

        ModuleResponseDto created = this.moduleService.createModule(moduleRequest);

        assertModule(created, "New Module", "{}", ModuleTypeEnum.WEATHER);
        assertAdCollection(created.getAdCollectionResponseDto(), "New Collection", "http://localhost/collection");
        assertAdContent(created.getAdCollectionResponseDto().getAdContents().get(0), "New Content", "http://localhost/content", AdContentTypeEnum.IMAGE);
    }


    @Test
    void shouldCreateModuleButNoNewAdCollection() {

        AdContentRequestUpdateDto adContentRequest = this.buildAdContentRequestUpdateDto(null, "New Content", "http://localhost/content", AdContentTypeEnum.IMAGE);

        AdCollectionRequestUpdateDto adCollectionRequest = this.buildAdCollectionRequestUpdateDto(null, "New Collection", "http://localhost/collection", List.of(adContentRequest));
        ModuleRequestDto moduleRequest = this.buildModuleRequestDto("New Module", "{}", ModuleTypeEnum.WEATHER, adCollectionRequest);

        ModuleResponseDto created = this.moduleService.createModule(moduleRequest);

        assertModule(created, "New Module", "{}", ModuleTypeEnum.WEATHER);
        assertNull(created.getAdCollectionResponseDto());
    }

    @Test
    void shouldThrowWhenCreateModuleWithNonExistentAdCollection() {
        AdContentRequestUpdateDto adContentRequest = this.buildAdContentRequestUpdateDto(null, "New Content", "http://localhost/content", AdContentTypeEnum.IMAGE);

        AdCollectionRequestUpdateDto adCollectionRequest = this.buildAdCollectionRequestUpdateDto(5L, "New Collection", "http://localhost/collection", List.of(adContentRequest));
        ModuleRequestDto moduleRequest = this.buildModuleRequestDto("New Module", "{}", ModuleTypeEnum.WEATHER, adCollectionRequest);

        assertThrows(AdCollectionNotFoundException.class, () -> this.moduleService.createModule(moduleRequest));

    }


    // update

    @Test
    void shouldUpdateModuleName() {
        ModuleResponseDto created = this.moduleService.createModule(this.buildModuleRequestDto("New Module", "{}", ModuleTypeEnum.WEATHER, null));
        ModuleRequestUpdateDto request = this.buildModuleRequestUpdateDto("Updated Module", null, null, null);
        ModuleResponseDto response = this.moduleService.updateModuleById(created.getId(), request);
        assertModule(response, "Updated Module", "{}", ModuleTypeEnum.WEATHER);
        assertNull(response.getAdCollectionResponseDto());
    }

    @Test
    void shouldUpdateModuleConfig() {

        String config = """
                {
                    "name": "Some Content",
                    "contents": [
                        {"name": "Content 1", "url": "https://localhost:3000/content1", "duration": 12},
                        {"name": "Content 1", "url": "https://localhost:3000/content1", "duration": 12},
                        {"name": "Content 1", "url": "https://localhost:3000/content1", "duration": 12},
                        {"name": "Content 1", "url": "https://localhost:3000/content1", "duration": 12}
                    ]
                }
                
                """;

        ModuleResponseDto created = this.moduleService.createModule(this.buildModuleRequestDto("New Module", "{}", ModuleTypeEnum.WEATHER, null));
        ModuleRequestUpdateDto request = this.buildModuleRequestUpdateDto(null, config, null, null);
        ModuleResponseDto response = this.moduleService.updateModuleById(created.getId(), request);
        assertModule(response, "New Module", config, ModuleTypeEnum.WEATHER);
        assertNull(response.getAdCollectionResponseDto());
    }


    @Test
    void shouldUpdateModuleType() {
        ModuleResponseDto created = this.moduleService.createModule(this.buildModuleRequestDto("New Module", "{}", ModuleTypeEnum.WEATHER, null));
        ModuleRequestUpdateDto request = this.buildModuleRequestUpdateDto("Updated Module", null, ModuleTypeEnum.CLOCK, null);
        ModuleResponseDto response = this.moduleService.updateModuleById(created.getId(), request);
        assertModule(response, "Updated Module", "{}", ModuleTypeEnum.CLOCK);
        assertNull(response.getAdCollectionResponseDto());
    }

    @Test
    void shouldThrowWhenUpdateNonExistentModule() {
        ModuleRequestUpdateDto request = this.buildModuleRequestUpdateDto("Updated Module", null, ModuleTypeEnum.CLOCK, null);
        assertThrows(ModuleNotFoundException.class, () -> this.moduleService.updateModuleById(1L, request));

    }



    @Test
    void shouldThrowWhenUpdateModuleWithNonExistentAdCollection() {
        ModuleResponseDto created = this.moduleService.createModule(this.buildModuleRequestDto("New Module", "{}", ModuleTypeEnum.WEATHER, null));

        AdContentRequestUpdateDto adContentRequest = this.buildAdContentRequestUpdateDto(null, "New Content", "http://localhost/content", AdContentTypeEnum.IMAGE);

        AdCollectionRequestUpdateDto adCollectionRequest = this.buildAdCollectionRequestUpdateDto(5L, "New Collection", "http://localhost/collection", List.of(adContentRequest));
        ModuleRequestUpdateDto moduleRequest = this.buildModuleRequestUpdateDto("New Module", "{}", ModuleTypeEnum.WEATHER, adCollectionRequest);

        assertThrows(AdCollectionNotFoundException.class, () -> this.moduleService.updateModuleById(created.getId(), moduleRequest));

    }

    // delete

    @Test
    void shouldDeleteModuleById() {
        ModuleResponseDto created = this.moduleService.createModule(this.buildModuleRequestDto("New Module", "{}", ModuleTypeEnum.WEATHER, null));
        this.moduleService.deleteModuleById(created.getId());
        assertFalse(this.moduleRepository.existsById(created.getId()));

    }

    @Test
    @Transactional
    void shouldDeleteModuleButNotAdCollection() {
        AdContent adContent = this.adContentRepository.save(this.buildContent(null, "New Content", "http://localhost/content", this.domainCache.buildDomain(AdContentTypeEnum.IMAGE)));
        AdContentRequestUpdateDto adContentRequest = this.buildAdContentRequestUpdateDto(adContent.getId(), "New Content", "http://localhost/content", AdContentTypeEnum.IMAGE);

        AdCollection adCollection = this.adCollectionRepository.save(this.buildAdCollection(null, "New Collection", "http://localhost/collection", Set.of(adContent)));
        AdCollectionRequestUpdateDto adCollectionRequest = this.buildAdCollectionRequestUpdateDto(adCollection.getId(), "New Collection", "http://localhost/collection", List.of(adContentRequest));

        ModuleRequestDto moduleRequest = this.buildModuleRequestDto("New Module", "{}", ModuleTypeEnum.WEATHER, adCollectionRequest);

        ModuleResponseDto created = this.moduleService.createModule(moduleRequest);

        this.moduleService.deleteModuleById(created.getId());

        Optional<AdCollection> responseAdCollection = this.adCollectionRepository.findById(adCollection.getId());
        Optional<AdContent> responseAdContent = this.adContentRepository.findById(adContent.getId());
        assertTrue(responseAdCollection.isPresent());
        assertTrue(responseAdContent.isPresent());
        assertAdCollection(this.adCollectionMapper.toAdCollectionResponseDto(responseAdCollection.get()) , "New Collection", "http://localhost/collection");
        assertAdContent(this.adContentMapper.toAdContentResponseDto(responseAdContent.get()), "New Content", "http://localhost/content", AdContentTypeEnum.IMAGE);

    }

    @Test
    void shouldThrowWhenDeletingNonExistentModule() {
        assertThrows(ModuleNotFoundException.class, () -> this.moduleService.deleteModuleById(1L));
    }

    @Test
    void shouldDeleteAllModules() {

        ModuleResponseDto created1 = this.moduleService.createModule(this.buildModuleRequestDto("New Module", "{}", ModuleTypeEnum.WEATHER, null));
        ModuleResponseDto created2 = this.moduleService.createModule(this.buildModuleRequestDto("New Module", "{}", ModuleTypeEnum.WEATHER, null));
        ModuleResponseDto created3 = this.moduleService.createModule(this.buildModuleRequestDto("New Module", "{}", ModuleTypeEnum.WEATHER, null));

        assertEquals(3, this.moduleService.getAllModules().size());
        this.moduleService.deleteAllModules();
        assertEquals(0, this.moduleService.getAllModules().size());
    }


}
