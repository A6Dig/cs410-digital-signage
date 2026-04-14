package com.a6dig.digitalsignage.dto;

public class DevicePairResponseDto {
    private Long id;
    private Long pairingId;
    private DevicePairingStatusDto pairing;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPairingId() {
        return pairingId;
    }

    public void setPairingId(Long pairingId) {
        this.pairingId = pairingId;
    }

    public DevicePairingStatusDto getPairing() {
        return pairing;
    }

    public void setPairing(DevicePairingStatusDto pairing) {
        this.pairing = pairing;
    }
}
