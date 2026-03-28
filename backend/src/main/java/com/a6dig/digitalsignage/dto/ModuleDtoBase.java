package com.a6dig.digitalsignage.dto;

import com.a6dig.digitalsignage.constant.ModuleTypeEnum;

import java.util.Optional;

public class ModuleDtoBase <T extends AdCollectionDtoBase>{

    private T adCollection;

    private String name;

    private ModuleTypeEnum type;

    private String config;

    public Optional<T> getAdCollection() {
        return Optional.ofNullable(adCollection);
    }

    public void setAdCollection(T adCollection) {
        this.adCollection = adCollection;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ModuleTypeEnum getType() {
        return type;
    }

    public void setType(ModuleTypeEnum type) {
        this.type = type;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }
}
