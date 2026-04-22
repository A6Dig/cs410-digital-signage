package com.a6dig.digitalsignage.integration.service;


import com.a6dig.digitalsignage.entity.Device;
import com.a6dig.digitalsignage.entity.DeviceGroup;
import com.a6dig.digitalsignage.entity.Layout;
import com.a6dig.digitalsignage.entity.LayoutSlot;
import com.a6dig.digitalsignage.entity.Module;
import com.a6dig.digitalsignage.repository.DeviceGroupRepository;
import com.a6dig.digitalsignage.repository.DeviceRepository;
import com.a6dig.digitalsignage.repository.LayoutRepository;
import com.a6dig.digitalsignage.service.DeviceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
public class DeviceServiceImplTest {

    @Autowired
    private DeviceGroupRepository deviceGroupRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceServiceImpl deviceService;

    @Autowired
    private LayoutRepository layoutRepository;

    @BeforeEach
    void setup() {
        this.deviceRepository.deleteAll();
        this.deviceGroupRepository.deleteAll();
        this.layoutRepository.deleteAll();
    }


    private Layout buildLayout(String name, int col, int row){
        Layout layout = new Layout();
        layout.setName(name);
        layout.setCols(col);
        layout.setRows(row);
        return layout;
    }



    private LayoutSlot buildLayoutSlot(Layout layout, Module module, int colPos, int rowPos, int colSpan, int rowSpan, int zIndex) {
        LayoutSlot slot = new LayoutSlot(layout);

        slot.setModule(module);
        slot.setColPos(colPos);
        slot.setRowPos(rowPos);
        slot.setColSpan(colSpan);
        slot.setRowSpan(rowSpan);
        slot.setzIndex(zIndex);

        return slot;
    }


    // helper
    private Device buildDevice(Long id
            , Layout layout
            , String name
            , String ipAddress
            , DeviceGroup deviceGroup) {

        Device device = new Device();
        device.setLayout(layout);
        device.setLayout(layout);
        device.setName(name);
        device.setIpAddress(ipAddress);
        device.setDeviceGroup(deviceGroup);

        return device;

    }

    private void assertDevice(Device device
                              , Long expectedLayoutId
                              , Optional<Layout> expectedLayout
                              , String expectedName
                              , String expectedIpAddress
                              , Long expectedDeviceGroupId
                              , Optional<DeviceGroup> expectedDeviceGroup
                              ) {
        assertNotNull(device.getId());
        assertEquals(expectedLayoutId, device.getLayout() != null ? device.getLayout().getId() : null);
        assertEquals(expectedLayout.map(Layout::getId).orElse(null)
                , device.getLayout() != null ? device.getLayout().getId() : null);
        assertEquals(expectedName, device.getName());
        assertEquals(expectedIpAddress, device.getIpAddress());
        assertEquals(expectedDeviceGroupId, device.getDeviceGroup() != null ? device.getDeviceGroup().getId() : null);
        assertEquals(expectedDeviceGroup.map(DeviceGroup::getId).orElse(null),
                device.getDeviceGroup() != null ?
                device.getDeviceGroup().getId() : null);
        assertNotNull(device.getCreatedAt());
        assertNotNull(device.getUpdatedAt());
    }

    @Test
    void shouldGetAllDevices() {
        this.deviceRepository.saveAll(List.of(
                this.buildDevice(null,null,"New Device 1","101.0.0.1", null),
                this.buildDevice(null,null,"New Device 2","101.0.0.2", null)
        ));
        List<Device> devices = this.deviceService.getAllDevices();
        devices.sort(Comparator.comparing(Device::getName));

        assertEquals(2, devices.size());
        assertDevice(devices.get(0),null, Optional.empty(), "New Device 1", "101.0.0.1", null, Optional.empty());
        assertDevice(devices.get(1),null, Optional.empty(), "New Device 2", "101.0.0.2", null, Optional.empty());

    }



    @Test
    void shouldGetDeviceById() {
        this.deviceRepository.saveAll(List.of(
                this.buildDevice(null,null,"New Device 1","101.0.0.1", null),
                this.buildDevice(null,null,"New Device 2","101.0.0.2", null)
        ));
        List<Device> devices = this.deviceService.getAllDevices();
        devices.sort(Comparator.comparing(Device::getName));

        assertEquals(2, devices.size());
        assertDevice(devices.get(0),null, Optional.empty(), "New Device 1", "101.0.0.1", null, Optional.empty());
        assertDevice(devices.get(1),null, Optional.empty(), "New Device 2", "101.0.0.2", null, Optional.empty());

        Long requestId = devices.get(0).getId();

        Device response = this.deviceService.getDeviceById(requestId);
        assertDevice(response,null, Optional.empty(), "New Device 1", "101.0.0.1", null, Optional.empty());

    }


    @Test
    void shouldCreateDevice() {
        Device request = this.buildDevice(null,
                null
                ,"New Device"
                ,"101.0.0.1"
                , null);
        Device saved = this.deviceService.createDevice(request);
        assertDevice(saved,null, Optional.empty(), "New Device", "101.0.0.1", null, Optional.empty());

    }


    @Test
    void shouldUpdateDevice() {
        // layout

        Layout layout = this.buildLayout("Default Layout", 2,2);
        LayoutSlot layoutSlot1 = this.buildLayoutSlot(layout, null, 2, 2, 2,1,0);
        LayoutSlot layoutSlot2 = this.buildLayoutSlot(layout, null, 22, 22, 2,1,0);

        layout.setSlots(List.of(layoutSlot1, layoutSlot2));
        Layout existingLayout = this.layoutRepository.saveAndFlush(layout);

        Device request = this.buildDevice(null,
                null
                ,"New Device"
                ,"101.0.0.1"
                , null);
        Device saved = this.deviceService.createDevice(request);
        assertDevice(saved,null, Optional.empty(), "New Device", "101.0.0.1", null, Optional.empty());


        saved.setLayout(existingLayout);

        Device updated = this.deviceService.updateDevice(saved.getId(), saved);
        assertDevice(updated,existingLayout.getId(), Optional.ofNullable(updated.getLayout()), "New Device", "101.0.0.1", null, Optional.empty());


    }


    @Test
    void shouldDeleteDevice() {
        this.deviceRepository.saveAll(List.of(
                this.buildDevice(null,null,"New Device 1","101.0.0.1", null),
                this.buildDevice(null,null,"New Device 2","101.0.0.2", null)
        ));
        List<Device> existingDevices = this.deviceService.getAllDevices();
        existingDevices.sort(Comparator.comparing(Device::getName));

        assertEquals(2, existingDevices.size());
        assertDevice(existingDevices.get(0),null, Optional.empty(), "New Device 1", "101.0.0.1", null, Optional.empty());
        assertDevice(existingDevices.get(1),null, Optional.empty(), "New Device 2", "101.0.0.2", null, Optional.empty());

        Long requestId = existingDevices.get(0).getId();

        this.deviceService.deleteDevice(requestId);


        List<Device> devices = this.deviceService.getAllDevices();
        assertEquals(1, devices.size());

        assertFalse(this.deviceRepository.existsById(requestId));
    }




    @Test
    void shouldDeleteAllDevices() {
        this.deviceRepository.saveAll(List.of(
                this.buildDevice(null,null,"New Device 1","101.0.0.1", null),
                this.buildDevice(null,null,"New Device 2","101.0.0.2", null)
        ));
        List<Device> existingDevices = this.deviceService.getAllDevices();
        existingDevices.sort(Comparator.comparing(Device::getName));

        assertEquals(2, existingDevices.size());
        assertDevice(existingDevices.get(0),null, Optional.empty(), "New Device 1", "101.0.0.1", null, Optional.empty());
        assertDevice(existingDevices.get(1),null, Optional.empty(), "New Device 2", "101.0.0.2", null, Optional.empty());


        this.deviceService.deleteAllDevices();
        List<Device> devices = this.deviceService.getAllDevices();
        assertEquals(0, devices.size());
    }

}
