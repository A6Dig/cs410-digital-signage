package com.a6dig.digitalsignage.dto;

import com.a6dig.digitalsignage.constant.ModuleTypeEnum;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;

public class ModuleDtoBase {

    private String name;

    private ModuleTypeEnum type;

    private JsonNode config;

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

    public JsonNode getConfig() {
        return config;
    }

    public void setConfig(JsonNode config) {
        this.config = config;
    }
}
