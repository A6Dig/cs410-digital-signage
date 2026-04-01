package com.a6dig.digitalsignage.service;

import com.a6dig.digitalsignage.entity.DeviceGroup;

import java.util.List;

public interface DeviceGroupService {
    List<DeviceGroup> getAllDeviceGroups();
    DeviceGroup getDeviceGroupById(Long id);
    DeviceGroup createDeviceGroup(DeviceGroup deviceGroup);
    DeviceGroup updateDeviceGroup(Long id, DeviceGroup deviceGroup);
    boolean deleteDeviceGroup(Long id);
    void deleteAllDeviceGroups();
}
