package com.a6dig.digitalsignage.integration.service;

import com.a6dig.digitalsignage.dto.DevicePairRequestDto;
import com.a6dig.digitalsignage.dto.DevicePairResponseDto;
import com.a6dig.digitalsignage.dto.DeviceRegistrationResponseDto;
import com.a6dig.digitalsignage.dto.DeviceResponseDto;
import com.a6dig.digitalsignage.dto.DeviceVerifyRegistrationRequestDto;
import com.a6dig.digitalsignage.entity.Device;
import com.a6dig.digitalsignage.repository.DeviceRepository;
import com.a6dig.digitalsignage.service.DeviceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
public class DeviceServiceImplTest {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceServiceImpl deviceService;

    @BeforeEach
    void cleanUp() {
        deviceRepository.deleteAll();
    }

    private Device buildDevice(String name, String ipAddress) {
        Device device = new Device();
        device.setName(name);
        device.setIpAddress(ipAddress);
        return device;
    }

    @Test
    void shouldCreateDevice() {
        DeviceResponseDto saved = deviceService.createDevice(buildDevice("New Device", "101.0.0.1"));

        assertNotNull(saved.getId());
        assertEquals("New Device", saved.getName());
        assertEquals("101.0.0.1", saved.getIpAddress());
        assertNotNull(saved.getPairingId());
        assertFalse(saved.getPaired());
        assertEquals("OFFLINE", saved.getStatus());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());
    }

    @Test
    void shouldRegisterAndVerifyPendingDevice() {
        DeviceRegistrationResponseDto registration = deviceService.registerDevice("127.0.0.1");

        assertNotNull(registration.getPairingId());

        DeviceVerifyRegistrationRequestDto verifyRequest = new DeviceVerifyRegistrationRequestDto();
        verifyRequest.setPairingId(registration.getPairingId());

        DevicePairResponseDto verification = deviceService.verifyRegistration(verifyRequest);

        assertNotNull(verification.getId());
        assertEquals(registration.getPairingId(), verification.getPairingId());
        assertFalse(verification.getPairing().getPaired());
    }

    @Test
    void shouldPairRegisteredDevice() {
        DeviceRegistrationResponseDto registration = deviceService.registerDevice("127.0.0.1");
        Device registeredDevice = deviceRepository.findByPairingId(registration.getPairingId()).orElseThrow();

        DevicePairRequestDto pairRequest = new DevicePairRequestDto();
        pairRequest.setPairingId(registration.getPairingId());
        pairRequest.setPaired(true);

        DevicePairResponseDto response = deviceService.pairDevice(registeredDevice.getId(), pairRequest);

        assertEquals(registeredDevice.getId(), response.getId());
        assertEquals(registration.getPairingId(), response.getPairingId());
        assertTrue(response.getPairing().getPaired());

        Device savedDevice = deviceRepository.findById(registeredDevice.getId()).orElseThrow();
        assertTrue(savedDevice.getPaired());
        assertEquals("ONLINE", savedDevice.getStatus());
    }
}
