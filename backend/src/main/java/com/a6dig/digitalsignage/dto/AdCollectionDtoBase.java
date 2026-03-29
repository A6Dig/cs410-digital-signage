package com.a6dig.digitalsignage.dto;

import java.util.List;

public class AdCollectionDtoBase<T extends AdContentDtoBase> {
    private String name;
    private String url;
    private List<T> adContents;

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

    public List<T> getAdContents() {
        return adContents;
    }

    public void setAdContents(List<T> adContents) {
        this.adContents = adContents;
    }
}
