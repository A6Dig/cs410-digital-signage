package com.a6dig.digitalsignage.service;

import com.a6dig.digitalsignage.dto.DeviceGroupAssignRequestDto;
import com.a6dig.digitalsignage.dto.DevicePairRequestDto;
import com.a6dig.digitalsignage.dto.DevicePairResponseDto;
import com.a6dig.digitalsignage.dto.DeviceRegistrationResponseDto;
import com.a6dig.digitalsignage.dto.DeviceResponseDto;
import com.a6dig.digitalsignage.dto.DeviceStatusUpdateRequestDto;
import com.a6dig.digitalsignage.dto.DeviceVerifyRegistrationRequestDto;
import com.a6dig.digitalsignage.entity.Device;

import java.util.List;

public interface DeviceService {
    DeviceRegistrationResponseDto registerDevice(String ipAddress);
    DevicePairResponseDto verifyRegistration(DeviceVerifyRegistrationRequestDto requestDto);
    List<DeviceResponseDto> getAllDevices();
    DeviceResponseDto getDeviceById(Long id);
    List<DeviceResponseDto> getDevicesByDeviceGroupId(Long deviceGroupId);
    DeviceResponseDto createDevice(Device device);
    DeviceResponseDto updateDevice(Long id, Device device);
    DeviceResponseDto updateDeviceStatus(Long id, DeviceStatusUpdateRequestDto requestDto);
    DeviceResponseDto assignDeviceGroup(Long id, DeviceGroupAssignRequestDto requestDto);
    DevicePairResponseDto pairDevice(Long id, DevicePairRequestDto requestDto);
    void deleteDevice(Long id);
    void deleteAllDevices();
}
