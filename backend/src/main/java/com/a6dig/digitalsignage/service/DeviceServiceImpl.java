package com.a6dig.digitalsignage.service;

import com.a6dig.digitalsignage.constant.AppConstant;
import com.a6dig.digitalsignage.entity.Device;
import com.a6dig.digitalsignage.exception.InvalidDeviceException;
import com.a6dig.digitalsignage.exception.DeviceNotFoundException;
import com.a6dig.digitalsignage.repository.DeviceGroupRepository;
import com.a6dig.digitalsignage.repository.DeviceRepository;
import com.a6dig.digitalsignage.repository.LayoutRepository;
import com.a6dig.digitalsignage.util.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Device getDeviceById(Long id) {
        return findExistingDevice(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Device> getDevicesByDeviceGroupId(Long deviceGroupId) {
        return deviceRepository.findByDeviceGroupId(deviceGroupId);
    }

    @Override
    @Transactional
    public Device createDevice(Device device) {
        if (device == null) {
            throw new InvalidDeviceException(
                    AppConstant.ExceptionMessage.Device.INVALID_DATA_PROVIDED,
                    List.of(ErrorMessage.createErrorMessage("Device cannot be null"))
            );
        }

        if (device.getId() != null) {
            throw new InvalidDeviceException(
                    AppConstant.ExceptionMessage.Device.INVALID_DATA_PROVIDED,
                    List.of(ErrorMessage.createErrorMessage("Id cannot be null"))
            );
        }
        return deviceRepository.save(device);
    }

    @Override
    @Transactional
    public Device updateDevice(Long id, Device device) {
        Device existing = findExistingDevice(id);

        if (device.getLayout() != null) {
            existing.setLayout(device.getLayout());
        }
        if (device.getName() != null) {
            existing.setName(device.getName().trim());
        }
        if (device.getIpAddress() != null) {
            existing.setIpAddress(device.getIpAddress().trim());
        }
        if (device.getDeviceGroup() != null) {
            existing.setDeviceGroup(device.getDeviceGroup());
        }

        return deviceRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteDevice(Long id) {
        Device existing = findExistingDevice(id);
        deviceRepository.delete(existing);
    }

    @Override
    @Transactional
    public void deleteAllDevices() {
        deviceRepository.deleteAll();
    }

    private Device findExistingDevice(Long id) {
        return deviceRepository.findById(id).orElseThrow(() -> new DeviceNotFoundException(
                AppConstant.ExceptionMessage.Device.NOT_FOUND,
                List.of(ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.Device.idDoesNotExist(id)))
        ));
    }

}
