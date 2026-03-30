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
import com.a6dig.digitalsignage.exception.AdContentNotFoundException;
import com.a6dig.digitalsignage.exception.ModuleNotFoundException;
import com.a6dig.digitalsignage.mapper.AdCollectionMapper;
import com.a6dig.digitalsignage.mapper.AdContentMapper;
import com.a6dig.digitalsignage.repository.AdCollectionRepository;
import com.a6dig.digitalsignage.repository.AdContentRepository;
import com.a6dig.digitalsignage.repository.DomainRepository;
import com.a6dig.digitalsignage.repository.ModuleRepository;
import com.a6dig.digitalsignage.service.AdCollectionService;
import com.a6dig.digitalsignage.service.AdContentService;
import com.a6dig.digitalsignage.service.ModuleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
public class AdCollectionServiceImplTest {


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
    void shouldGetAllAdCollections() {

        AdCollectionRequestDto dto1 = this.buildAdCollectionRequestDto("Default Ad Collection 1", "http://localhost:3000/collection",  null);
        AdCollectionRequestDto dto2 = this.buildAdCollectionRequestDto("Default Ad Collection 2", "http://localhost:3000/collection",  null);

        AdCollectionResponseDto created1 = this.adCollectionService.createAdCollection(dto1);
        AdCollectionResponseDto created2 = this.adCollectionService.createAdCollection(dto2);

        List<AdCollectionResponseDto> response = new ArrayList<>(this.adCollectionService.getAllAdCollections());
        response.sort(Comparator.comparing(AdCollectionResponseDto::getName));

        assertAdCollection(response.get(0), "Default Ad Collection 1", "http://localhost:3000/collection");
        assertAdCollection(response.get(1), "Default Ad Collection 2", "http://localhost:3000/collection");


    }



    @Test
    void shouldReturnEmptyListWhenNoAdCollections() {
        List<AdCollectionResponseDto> response = this.adCollectionService.getAllAdCollections();
        assertTrue(response.isEmpty());
    }


    @Test
    void shouldGetAdCollectionById() {
        AdCollectionResponseDto created = this.adCollectionService.createAdCollection(
                this.buildAdCollectionRequestDto("Default Ad Collection", "http://localhost:3000/collection", null)
        );
        AdCollectionResponseDto response = this.adCollectionService.getAdCollectionById(created.getId());
        assertAdCollection(response, "Default Ad Collection", "http://localhost:3000/collection");
    }



    @Test
    void shouldGetAdCollectionByIdWithAdContent() {

        AdContent adContent = this.adContentRepository.save(this.buildContent(null, "New Content", "http://localhost/content", this.domainCache.buildDomain(AdContentTypeEnum.IMAGE)));
        AdCollection created = this.adCollectionRepository.save(this.buildAdCollection(null, "New Collection", "http://localhost/collection", Set.of(adContent)));


        AdCollectionResponseDto response = this.adCollectionService.getAdCollectionById(created.getId());

        assertAdCollection(response, "New Collection", "http://localhost/collection");
        assertAdContent(response.getAdContents().get(0), "New Content", "http://localhost/content", AdContentTypeEnum.IMAGE);
    }


    @Test
    void shouldThrowWhenAdCollectionNotFoundById() {
        assertThrows(AdCollectionNotFoundException.class, () -> this.adCollectionService.getAdCollectionById(1L));
    }


    // create




    @Test
    void shouldCreateAdCollection() {
        AdCollectionResponseDto created = this.adCollectionService.createAdCollection(
                this.buildAdCollectionRequestDto("Default Ad Collection", "http://localhost:3000/collection", null)
        );
        AdCollectionResponseDto response = this.adCollectionService.getAdCollectionById(created.getId());
        assertAdCollection(response, "Default Ad Collection", "http://localhost:3000/collection");
    }


    @Test
    void shouldCreateAdCollectionWithExistingAdContent() {

        AdContent adContent = this.adContentRepository.save(this.buildContent(null, "New Content", "http://localhost/content", this.domainCache.buildDomain(AdContentTypeEnum.IMAGE)));

        AdContentRequestUpdateDto adContentRequestUpdateDto = this.buildAdContentRequestUpdateDto(
                adContent.getId(), adContent.getName(), adContent.getUrl(), AdContentTypeEnum.IMAGE
        );

        AdCollectionResponseDto created = this.adCollectionService.createAdCollection(
                this.buildAdCollectionRequestDto("Default Ad Collection", "http://localhost:3000/collection", List.of(adContentRequestUpdateDto))
        );
        AdCollectionResponseDto response = this.adCollectionService.getAdCollectionById(created.getId());
        assertAdCollection(response, "Default Ad Collection", "http://localhost:3000/collection");
        assertAdContent(response.getAdContents().get(0), "New Content", "http://localhost/content", AdContentTypeEnum.IMAGE);
    }

    @Test
    void shouldThrowWhenCreateAdCollectionWithNewAdContent() {


        AdContent adContent = this.adContentRepository.save(this.buildContent(null, "New Content", "http://localhost/content", this.domainCache.buildDomain(AdContentTypeEnum.IMAGE)));

        AdContentRequestUpdateDto adContentRequestUpdateDto = this.buildAdContentRequestUpdateDto(
                null, adContent.getName(), adContent.getUrl(), AdContentTypeEnum.IMAGE
        );

        AdCollectionRequestDto request = this.buildAdCollectionRequestDto("Default Ad Collection", "http://localhost:3000/collection", List.of(adContentRequestUpdateDto));

        assertThrows(AdContentNotFoundException.class, () -> this.adCollectionService.createAdCollection(request));

    }

    // update

    @Test
    void shouldUpdateAdCollection() {

        AdContent adContent = this.adContentRepository.save(this.buildContent(null, "New Content", "http://localhost/content", this.domainCache.buildDomain(AdContentTypeEnum.IMAGE)));
        AdCollection existing = this.adCollectionRepository.save(this.buildAdCollection(null, "New Collection", "http://localhost/collection", Set.of(adContent)));

        AdContentRequestUpdateDto adContentRequestUpdateDto =
                this.buildAdContentRequestUpdateDto(adContent.getId(), "New Content", "http://localhost/content", AdContentTypeEnum.IMAGE);

        AdCollectionRequestUpdateDto updated =
                this.buildAdCollectionRequestUpdateDto(existing.getId(), "Updated Collection", "http://localhost/collection", List.of(adContentRequestUpdateDto));

        AdCollectionResponseDto response = this.adCollectionService.updateAdCollectionById(existing.getId(), updated);

        assertAdCollection(response, "Updated Collection", "http://localhost/collection");
        assertAdContent(response.getAdContents().get(0), "New Content", "http://localhost/content", AdContentTypeEnum.IMAGE);

    }



    @Test
    void shouldThrowWhenUpdateNonExistentAdCollection() {

        AdContent adContent = this.adContentRepository.save(this.buildContent(null, "New Content", "http://localhost/content", this.domainCache.buildDomain(AdContentTypeEnum.IMAGE)));
        AdCollection existing = this.adCollectionRepository.save(this.buildAdCollection(null, "New Collection", "http://localhost/collection", Set.of(adContent)));

        AdContentRequestUpdateDto adContentRequestUpdateDto =
                this.buildAdContentRequestUpdateDto(adContent.getId(), "New Content", "http://localhost/content", AdContentTypeEnum.IMAGE);

        AdCollectionRequestUpdateDto updated =
                this.buildAdCollectionRequestUpdateDto(2L, "Updated Collection", "http://localhost/collection", List.of(adContentRequestUpdateDto));


        assertThrows(AdCollectionNotFoundException.class, () -> this.adCollectionService.updateAdCollectionById(2L, updated));
    }



    @Test
    void shouldThrowWhenUpdateWithNewAdContent() {

        AdContent adContent = this.adContentRepository.save(this.buildContent(null, "New Content", "http://localhost/content", this.domainCache.buildDomain(AdContentTypeEnum.IMAGE)));
        AdCollection existing = this.adCollectionRepository.save(this.buildAdCollection(null, "New Collection", "http://localhost/collection", Set.of(adContent)));

        AdContentRequestUpdateDto adContentRequestUpdateDto =
                this.buildAdContentRequestUpdateDto(null, "New Content", "http://localhost/content", AdContentTypeEnum.IMAGE);

        AdCollectionRequestUpdateDto updated =
                this.buildAdCollectionRequestUpdateDto(existing.getId(), "Updated Collection", "http://localhost/collection", List.of(adContentRequestUpdateDto));


        assertThrows(AdContentNotFoundException.class, () -> this.adCollectionService.updateAdCollectionById(existing.getId(), updated));
    }



    @Test
    void shouldThrowWhenUpdateWithNonExistentAdContent() {

        AdContent adContent = this.adContentRepository.save(this.buildContent(null, "New Content", "http://localhost/content", this.domainCache.buildDomain(AdContentTypeEnum.IMAGE)));
        AdCollection existing = this.adCollectionRepository.save(this.buildAdCollection(null, "New Collection", "http://localhost/collection", Set.of(adContent)));

        AdContentRequestUpdateDto adContentRequestUpdateDto =
                this.buildAdContentRequestUpdateDto(5L, "New Content", "http://localhost/content", AdContentTypeEnum.IMAGE);

        AdCollectionRequestUpdateDto updated =
                this.buildAdCollectionRequestUpdateDto(existing.getId(), "Updated Collection", "http://localhost/collection", List.of(adContentRequestUpdateDto));


        assertThrows(AdContentNotFoundException.class, () -> this.adCollectionService.updateAdCollectionById(existing.getId(), updated));
    }




    // delete

    @Test
    void shouldDeleteAdCollectionById() {
        AdCollectionResponseDto created = this.adCollectionService.createAdCollection(this.buildAdCollectionRequestDto("Default Ad Collection", "http://localhost:3000/collection", null));
        this.adCollectionService.deleteAdCollectionById(created.getId());
        assertFalse(this.adCollectionRepository.existsById(created.getId()));

    }

    @Test
    @Transactional
    void shouldDeleteAdCollectionButNotAdContent() {
        AdContent adContent = this.adContentRepository.save(this.buildContent(null, "New Content", "http://localhost/content", this.domainCache.buildDomain(AdContentTypeEnum.IMAGE)));

        AdCollection adCollection = this.adCollectionRepository.save(this.buildAdCollection(null, "New Collection", "http://localhost/collection", Set.of(adContent)));

        this.adCollectionService.deleteAdCollectionById(adCollection.getId());

        Optional<AdCollection> responseAdCollection = this.adCollectionRepository.findById(adCollection.getId());
        Optional<AdContent> responseAdContent = this.adContentRepository.findById(adContent.getId());
        assertFalse(responseAdCollection.isPresent());
        assertTrue(responseAdContent.isPresent());
        assertAdContent(AdContentMapper.toAdContentResponseDto(responseAdContent.get()), "New Content", "http://localhost/content", AdContentTypeEnum.IMAGE);

    }

    @Test
    void shouldThrowWhenDeletingNonExistentAdCollection() {
        assertThrows(AdCollectionNotFoundException.class, () -> this.adCollectionService.deleteAdCollectionById(1L));
    }

    @Test
    void shouldDeleteAllAdCollection() {

        this.adCollectionService.createAdCollection(this.buildAdCollectionRequestDto("Default Ad Collection", "http://localhost:3000/collection", null));
        this.adCollectionService.createAdCollection(this.buildAdCollectionRequestDto("Default Ad Collection", "http://localhost:3000/collection", null));
        this.adCollectionService.createAdCollection(this.buildAdCollectionRequestDto("Default Ad Collection", "http://localhost:3000/collection", null));
        this.adCollectionService.createAdCollection(this.buildAdCollectionRequestDto("Default Ad Collection", "http://localhost:3000/collection", null));

        assertEquals(4, this.adCollectionService.getAllAdCollections().size());
        this.adCollectionService.deleteAllAdCollections();
        assertEquals(0, this.adCollectionService.getAllAdCollections().size());
    }


}
