package com.a6dig.digitalsignage.integration.misc;


import com.a6dig.digitalsignage.config.DatabaseInit;
import com.a6dig.digitalsignage.config.DomainCache;
import com.a6dig.digitalsignage.constant.AdContentTypeEnum;
import com.a6dig.digitalsignage.constant.AppConstant;
import com.a6dig.digitalsignage.constant.ModuleTypeEnum;
import com.a6dig.digitalsignage.entity.Domain;
import com.a6dig.digitalsignage.exception.InvalidDomainException;
import com.a6dig.digitalsignage.repository.DomainRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({DomainCache.class, DatabaseInit.class})
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
public class DomainCacheTest {
    @Autowired
    private DomainCache domainCache;

    @Autowired
    private DomainRepository domainRepository;

    private <T extends Enum<T>>void assertDomain(Domain domain, T expectedEnumClass,
                                                 String expectedType) {
        assertEquals(domain.getType(), expectedType);
        assertEquals(domain.getAlphaNumCode(), expectedEnumClass.name());
        assertNotNull(this.domainRepository.findDomainByAlphaNumCode(domain.getAlphaNumCode()));
    }

    @Test
    public void shouldModuleTypeExist() {
        Domain domain1 = this.domainCache.buildDomain(ModuleTypeEnum.WEATHER);
        assertEquals(ModuleTypeEnum.WEATHER.name(), domain1.getAlphaNumCode());
        assertDomain(domain1, ModuleTypeEnum.WEATHER, AppConstant.SystemConstant.DOMAIN_TYPE_MODULE);


        Domain domain2 = this.domainCache.buildDomain(ModuleTypeEnum.CLOCK);
        assertEquals(ModuleTypeEnum.CLOCK.name(), domain2.getAlphaNumCode());
        assertDomain(domain2, ModuleTypeEnum.CLOCK, AppConstant.SystemConstant.DOMAIN_TYPE_MODULE);



        Domain domain3 = this.domainCache.buildDomain(ModuleTypeEnum.ROTATING_AD);
        assertEquals(ModuleTypeEnum.ROTATING_AD.name(), domain3.getAlphaNumCode());
        assertDomain(domain3, ModuleTypeEnum.ROTATING_AD, AppConstant.SystemConstant.DOMAIN_TYPE_MODULE);

    }

    @Test
    public void shouldAdContentTypeExist() {

        Domain domain1 = this.domainCache.buildDomain(AdContentTypeEnum.IMAGE);
        assertEquals(AdContentTypeEnum.IMAGE.name(), domain1.getAlphaNumCode());
        assertDomain(domain1, AdContentTypeEnum.IMAGE, AppConstant.SystemConstant.DOMAIN_TYPE_AD_CONTENT);


        Domain domain2 = this.domainCache.buildDomain(AdContentTypeEnum.VIDEO);
        assertEquals(AdContentTypeEnum.VIDEO.name(), domain2.getAlphaNumCode());
        assertDomain(domain2, AdContentTypeEnum.VIDEO, AppConstant.SystemConstant.DOMAIN_TYPE_AD_CONTENT);
    }

    @Test
    public void shouldThrowWhenInvalidCode() {
        ;assertThrows(InvalidDomainException.class, () -> this.domainCache.validate("TEST", "Module"));
    }



    @Test
    public void shouldThrowWhenInvalidType() {
        ;assertThrows(InvalidDomainException.class, () -> this.domainCache.validate("IMAGE", "Module"));
    }

}
