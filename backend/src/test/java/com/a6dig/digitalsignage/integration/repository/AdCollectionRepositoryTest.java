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
public class AdCollectionRepositoryTest {

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
        Set<AdContent> adContents = new HashSet<>();
        AdContent savedAdContent = this.adContentRepository.save(this.buildAdContent("Default Ad Content", "http://localhost:3000/image1", this.domainCache.buildDomain(AdContentTypeEnum.IMAGE)));
        adContents.add(savedAdContent);
        AdCollection adCollection = this.buildAdCollection("Default Ad Collection", "", adContents);
        AdCollection created = this.adCollectionRepository.save(adCollection);
        Optional<AdCollection> response = this.adCollectionRepository.findById(created.getId());
        assertTrue(response.isPresent());
        assertAdCollection(response.get(), "Default Ad Collection", "");
        assertAdContent(response.get().getAdContents().stream().toList().get(0), "Default Ad Content", "http://localhost:3000/image1");
        assertDomain(response.get().getAdContents().stream().toList().get(0).getDomain(), this.domainCache.buildDomain(AdContentTypeEnum.IMAGE));
    }


    @Test
    void shouldFindAll() {
        Set<AdContent> adContents1 = new HashSet<>();
        AdContent savedAdContent1 = this.adContentRepository.save(this.buildAdContent("Default Ad Content", "http://localhost:3000/image1", this.domainCache.buildDomain(AdContentTypeEnum.IMAGE)));
        adContents1.add(savedAdContent1);

        AdCollection adCollection1 = this.buildAdCollection("Default Ad Collection 1", "", adContents1);
        AdCollection created1 = this.adCollectionRepository.save(adCollection1);


        Set<AdContent> adContents2 = new HashSet<>();
        AdContent savedAdContent2 = this.adContentRepository.save(this.buildAdContent("Default Ad Content", "http://localhost:3000/image1", this.domainCache.buildDomain(AdContentTypeEnum.IMAGE)));
        adContents2.add(savedAdContent2);


        AdCollection adCollection2 = this.buildAdCollection("Default Ad Collection 2", "", adContents2);
        AdCollection created2 = this.adCollectionRepository.save(adCollection2);



        List<AdCollection> response = this.adCollectionRepository.findAll();
        response.sort(Comparator.comparing(AdCollection::getName));

        AdCollection response1 = response.get(0);
        AdCollection response2 = response.get(1);

        assertEquals(2, response.size());

        assertAdCollection(response1, "Default Ad Collection 1", "");
        assertAdContent(response1.getAdContents().stream().toList().get(0), "Default Ad Content", "http://localhost:3000/image1");
        assertDomain(response1.getAdContents().stream().toList().get(0).getDomain(), this.domainCache.buildDomain(AdContentTypeEnum.IMAGE));

        assertAdCollection(response2, "Default Ad Collection 2", "");
        assertAdContent(response2.getAdContents().stream().toList().get(0), "Default Ad Content", "http://localhost:3000/image1");
        assertDomain(response2.getAdContents().stream().toList().get(0).getDomain(), this.domainCache.buildDomain(AdContentTypeEnum.IMAGE));
    }


    @Test
    @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldReturnEmptyWhenNotFound() {
        Optional<AdCollection> adCollection = this.adCollectionRepository.findById(1L);
        assertFalse(adCollection.isPresent());
    }

    @Test
    @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldReturnEmptyListWhenNotFound() {
        List<AdCollection> adCollection = this.adCollectionRepository.findAll();
        assertEquals(0, adCollection.size());
    }

    // Post
    @Test
    void shouldSave() {
        AdCollection adCollection = this.buildAdCollection("Default Ad Collection", "", null);
        AdCollection created = this.adCollectionRepository.save(adCollection);
        assertAdCollection(created, "Default Ad Collection", "");
        assertNull(created.getAdContents());
    }

//    @Test
//    void shouldSaveWithPersistingAdContent() {
//
//        Set<AdContent> adContents = new HashSet<>();
//        adContents.add(this.buildAdContent("Default Ad Content", "http://localhost:3000/image1", this.domainCache.buildDomain(AdContentTypeEnum.IMAGE)));
//        AdCollection adCollection = this.buildAdCollection("Default Ad Collection", "", adContents);
//        AdCollection created = this.adCollectionRepository.save(adCollection);
//
//        assertAdCollection(created, "Default Ad Collection", "");
//        assertAdContent(created.getAdContents().stream().toList().get(0), "Default Ad Content", "http://localhost:3000/image1");
//    }

    // Update
    @Test
    void shouldUpdate() {

        Set<AdContent> adContents = new HashSet<>();
        AdContent savedAdContent = this.adContentRepository.save(this.buildAdContent("Default Ad Content", "http://localhost:3000/image1", this.domainCache.buildDomain(AdContentTypeEnum.IMAGE)));
        adContents.add(savedAdContent);
        AdCollection adCollection = this.buildAdCollection("Default Ad Collection", "", adContents);
        AdCollection created = this.adCollectionRepository.save(adCollection);

        created.setName("Updated Ad Content");

        List<AdCollection> adCollections = this.adCollectionRepository.findAll();

        assertEquals(1, adCollections.size());
        assertAdCollection(adCollections.get(0), "Updated Ad Content", "");
        assertAdContent(adCollections.get(0).getAdContents().stream().toList().get(0), "Default Ad Content", "http://localhost:3000/image1");

    }



    @Test
    void shouldUpdateWithPersistingAdContent() {


        AdCollection adCollection = this.buildAdCollection("Default Ad Collection", "", null);
        AdCollection created = this.adCollectionRepository.save(adCollection);


        AdContent adContent = this.buildAdContent("Default Ad Content", "http://localhost:3000/image1", this.domainCache.buildDomain(AdContentTypeEnum.IMAGE));
        AdContent savedAdContent = this.adContentRepository.save(adContent);
        Set<AdContent> adContents = new HashSet<>();
        adContents.add(savedAdContent);


        created.setName("Updated Ad Content");
        created.setAdContents(adContents);

        AdCollection response = this.adCollectionRepository.save(created);

        List<AdCollection> adCollections = this.adCollectionRepository.findAll();
        List<AdContent> adContentList = this.adContentRepository.findAll();

        assertEquals(1, adContentList.size());
        assertEquals(1, adCollections.size());
        assertAdCollection(adCollections.get(0), "Updated Ad Content", "");
        assertAdContent(adCollections.get(0).getAdContents().stream().toList().get(0), "Default Ad Content", "http://localhost:3000/image1");

    }

    // delete
    @Test
    void shouldDeleteByIdAndNotDeleteChildren() {


        Set<AdContent> adContents = new HashSet<>();
        AdContent savedAdContent = this.adContentRepository.save(this.buildAdContent("Default Ad Content", "http://localhost:3000/image1", this.domainCache.buildDomain(AdContentTypeEnum.IMAGE)));
        adContents.add(savedAdContent);
        AdCollection adCollection = this.buildAdCollection("Default Ad Collection", "", adContents);
        AdCollection created = this.adCollectionRepository.save(adCollection);

        this.adCollectionRepository.deleteById(created.getId());


        List<AdCollection> adCollections = this.adCollectionRepository.findAll();
        List<AdContent> adContentList = this.adContentRepository.findAll();
        assertEquals(0, adCollections.size());
        assertEquals(1, adContentList.size());
        assertAdContent(adContentList.get(0), "Default Ad Content", "http://localhost:3000/image1");
    }

    @Test
    void shouldDeleteAllAndNotDeleteChildren() {
        AdContent adContent = this.buildAdContent("Default Ad Content", "http://localhost:3000/image1", this.domainCache.buildDomain(AdContentTypeEnum.IMAGE));

        AdContent savedAdContent = this.adContentRepository.save(adContent);

        Set<AdContent> adContents1 = new HashSet<>();
        adContents1.add(savedAdContent);


        AdCollection adCollection1 = this.buildAdCollection("Default Ad Collection 1", "", adContents1);
        AdCollection created1 = this.adCollectionRepository.save(adCollection1);


        Set<AdContent> adContents2 = new HashSet<>();
        adContents2.add(savedAdContent);


        AdCollection adCollection2 = this.buildAdCollection("Default Ad Collection 2", "", adContents2);
        AdCollection created2 = this.adCollectionRepository.save(adCollection2);

        created1.getAdContents().clear();
        created2.getAdContents().clear();
        this.adCollectionRepository.flush();
        this.adCollectionRepository.deleteAll();


        List<AdCollection> adCollections = this.adCollectionRepository.findAll();
        List<AdContent> adContentList = this.adContentRepository.findAll();
        assertEquals(0, adCollections.size());
        assertEquals(1, adContentList.size());
        assertAdContent(adContentList.get(0), "Default Ad Content", "http://localhost:3000/image1");
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
        AdContent savedAdContent1 = this.adContentRepository.save(this.buildAdContent("Default Ad Content 1", "http://localhost:3000/image1", this.domainCache.buildDomain(AdContentTypeEnum.IMAGE)));
        adContents1.add(savedAdContent1);

        AdCollection adCollection1 = this.buildAdCollection("Default Ad Collection 1", "", adContents1);
        AdCollection created1 = this.adCollectionRepository.save(adCollection1);


        Set<AdContent> adContents2 = new HashSet<>();
        AdContent savedAdContent2 = this.adContentRepository.save(this.buildAdContent("Default Ad Content 2", "http://localhost:3000/image1", this.domainCache.buildDomain(AdContentTypeEnum.IMAGE)));
        adContents2.add(savedAdContent2);


        AdCollection adCollection2 = this.buildAdCollection("Default Ad Collection 2", "", adContents2);
        AdCollection created2 = this.adCollectionRepository.save(adCollection2);

        moduleCreated.setAdCollection(adCollection1);
        this.moduleRepository.save(moduleCreated);

        moduleCreated.setAdCollection(null);
        this.moduleRepository.save(moduleCreated);
        this.moduleRepository.flush();

        this.adCollectionRepository.findAll().forEach(adCollection -> adCollection.getAdContents().clear());
        this.adCollectionRepository.flush();

        this.adCollectionRepository.deleteAll();
        this.adCollectionRepository.flush();



        List<Module> modules = this.moduleRepository.findAll();
        List<AdContent> adContents = this.adContentRepository.findAll();
        adContents.sort(Comparator.comparing(AdContent::getName));


        assertEquals(2, adContents.size());
        assertEquals(1, modules.size());
        assertEquals(0, this.adCollectionRepository.findAll().size());
        assertAdContent(adContents.get(0), "Default Ad Content 1", "http://localhost:3000/image1");
        assertDomain(adContents.get(0).getDomain(), this.domainCache.buildDomain(AdContentTypeEnum.IMAGE));
        assertAdContent(adContents.get(1), "Default Ad Content 2", "http://localhost:3000/image1");
        assertDomain(adContents.get(1).getDomain(), this.domainCache.buildDomain(AdContentTypeEnum.IMAGE));
        assertModule(modules.get(0), "Default Module", ModuleTypeEnum.ROTATING_AD, config);
    }

}
