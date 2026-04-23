package com.a6dig.digitalsignage.service;

import com.a6dig.digitalsignage.constant.AppConstant;
import com.a6dig.digitalsignage.dto.DeviceGroupAssignRequestDto;
import com.a6dig.digitalsignage.dto.DevicePairRequestDto;
import com.a6dig.digitalsignage.dto.DevicePairResponseDto;
import com.a6dig.digitalsignage.dto.DevicePairingStatusDto;
import com.a6dig.digitalsignage.dto.DeviceRegistrationResponseDto;
import com.a6dig.digitalsignage.dto.DeviceResponseDto;
import com.a6dig.digitalsignage.dto.DeviceStatusUpdateRequestDto;
import com.a6dig.digitalsignage.dto.DeviceVerifyRegistrationRequestDto;
import com.a6dig.digitalsignage.entity.Device;
import com.a6dig.digitalsignage.entity.DeviceGroup;
import com.a6dig.digitalsignage.exception.DeviceNotFoundException;
import com.a6dig.digitalsignage.exception.InvalidDeviceException;
import com.a6dig.digitalsignage.exception.PairingNotFoundException;
import com.a6dig.digitalsignage.repository.DeviceGroupRepository;
import com.a6dig.digitalsignage.repository.DeviceRepository;
import com.a6dig.digitalsignage.util.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
public class DeviceServiceImpl implements DeviceService {

    private static final long MIN_PAIRING_ID = 100000L;
    private static final long MAX_PAIRING_ID = 999999L;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceGroupRepository deviceGroupRepository;

    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    @Transactional
    public DeviceRegistrationResponseDto registerDevice(String ipAddress) {
        Device device = new Device();
        Long pairingId = generateUniquePairingId();
        device.setPairingId(pairingId);
        device.setPaired(false);
        device.setStatus("OFFLINE");
        device.setName("Unpaired Device " + pairingId);
        device.setIpAddress(normalizeIpAddress(ipAddress));

        Device savedDevice = deviceRepository.save(device);

        DeviceRegistrationResponseDto responseDto = new DeviceRegistrationResponseDto();
        responseDto.setPairingId(savedDevice.getPairingId());
        return responseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public DevicePairResponseDto verifyRegistration(DeviceVerifyRegistrationRequestDto requestDto) {
        if (requestDto == null || requestDto.getPairingId() == null) {
            throw invalidDevice("Pairing id cannot be null");
        }

        Device device = findByPairingId(requestDto.getPairingId());
        return toPairResponseDto(device);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeviceResponseDto> getAllDevices() {
        return deviceRepository.findAll().stream().map(this::toResponseDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DeviceResponseDto getDeviceById(Long id) {
        return toResponseDto(findExistingDevice(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeviceResponseDto> getDevicesByDeviceGroupId(Long deviceGroupId) {
        return deviceRepository.findByDeviceGroupId(deviceGroupId).stream().map(this::toResponseDto).toList();
    }

    @Override
    @Transactional
    public DeviceResponseDto createDevice(Device device) {
        validateDevicePayload(device);

        if (device.getPairingId() == null) {
            device.setPairingId(generateUniquePairingId());
        } else if (deviceRepository.existsByPairingId(device.getPairingId())) {
            throw invalidDevice("Pairing id already exists");
        }

        if (device.getPaired() == null) {
            device.setPaired(false);
        }
        if (device.getStatus() == null || device.getStatus().isBlank()) {
            device.setStatus("OFFLINE");
        } else {
            device.setStatus(normalizeStatus(device.getStatus()));
        }

        device.setName(device.getName().trim());
        device.setIpAddress(device.getIpAddress().trim());

        return toResponseDto(deviceRepository.save(device));
    }

    @Override
    @Transactional
    public DeviceResponseDto updateDevice(Long id, Device device) {
        Device existing = findExistingDevice(id);

        if (device.getLayout() != null) {
            existing.setLayout(device.getLayout());
        }
        if (device.getName() != null && !device.getName().isBlank()) {
            existing.setName(device.getName().trim());
        }
        if (device.getIpAddress() != null && !device.getIpAddress().isBlank()) {
            existing.setIpAddress(device.getIpAddress().trim());
        }
        if (device.getDeviceGroup() != null) {
            existing.setDeviceGroup(device.getDeviceGroup());
        }
        if (device.getPairingId() != null && !Objects.equals(existing.getPairingId(), device.getPairingId())) {
            if (deviceRepository.existsByPairingId(device.getPairingId())) {
                throw invalidDevice("Pairing id already exists");
            }
            existing.setPairingId(device.getPairingId());
        }
        if (device.getPaired() != null) {
            existing.setPaired(device.getPaired());
        }
        if (device.getStatus() != null && !device.getStatus().isBlank()) {
            existing.setStatus(normalizeStatus(device.getStatus()));
        }

        return toResponseDto(deviceRepository.save(existing));
    }

    @Override
    @Transactional
    public DeviceResponseDto updateDeviceStatus(Long id, DeviceStatusUpdateRequestDto requestDto) {
        if (requestDto == null || requestDto.getStatus() == null || requestDto.getStatus().isBlank()) {
            throw invalidDevice("Device status cannot be null or empty");
        }

        Device device = findExistingDevice(id);
        device.setStatus(normalizeStatus(requestDto.getStatus()));
        return toResponseDto(deviceRepository.save(device));
    }

    @Override
    @Transactional
    public DeviceResponseDto assignDeviceGroup(Long id, DeviceGroupAssignRequestDto requestDto) {
        if (requestDto == null || requestDto.getDeviceGroupId() == null) {
            throw invalidDevice("Device group id cannot be null");
        }

        Device device = findExistingDevice(id);
        DeviceGroup deviceGroup = deviceGroupRepository.findById(requestDto.getDeviceGroupId())
                .orElseThrow(() -> invalidDevice("Device group with id " + requestDto.getDeviceGroupId() + " doesn't exist"));

        device.setDeviceGroup(deviceGroup);
        return toResponseDto(deviceRepository.save(device));
    }

    @Override
    @Transactional
    public DevicePairResponseDto pairDevice(Long id, DevicePairRequestDto requestDto) {
        if (requestDto == null || requestDto.getPairingId() == null) {
            throw invalidDevice("Pairing id cannot be null");
        }

        Device device = findExistingDevice(id);
        Device pairedDevice = findByPairingId(requestDto.getPairingId());

        if (!Objects.equals(device.getId(), pairedDevice.getId())) {
            throw invalidDevice("Pairing id does not belong to device " + id);
        }

        if (requestDto.getPaired() != null) {
            device.setPaired(requestDto.getPaired());
        } else {
            device.setPaired(true);
        }

        if (Boolean.TRUE.equals(device.getPaired()) && (device.getStatus() == null || "OFFLINE".equalsIgnoreCase(device.getStatus()))) {
            device.setStatus("ONLINE");
        }

        return toPairResponseDto(deviceRepository.save(device));
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

    private void validateDevicePayload(Device device) {
        if (device == null) {
            throw invalidDevice("Device cannot be null");
        }
        if (device.getName() == null || device.getName().isBlank()) {
            throw invalidDevice("Device name cannot be null or empty");
        }
        if (device.getIpAddress() == null || device.getIpAddress().isBlank()) {
            throw invalidDevice("Device ipAddress cannot be null or empty");
        }
    }

    private Device findExistingDevice(Long id) {
        return deviceRepository.findById(id).orElseThrow(() -> new DeviceNotFoundException(
                AppConstant.ExceptionMessage.Device.NOT_FOUND,
                List.of(ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.Device.idDoesNotExist(id)))
        ));
    }

    private Device findByPairingId(Long pairingId) {
        return deviceRepository.findByPairingId(pairingId).orElseThrow(() -> new PairingNotFoundException(
                AppConstant.ExceptionMessage.Device.PAIRING_ID_NOT_FOUND,
                List.of(ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.Device.pairingIdDoesNotExist(pairingId)))
        ));
    }

    private DeviceResponseDto toResponseDto(Device device) {
        DeviceResponseDto dto = new DeviceResponseDto();
        dto.setId(device.getId());
        dto.setLayoutId(device.getLayout() != null ? device.getLayout().getId() : null);
        dto.setName(device.getName());
        dto.setIpAddress(device.getIpAddress());
        dto.setPairingId(device.getPairingId());
        dto.setPaired(device.getPaired());
        dto.setStatus(device.getStatus());
        dto.setDeviceGroupId(device.getDeviceGroup() != null ? device.getDeviceGroup().getId() : null);
        dto.setCreatedAt(device.getCreatedAt());
        dto.setUpdatedAt(device.getUpdatedAt());
        return dto;
    }

    private DevicePairResponseDto toPairResponseDto(Device device) {
        DevicePairingStatusDto pairingStatusDto = new DevicePairingStatusDto();
        pairingStatusDto.setId(device.getId());
        pairingStatusDto.setPaired(device.getPaired());

        DevicePairResponseDto responseDto = new DevicePairResponseDto();
        responseDto.setId(device.getId());
        responseDto.setPairingId(device.getPairingId());
        responseDto.setPairing(pairingStatusDto);
        return responseDto;
    }

    private Long generateUniquePairingId() {
        long bound = (MAX_PAIRING_ID - MIN_PAIRING_ID) + 1;
        long pairingId;
        do {
            pairingId = MIN_PAIRING_ID + secureRandom.nextLong(bound);
        } while (deviceRepository.existsByPairingId(pairingId));

        return pairingId;
    }

    private String normalizeIpAddress(String ipAddress) {
        if (ipAddress == null || ipAddress.isBlank()) {
            return "UNKNOWN";
        }

        String normalizedIpAddress = ipAddress.trim();
        if (normalizedIpAddress.equals("0:0:0:0:0:0:0:1")) {
            return "127.0.0.1";
        }

        return normalizedIpAddress;
    }

    private String normalizeStatus(String status) {
        String normalizedStatus = status.trim().toUpperCase(Locale.ROOT);
        if (!normalizedStatus.equals("ONLINE") && !normalizedStatus.equals("OFFLINE")) {
            throw invalidDevice("Unsupported device status: " + status);
        }
        return normalizedStatus;
    }

    private InvalidDeviceException invalidDevice(String errorMessage) {
        return new InvalidDeviceException(
                AppConstant.ExceptionMessage.Device.INVALID_DATA_PROVIDED,
                List.of(ErrorMessage.createErrorMessage(errorMessage))
        );
    }
}
