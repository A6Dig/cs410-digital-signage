package com.a6dig.digitalsignage.entity;

<<<<<<< HEAD
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

=======
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "devices")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "layoutId", nullable = false)
    private Long layoutId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "layoutId", insertable = false, updatable = false)
    private Layout layout;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(nullable = false)
    private String ipAddress;

    @Column(name = "deviceGroupId")
    private Long deviceGroupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deviceGroupId", insertable = false, updatable = false)
    @JsonIgnoreProperties({"devices"})
    private DeviceGroup deviceGroup;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false)
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(Long layoutId) {
        this.layoutId = layoutId;
    }

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

>>>>>>> 28ec52b383dd2e358d2e5711391f9e0f9f3feb92
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

<<<<<<< HEAD
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

    

=======
    public Long getDeviceGroupId() {
        return deviceGroupId;
    }

    public void setDeviceGroupId(Long deviceGroupId) {
        this.deviceGroupId = deviceGroupId;
    }

    public DeviceGroup getDeviceGroup() {
        return deviceGroup;
    }

    public void setDeviceGroup(DeviceGroup deviceGroup) {
        this.deviceGroup = deviceGroup;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
>>>>>>> 28ec52b383dd2e358d2e5711391f9e0f9f3feb92
}
