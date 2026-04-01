package com.a6dig.digitalsignage.service;

import com.a6dig.digitalsignage.entity.DeviceGroup;
import com.a6dig.digitalsignage.repository.DeviceGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeviceGroupServiceImpl implements DeviceGroupService {

    @Autowired
    private DeviceGroupRepository deviceGroupRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DeviceGroup> getAllDeviceGroups() {
        return deviceGroupRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public DeviceGroup getDeviceGroupById(Long id) {
        return deviceGroupRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public DeviceGroup createDeviceGroup(DeviceGroup deviceGroup) {
        return deviceGroupRepository.save(deviceGroup);
    }

    @Override
    @Transactional
    public DeviceGroup updateDeviceGroup(Long id, DeviceGroup deviceGroup) {
        DeviceGroup existing = deviceGroupRepository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }

        return deviceGroupRepository.save(existing);
    }

    @Override
    @Transactional
    public boolean deleteDeviceGroup(Long id) {
        DeviceGroup existing = deviceGroupRepository.findById(id).orElse(null);
        if (existing == null) {
            return false;
        }

        deviceGroupRepository.delete(existing);
        return true;
    }

    @Override
    @Transactional
    public void deleteAllDeviceGroups() {
        deviceGroupRepository.deleteAll();
    }
}
