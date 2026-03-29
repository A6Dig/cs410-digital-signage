package com.a6dig.digitalsignage.integration.repository;

import com.a6dig.digitalsignage.config.DatabaseInit;
import com.a6dig.digitalsignage.config.DomainCache;
import com.a6dig.digitalsignage.constant.AdContentTypeEnum;
import com.a6dig.digitalsignage.constant.AppConstant;
import com.a6dig.digitalsignage.constant.ModuleTypeEnum;
import com.a6dig.digitalsignage.entity.*;
import com.a6dig.digitalsignage.entity.Module;
import com.a6dig.digitalsignage.repository.AdCollectionRepository;
import com.a6dig.digitalsignage.repository.AdContentRepository;
import com.a6dig.digitalsignage.repository.DomainRepository;
import com.a6dig.digitalsignage.repository.ModuleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;


import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

@DataJpaTest
@Import({DomainCache.class, DatabaseInit.class})
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
public class AdContentRepositoryTest {

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private AdCollectionRepository adCollectionRepository;

    @Autowired
    private AdContentRepository adContentRepository;

    @Autowired
    private DomainCache domainCache;


    @BeforeEach
    private void setUp() {
        this.domainCache.refresh(AppConstant.SystemConstant.DOMAIN_TYPE_MODULE);
        this.domainCache.refresh(AppConstant.SystemConstant.DOMAIN_TYPE_AD_CONTENT);
    }

    private Module buildModule(String name, String config, ModuleTypeEnum type, AdCollection adCollection) {
        Module module = new Module();
        module.setDomain(domainCache.buildDomain(type));
        module.setAdCollection(adCollection);
        module.setName(name);
        module.setConfig(config);
        return module;
    }

    private AdCollection buildAdCollection(String name, String url, Set<AdContent> adContents) {
        AdCollection adCollection = new AdCollection();
        adCollection.setName(name);
        adCollection.setUrl(url);
        adCollection.setAdContents(adContents);
        return adCollection;
    }

    private AdContent buildAdContent(String name, String url, Domain domain) {
        AdContent adContent = new AdContent();
        adContent.setName(name);
        adContent.setUrl(url);
        adContent.setDomain(domain);
        return adContent;
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
            Module module,
            String expectedName,
            ModuleTypeEnum moduleTypeEnum,
            String expectedConfig) {
        assertNotNull(module.getId());
        assertNotNull(module.getName());
        assertNotNull(module.getCreatedAt());
        assertNotNull(module.getUpdatedAt());
        assertTrue(moduleRepository.existsById(module.getId()));
        assertEquals(module.getName(), expectedName);
        assertEquals(module.getConfig(), expectedConfig);

        assertDomain(module.getDomain(), this.domainCache.buildDomain(moduleTypeEnum));
    }

    private void assertAdCollection(
            AdCollection adCollection,
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
            AdContent adContent,
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

    // Get
    @Test
    void shouldFindById() {
        AdContent adContent = this.buildAdContent("Default Ad Content", "http://localhost:3000/image1", this.domainCache.buildDomain(AdContentTypeEnum.IMAGE));
        AdContent created = this.adContentRepository.save(adContent);
        Optional<AdContent> response = this.adContentRepository.findById(created.getId());
        assertTrue(response.isPresent());
        assertAdContent(response.get(), "Default Ad Content", "http://localhost:3000/image1");
        assertDomain(response.get().getDomain(), this.domainCache.buildDomain(AdContentTypeEnum.IMAGE));
    }


    @Test
    void shouldFindAll() {
        AdContent adContent1 = this.buildAdContent("Default Ad Content 1", "http://localhost:3000/image1", this.domainCache.buildDomain(AdContentTypeEnum.IMAGE));
        AdContent created1 = this.adContentRepository.save(adContent1);
        AdContent adContent2 = this.buildAdContent("Default Ad Content 2", "http://localhost:3000/video1", this.domainCache.buildDomain(AdContentTypeEnum.VIDEO));
        AdContent created2 = this.adContentRepository.save(adContent2);

        List<AdContent> adContents = this.adContentRepository.findAll();
        adContents.sort(Comparator.comparing(AdContent::getName));

        AdContent response1 = adContents.get(0);
        AdContent response2 = adContents.get(1);

        assertEquals(2, adContents.size());

        assertAdContent(response1, "Default Ad Content 1", "http://localhost:3000/image1");
        assertDomain(response1.getDomain(), this.domainCache.buildDomain(AdContentTypeEnum.IMAGE));

        assertAdContent(response2, "Default Ad Content 2", "http://localhost:3000/video1");
        assertDomain(response2.getDomain(), this.domainCache.buildDomain(AdContentTypeEnum.VIDEO));
    }


    @Test
    @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldReturnEmptyWhenNotFound() {
        Optional<AdContent> adContent = this.adContentRepository.findById(1L);
        assertFalse(adContent.isPresent());
    }

    @Test
    @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldReturnEmptyListWhenNotFound() {
        List<AdContent> adContents = this.adContentRepository.findAll();
        assertEquals(0, adContents.size());
    }

    // Post
    @Test
    void shouldSave() {
        AdContent adContent = this.buildAdContent("Default Ad Content", "http://localhost:3000/image1", this.domainCache.buildDomain(AdContentTypeEnum.IMAGE));
        AdContent created = this.adContentRepository.save(adContent);
        assertAdContent(created, "Default Ad Content", "http://localhost:3000/image1");
        assertDomain(created.getDomain(), this.domainCache.buildDomain(AdContentTypeEnum.IMAGE));
    }

    // Update
    @Test
    void shouldUpdate() {
        AdContent adContent = this.buildAdContent("Default Ad Content", "http://localhost:3000/image1", this.domainCache.buildDomain(AdContentTypeEnum.IMAGE));
        AdContent created = this.adContentRepository.save(adContent);

        created.setUrl("http://localhost:3000/video1");
        created.setDomain(this.domainCache.buildDomain(AdContentTypeEnum.VIDEO));

        AdContent response = this.adContentRepository.save(created);

        List<AdContent> adContentList = this.adContentRepository.findAll();

        assertEquals(1, adContentList.size());
        assertAdContent(adContentList.get(0), "Default Ad Content", "http://localhost:3000/video1");
        assertDomain(adContentList.get(0).getDomain(), this.domainCache.buildDomain(AdContentTypeEnum.VIDEO));
    }

    // delete
    @Test
    void shouldDeleteById() {
        AdContent adContent1 = this.buildAdContent("Default Ad Content 1", "http://localhost:3000/image1", this.domainCache.buildDomain(AdContentTypeEnum.IMAGE));
        AdContent created1 = this.adContentRepository.save(adContent1);
        AdContent adContent2 = this.buildAdContent("Default Ad Content 2", "http://localhost:3000/video1", this.domainCache.buildDomain(AdContentTypeEnum.VIDEO));
        AdContent created2 = this.adContentRepository.save(adContent2);

        this.adContentRepository.deleteById(created1.getId());

        List<AdContent> adContents = this.adContentRepository.findAll();

        Optional<AdContent> adContent = this.adContentRepository.findById(created2.getId());
        assertEquals(1, adContents.size());
        assertTrue(adContent.isPresent());
        assertAdContent(adContent.get(), "Default Ad Content 2", "http://localhost:3000/video1");
        assertDomain(adContent.get().getDomain(), this.domainCache.buildDomain(AdContentTypeEnum.VIDEO));


    }

    @Test
    void shouldDeleteAll() {
        AdContent adContent1 = this.buildAdContent("Default Ad Content 1", "http://localhost:3000/image1", this.domainCache.buildDomain(AdContentTypeEnum.IMAGE));
        AdContent created1 = this.adContentRepository.save(adContent1);
        AdContent adContent2 = this.buildAdContent("Default Ad Content 2", "http://localhost:3000/video1", this.domainCache.buildDomain(AdContentTypeEnum.VIDEO));
        AdContent created2 = this.adContentRepository.save(adContent2);

        this.adContentRepository.deleteAll();

        List<AdContent> adContents = this.adContentRepository.findAll();

        assertEquals(0, adContents.size());
    }

    @Test
    void shouldDeleteWithoutDeletingParent() {


        Set<AdContent> adContents1 = new HashSet<>();
        adContents1.add(this.buildAdContent("Default Ad Content", "http://localhost:3000/image1", this.domainCache.buildDomain(AdContentTypeEnum.IMAGE)));

        AdCollection adCollection1 = this.buildAdCollection("Default Ad Collection 1", "", adContents1);
        AdCollection created1 = this.adCollectionRepository.save(adCollection1);


        Set<AdContent> adContents2 = new HashSet<>();
        adContents2.add(this.buildAdContent("Default Ad Content", "http://localhost:3000/image1", this.domainCache.buildDomain(AdContentTypeEnum.IMAGE)));


        AdCollection adCollection2 = this.buildAdCollection("Default Ad Collection 2", "", adContents2);
        AdCollection created2 = this.adCollectionRepository.save(adCollection2);



        created1.getAdContents().clear();
        created2.getAdContents().clear();
        this.adContentRepository.flush();

        this.adContentRepository.deleteAll();



        List<AdCollection> adCollections = this.adCollectionRepository.findAll();
        adCollections.sort(Comparator.comparing(AdCollection::getName));


        assertEquals(0, this.adContentRepository.findAll().size());
        assertEquals(2, adCollections.size());
        assertAdCollection(adCollections.get(0), "Default Ad Collection 1", "");
        assertAdCollection(adCollections.get(1), "Default Ad Collection 2", "");
    }




    @Test
    void shouldDeleteWithoutDeletingParentAndModule() {
        String config = """
                {
                    "name": "Some Content",
                    "contents": [
                        {"name": "Content 1", "url": "https://localhost:3000/content1", "duration": 12}
                        {"name": "Content 1", "url": "https://localhost:3000/content1", "duration": 12},
                        {"name": "Content 1", "url": "https://localhost:3000/content1", "duration": 12},
                        {"name": "Content 1", "url": "https://localhost:3000/content1", "duration": 12},
                    ]
                }
                
                """;


        Module module = this.buildModule("Default Module", config, ModuleTypeEnum.ROTATING_AD, null);
        Module moduleCreated = this.moduleRepository.save(module);


        Set<AdContent> adContents1 = new HashSet<>();
        adContents1.add(this.buildAdContent("Default Ad Content", "http://localhost:3000/image1", this.domainCache.buildDomain(AdContentTypeEnum.IMAGE)));

        AdCollection adCollection1 = this.buildAdCollection("Default Ad Collection 1", "", adContents1);
        AdCollection created1 = this.adCollectionRepository.save(adCollection1);


        Set<AdContent> adContents2 = new HashSet<>();
        adContents2.add(this.buildAdContent("Default Ad Content", "http://localhost:3000/image1", this.domainCache.buildDomain(AdContentTypeEnum.IMAGE)));


        AdCollection adCollection2 = this.buildAdCollection("Default Ad Collection 2", "", adContents2);
        AdCollection created2 = this.adCollectionRepository.save(adCollection2);

        moduleCreated.setAdCollection(adCollection1);
        this.moduleRepository.save(moduleCreated);

        created1.getAdContents().clear();
        created2.getAdContents().clear();
        this.adContentRepository.flush();

        this.adContentRepository.deleteAll();



        List<Module> modules = this.moduleRepository.findAll();
        List<AdCollection> adCollections = this.adCollectionRepository.findAll();
        adCollections.sort(Comparator.comparing(AdCollection::getName));


        assertEquals(0, this.adContentRepository.findAll().size());
        assertEquals(1, modules.size());
        assertEquals(2, adCollections.size());
        assertAdCollection(adCollections.get(0), "Default Ad Collection 1", "");
        assertAdCollection(adCollections.get(1), "Default Ad Collection 2", "");
        assertModule(modules.get(0), "Default Module", ModuleTypeEnum.ROTATING_AD, config);
    }

}
