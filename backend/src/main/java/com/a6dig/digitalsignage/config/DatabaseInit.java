package com.a6dig.digitalsignage.config;

import com.a6dig.digitalsignage.constant.AdContentTypeEnum;
import com.a6dig.digitalsignage.constant.AppConstant;
import com.a6dig.digitalsignage.constant.ModuleTypeEnum;
import com.a6dig.digitalsignage.repository.DomainRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseInit {
    @Autowired
    private final DomainRepository domainRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private final DomainCache domainCache;

    public DatabaseInit(DomainRepository domainRepository, JdbcTemplate jdbcTemplate, DomainCache domainCache) {
        this.domainRepository = domainRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.domainCache = domainCache;
    }

    @PostConstruct
    public void init() {

        // module type
        this.jdbcTemplate.update(
                """
                        INSERT INTO domain (type, name, description, displayOrder, alphaNumCode)
                        VALUES('Module', 'Clock', '', 1, 'CLOCK')
                        """
        );
        this.jdbcTemplate.update(
                """
                        INSERT INTO domain (type, name, description, displayOrder, alphaNumCode)
                        VALUES('Module', 'Weather', '', 2, 'WEATHER')
                        """
        );
        this.jdbcTemplate.update(
                """     
                        INSERT INTO domain (type, name, description, displayOrder, alphaNumCode)
                        VALUES('Module', 'Rotating Ad', '', 3, 'ROTATING_AD')
                        """
        );

        // ad content type
        this.jdbcTemplate.update(
                """     
                        INSERT INTO domain (type, name, description, displayOrder, alphaNumCode)
                        VALUES('Ad Content', 'Image', '', 1, 'IMAGE')
                        """
        );
        this.jdbcTemplate.update(
                """     
                        INSERT INTO domain (type, name, description, displayOrder, alphaNumCode)
                        VALUES('Ad Content', 'Video', '', 2, 'VIDEO')
                        """
        );

        validateEnum(ModuleTypeEnum.class, "Module");
        validateEnum(AdContentTypeEnum.class, "Ad Content");

        this.domainCache.refresh(AppConstant.SystemConstant.DOMAIN_TYPE_MODULE);
        this.domainCache.refresh(AppConstant.SystemConstant.DOMAIN_TYPE_AD_CONTENT);
    }

    private <T extends Enum<T>> void validateEnum(Class<T> enumClass, String type) {
        List<String> domainValues = this.domainRepository.findValuesByType(type);

        // there is no unknown enum value in code
        for(T t : enumClass.getEnumConstants()) {
            if (!domainValues.contains(t.name())) {
                throw new IllegalStateException(
                        enumClass.getSimpleName() + "." + t.name() + " missing from the DB"
                );
            }
        }

        // there is no extra enum value in db
        for(String value : domainValues) {
            try {
                Enum.valueOf(enumClass, value);
            } catch (IllegalArgumentException ex) {
                throw new IllegalStateException(
                        "Database contain extra enum " + value + " that don't exist in " + enumClass.getSimpleName()
                );
            }
        }
    }


}
