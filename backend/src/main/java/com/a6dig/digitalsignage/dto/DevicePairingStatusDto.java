package com.a6dig.digitalsignage.dto;

public class DevicePairingStatusDto {
    private Long id;
    private Boolean paired;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getPaired() {
        return paired;
    }

    public void setPaired(Boolean paired) {
        this.paired = paired;
    }
}
