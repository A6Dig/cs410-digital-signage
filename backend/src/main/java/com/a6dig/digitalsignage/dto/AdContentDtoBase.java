package com.a6dig.digitalsignage.dto;

import com.a6dig.digitalsignage.constant.AdContentTypeEnum;

public class AdContentDtoBase {
    private String name;
    private String url;
    private AdContentTypeEnum type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public AdContentTypeEnum getType() {
        return type;
    }

    public void setType(AdContentTypeEnum type) {
        this.type = type;
    }
}
