package com.a6dig.digitalsignage.config;


import com.a6dig.digitalsignage.constant.AppConstant;
import com.a6dig.digitalsignage.entity.Domain;
import com.a6dig.digitalsignage.exception.InvalidDomainException;
import com.a6dig.digitalsignage.repository.DomainRepository;
import com.a6dig.digitalsignage.util.ErrorMessage;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class DomainCache {
    private final Map<String, Domain> cache = new HashMap<>();

    @Autowired
    private DomainRepository domainRepository;

    @PostConstruct
    public void init() {
        domainRepository.findDomainByType(AppConstant.SystemConstant.DOMAIN_TYPE_AD_CONTENT).forEach(d -> cache.put(d.getAlphaNumCode(), d));
        domainRepository.findDomainByType(AppConstant.SystemConstant.DOMAIN_TYPE_MODULE).forEach(d -> cache.put(d.getAlphaNumCode(), d));
    }

    public void refresh(String type) {
        domainRepository.findDomainByType(type).forEach(d -> cache.put(d.getAlphaNumCode(), d));
    }

    public <T extends Enum<T>>Domain buildDomain(T enumValue) {
        return cache.get(enumValue.name());
    }


    public void validate(String code, String type) {
        Domain domain = this.cache.get(code);

        if (domain == null) {
            throw new InvalidDomainException(
                    AppConstant.ExceptionMessage.Domain.NOT_FOUND,
                    List.of(ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.Domain.domainCodeDoesNotExist(code)))
            );
        }

        if (!Objects.equals(domain.getType(), type)) {

            throw new InvalidDomainException(
                    AppConstant.ExceptionMessage.Domain.NOT_FOUND,
                    List.of(ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.Domain.domainCodeDoesNotBelongToTheType(code, type)))
            );
        }
    }
}
