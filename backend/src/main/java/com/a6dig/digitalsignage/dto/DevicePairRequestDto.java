package com.a6dig.digitalsignage.dto;

public class DevicePairRequestDto {
    private Long pairingId;
    private Boolean paired;

    public Long getPairingId() {
        return pairingId;
    }

    public void setPairingId(Long pairingId) {
        this.pairingId = pairingId;
    }

    public Boolean getPaired() {
        return paired;
    }

    public void setPaired(Boolean paired) {
        this.paired = paired;
    }
}
