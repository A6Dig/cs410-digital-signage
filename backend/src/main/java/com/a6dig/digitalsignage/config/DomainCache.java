package com.a6dig.digitalsignage.config;


import com.a6dig.digitalsignage.constant.AppConstant;
import com.a6dig.digitalsignage.entity.Domain;
import com.a6dig.digitalsignage.repository.DomainRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

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
}
