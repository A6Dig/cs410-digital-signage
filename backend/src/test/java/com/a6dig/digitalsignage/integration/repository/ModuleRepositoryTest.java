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
import org.testcontainers.shaded.org.bouncycastle.math.raw.Mod;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

@DataJpaTest
@Import({DomainCache.class, DatabaseInit.class})
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
public class ModuleRepositoryTest {
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


    // GET
    @Test
    void shouldFindById() {
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
        Module created = this.moduleRepository.save(module);
        Optional<Module> response = this.moduleRepository.findById(created.getId());
        assertTrue(response.isPresent());
        assertModule(response.get(), "Default Module", ModuleTypeEnum.ROTATING_AD, config);
    }


    @Test
    void shouldFindAllModules() {
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

        Module module1 = this.buildModule("Default Module 1", config, ModuleTypeEnum.ROTATING_AD, null);
        Module created1 = this.moduleRepository.save(module1);


        Set<AdContent> adContents = new HashSet<>();
        AdCollection adCollection = this.buildAdCollection("Default Ad Collection", "", adContents);
        AdCollection savedAdCollection = this.adCollectionRepository.save(adCollection);
        Module module2 = this.buildModule("Default Module 2", null, ModuleTypeEnum.WEATHER, savedAdCollection);
        Module created2 = this.moduleRepository.save(module2);

        List<Module> response = this.moduleRepository.findAll();
        response.sort(Comparator.comparing(Module::getName));


        assertEquals(2, response.size());
        assertModule(response.get(0), "Default Module 1", ModuleTypeEnum.ROTATING_AD, config);
        assertModule(response.get(1), "Default Module 2", ModuleTypeEnum.WEATHER, null);
        assertAdCollection(response.get(1).getAdCollection(), "Default Ad Collection", "");
    }

    @Test
    @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldReturnEmptyWhenNotFound() {
        Optional<Module> module = this.moduleRepository.findById(1L);
        assertFalse(module.isPresent());
    }

    @Test
    @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldReturnEmptyListWhenNotFound() {
        List<Module> modules = this.moduleRepository.findAll();
        assertEquals(0, modules.size());
    }


    // post

    @Test
    void shouldSave() {

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
        Module created = this.moduleRepository.save(module);
        assertModule(created, "Default Module", ModuleTypeEnum.ROTATING_AD, config);
    }



//    @Test
//    void shouldSaveWithPersistingAdCollection() {
//        Set<AdContent> adContents = new HashSet<>();
//        AdCollection adCollection = this.buildAdCollection("Default Ad Collection", "", adContents);
//        Module module = this.buildModule("Default Module 2", null, ModuleTypeEnum.WEATHER, adCollection);
//        Module created = this.moduleRepository.save(module);
//
//
//        assertModule(created, "Default Module 2", ModuleTypeEnum.WEATHER, null);
//        assertAdCollection(created.getAdCollection(), "Default Ad Collection", "");
//    }





//    @Test
//    void shouldSaveWithPersistingAdCollectionAndAdContent() {
//        Set<AdContent> adContents = new HashSet<>();
//        adContents.add(this.buildAdContent("Default Ad Content", "http://localhost:3000/image1", this.domainCache.buildDomain(AdContentTypeEnum.IMAGE)));
//        AdCollection adCollection = this.buildAdCollection("Default Ad Collection", "", adContents);
//        Module module = this.buildModule("Default Module 2", null, ModuleTypeEnum.WEATHER, adCollection);
//        Module created = this.moduleRepository.save(module);
//
//
//        assertModule(created, "Default Module 2", ModuleTypeEnum.WEATHER, null);
//        assertAdCollection(created.getAdCollection(), "Default Ad Collection", "");
//        assertAdContent(created.getAdCollection().getAdContents().stream().toList().get(0), "Default Ad Content", "http://localhost:3000/image1");
//    }


    // update


    @Test
    void shouldUpdate() {

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
        Module created = this.moduleRepository.save(module);

        created.setName("Updated Name");

        this.moduleRepository.save(created);


        List<Module> modules = this.moduleRepository.findAll();
        assertEquals(1, modules.size());
        assertModule(modules.get(0), "Updated Name", ModuleTypeEnum.ROTATING_AD, config);
    }


    @Test
    void shouldUpdateWithPersistingAdCollection() {

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


        Set<AdContent> adContents = new HashSet<>();
        AdContent createdAdContent = this.adContentRepository.save(this.buildAdContent("Default Ad Content", "http://localhost:3000/image1", this.domainCache.buildDomain(AdContentTypeEnum.IMAGE)));
        adContents.add(createdAdContent);
        AdCollection adCollection = this.buildAdCollection("Default Ad Collection", "", adContents);
        AdCollection savedAdCollection = this.adCollectionRepository.save(adCollection);
        Module module = this.buildModule("Default Module", config, ModuleTypeEnum.ROTATING_AD, savedAdCollection);
        Module created = this.moduleRepository.save(module);

        created.setName("Updated Name");



        this.moduleRepository.save(created);


        List<Module> modules = this.moduleRepository.findAll();
        assertEquals(1, modules.size());
        assertModule(modules.get(0), "Updated Name", ModuleTypeEnum.ROTATING_AD, config);
        assertAdCollection(modules.get(0).getAdCollection(), "Default Ad Collection", "");
        assertAdContent(modules.get(0).getAdCollection().getAdContents().stream().toList().get(0), "Default Ad Content","http://localhost:3000/image1");
    }



    // delete
    @Test
    void shouldDeleteByIdAndNotDeleteChildren() {

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

        Module module1 = this.buildModule("Default Module 1", config, ModuleTypeEnum.ROTATING_AD, null);
        Module created1 = this.moduleRepository.save(module1);


        Set<AdContent> adContents = new HashSet<>();
        AdContent savedAdCont = this.adContentRepository.save(this.buildAdContent("Default Ad Content", "http://localhost:3000/image1", this.domainCache.buildDomain(AdContentTypeEnum.IMAGE)));
        adContents.add(savedAdCont);
        AdCollection adCollection = this.buildAdCollection("Default Ad Collection", "", adContents);
        AdCollection savedAdCol = this.adCollectionRepository.save(adCollection);
        Module module2 = this.buildModule("Default Module 2", null, ModuleTypeEnum.WEATHER, savedAdCol);
        Module created2 = this.moduleRepository.save(module2);

        this.moduleRepository.deleteById(created1.getId());

        Optional<AdCollection> savedAdCollection = this.adCollectionRepository.findById(created2.getAdCollection().getId());
        Optional<AdContent> savedAdContent = this.adContentRepository.findById(created2.getAdCollection().getAdContents().stream().toList().get(0).getId());

        assertEquals(1, this.moduleRepository.findAll().size());
        assertTrue(savedAdCollection.isPresent());
        assertAdCollection(savedAdCollection.get(), "Default Ad Collection", "");
        assertTrue(savedAdContent.isPresent());
        assertAdContent(savedAdContent.get(), "Default Ad Content", "http://localhost:3000/image1");
    }


    @Test
    void shouldDeleteAllAndNotDeleteChildren() {

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

        Module module1 = this.buildModule("Default Module 1", config, ModuleTypeEnum.ROTATING_AD, null);
        Module created1 = this.moduleRepository.save(module1);


        Set<AdContent> adContents = new HashSet<>();
        AdContent createdAdContent = this.adContentRepository.save(this.buildAdContent("Default Ad Content", "http://localhost:3000/image1", this.domainCache.buildDomain(AdContentTypeEnum.IMAGE)));


        adContents.add(createdAdContent);
        AdCollection adCollection = this.buildAdCollection("Default Ad Collection", "", adContents);
        AdCollection savedAdCol = this.adCollectionRepository.save(adCollection);
        Module module2 = this.buildModule("Default Module 2", null, ModuleTypeEnum.WEATHER, savedAdCol);
        Module created2 = this.moduleRepository.save(module2);

        this.moduleRepository.deleteAll();

        Optional<AdCollection> savedAdCollection = this.adCollectionRepository.findById(created2.getAdCollection().getId());
        Optional<AdContent> savedAdContent = this.adContentRepository.findById(created2.getAdCollection().getAdContents().stream().toList().get(0).getId());



        assertEquals(0, this.moduleRepository.findAll().size());
        assertTrue(savedAdCollection.isPresent());
        assertAdCollection(savedAdCollection.get(), "Default Ad Collection", "");
        assertTrue(savedAdContent.isPresent());
        assertAdContent(savedAdContent.get(), "Default Ad Content", "http://localhost:3000/image1");
    }


}


