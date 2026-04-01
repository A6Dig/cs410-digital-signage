package com.a6dig.digitalsignage.service;

import com.a6dig.digitalsignage.entity.Device;

import java.util.List;

public interface DeviceService {
    List<Device> getAllDevices();
    Device getDeviceById(Long id);
    List<Device> getDevicesByDeviceGroupId(Long deviceGroupId);
    Device createDevice(Device device);
    Device updateDevice(Long id, Device device);
    void deleteDevice(Long id);
    void deleteAllDevices();
}
