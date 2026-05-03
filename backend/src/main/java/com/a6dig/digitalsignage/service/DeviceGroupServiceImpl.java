package com.a6dig.digitalsignage.service;

import com.a6dig.digitalsignage.constant.AppConstant;
import com.a6dig.digitalsignage.entity.DeviceGroup;
import com.a6dig.digitalsignage.entity.Layout;
import com.a6dig.digitalsignage.exception.InvalidLayoutException;
import com.a6dig.digitalsignage.repository.DeviceGroupRepository;
import com.a6dig.digitalsignage.repository.LayoutRepository;
import com.a6dig.digitalsignage.util.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeviceGroupServiceImpl implements DeviceGroupService {

    @Autowired
    private DeviceGroupRepository deviceGroupRepository;

    @Autowired
    private LayoutRepository layoutRepository;

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
        if (deviceGroup != null && deviceGroup.getLayout() != null) {
            deviceGroup.setLayout(resolveExistingLayout(deviceGroup.getLayout()));
        }

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

    private Layout resolveExistingLayout(Layout layout) {
        if (layout.getId() == null) {
            throw new InvalidLayoutException(
                    AppConstant.ExceptionMessage.LAYOUT_INVALID_LAYOUT_ID,
                    List.of(ErrorMessage.createErrorMessage("Layout id must be provided"))
            );
        }

        return layoutRepository.findById(layout.getId()).orElseThrow(() -> new InvalidLayoutException(
                AppConstant.ExceptionMessage.LAYOUT_INVALID_LAYOUT_ID,
                List.of(ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.layoutIdDoesNotExist(layout.getId())))
        ));
    }
}
