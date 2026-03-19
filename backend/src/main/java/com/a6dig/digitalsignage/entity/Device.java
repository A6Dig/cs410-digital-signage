package com.a6dig.digitalsignage.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Device {

    private String id;
    private String name;
    private String ipAddress;
    private Integer deviceGroupId;
    private LocalDateTime lastConnected;
    
    public Device() {
    }

    public Device(String id, String name, String ipAddress, Integer deviceGroupId, LocalDateTime lastConnected) {
        this.id = id;
        this.name = name;
        this.ipAddress = ipAddress;
        this.deviceGroupId = deviceGroupId;
        this.lastConnected = lastConnected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Integer getDeviceGroupId() {
        return deviceGroupId;
    }

    public void setDeviceGroupId(Integer deviceGroupId) {
        this.deviceGroupId = deviceGroupId;
    }

    public LocalDateTime getLastConnected() {
        return lastConnected;
    }

    public void setLastConnected(LocalDateTime lastConnected) {
        this.lastConnected = lastConnected;
    }

    

}
