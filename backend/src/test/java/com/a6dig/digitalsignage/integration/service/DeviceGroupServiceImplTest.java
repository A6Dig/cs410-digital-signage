package com.a6dig.digitalsignage.integration.service;


import com.a6dig.digitalsignage.entity.*;
import com.a6dig.digitalsignage.entity.Module;
import com.a6dig.digitalsignage.exception.DeviceNotFoundException;
import com.a6dig.digitalsignage.exception.InvalidLayoutException;
import com.a6dig.digitalsignage.exception.InvalidLayoutSlotException;
import com.a6dig.digitalsignage.exception.LayoutNotFoundException;
import com.a6dig.digitalsignage.repository.DeviceGroupRepository;
import com.a6dig.digitalsignage.repository.DeviceRepository;
import com.a6dig.digitalsignage.repository.LayoutRepository;
import com.a6dig.digitalsignage.service.DeviceGroupServiceImpl;
import com.a6dig.digitalsignage.service.DeviceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
public class DeviceGroupServiceImplTest {

    @Autowired
    private DeviceGroupRepository deviceGroupRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceServiceImpl deviceService;

    @Autowired
    private DeviceGroupServiceImpl deviceGroupService;

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

    private DeviceGroup buildDeviceGroup(
            String name
            , String description
            , Layout layout
            , List<Device> devices
    ) {
        DeviceGroup deviceGroup = new DeviceGroup();
        deviceGroup.setName(name);
        deviceGroup.setDescription(description);
        deviceGroup.setLayout(layout);
        deviceGroup.setDevices(devices);
        return  deviceGroup;
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

    private void assertDeviceGroup(DeviceGroup deviceGroup
       , String expectedName
       , String expectedDescription
       , Layout expectedLayout
       , List<Device> expectedDevices
    ){
       assertNotNull(deviceGroup.getId());
       assertEquals(expectedName, deviceGroup.getName());
       assertEquals(expectedDescription, deviceGroup.getDescription());
       assertEquals(expectedLayout != null ? expectedLayout.getId() : null
               , deviceGroup.getLayout() != null ? deviceGroup.getLayout().getId() : null);
       assertEquals(expectedDevices != null ? expectedDevices.size() : 0
               , deviceGroup.getDevices() != null ? deviceGroup.getDevices().size() : 0);
       assertNotNull(deviceGroup.getCreatedAt());
       assertNotNull(deviceGroup.getUpdatedAt());
    }


    // read

    @Test
    void shouldGetAllDeviceGroups() {
        DeviceGroup deviceGroup1 = this.buildDeviceGroup("Device Group 1", "Description 1", null, new ArrayList<>());
        DeviceGroup deviceGroup2 = this.buildDeviceGroup("Device Group 2", "Description 2", null, new ArrayList<>());
        this.deviceGroupRepository.saveAll(List.of(deviceGroup1, deviceGroup2));
        List<DeviceGroup> response = this.deviceGroupService.getAllDeviceGroups();
        assertEquals(2, response.size());
    }

    @Test
    @Transactional
    void shouldGetDeviceGroupById() {
        DeviceGroup deviceGroup = this.buildDeviceGroup("Device Group", "Description", null, new ArrayList<>());
        DeviceGroup saved = this.deviceGroupRepository.save(deviceGroup);
        DeviceGroup response = this.deviceGroupService.getDeviceGroupById(saved.getId());
        assertDeviceGroup(response, saved.getName(), saved.getDescription(), saved.getLayout(), saved.getDevices());
    }

    // create

    @Test
    void shouldCreateDeviceGroupWithoutLayout() {
        DeviceGroup deviceGroup = this.buildDeviceGroup("Device Group", "Description", null, new ArrayList<>());
        DeviceGroup saved = this.deviceGroupService.createDeviceGroup(deviceGroup);
        assertDeviceGroup(saved, deviceGroup.getName(), deviceGroup.getDescription(), deviceGroup.getLayout(), deviceGroup.getDevices());
    }

    @Test
    void shouldCreateDeviceGroupWithLayout() {
        Layout layout = this.buildLayout("Layout", 1, 1);
        Layout existingLayout = this.layoutRepository.save(layout);

        DeviceGroup deviceGroup = this.buildDeviceGroup("Device Group", "Description", existingLayout, new ArrayList<>());
        DeviceGroup saved = this.deviceGroupService.createDeviceGroup(deviceGroup);

        assertDeviceGroup(saved, deviceGroup.getName(), deviceGroup.getDescription(), existingLayout, deviceGroup.getDevices());

        // it should not create any new layout
        assertEquals(1, this.layoutRepository.findAll().size());
    }

    @Test
    void shouldThrowWhenCreateDeviceGroupWithInvalidLayout() {
        Layout layout = this.buildLayout("Layout", 1, 1);
        DeviceGroup deviceGroup = this.buildDeviceGroup("Device Group", "Description", layout, new ArrayList<>());
        assertThrows(InvalidLayoutException.class, () -> this.deviceGroupService.createDeviceGroup(deviceGroup));
    }

    @Test
    void shouldAssignExistingLayoutWhenCreateDeviceGroupWithoutUpdatingExistingLayout() {
        Layout layout = this.buildLayout("Layout", 1, 1);
        Layout savedLayout = this.layoutRepository.saveAndFlush(layout);
        Long layoutId = savedLayout.getId();

        Layout layoutToAssign = this.buildLayout("Layout to Assign", 1, 1);
        layoutToAssign.setId(layoutId);

        DeviceGroup deviceGroup = this.buildDeviceGroup("Device Group", "Description", layoutToAssign, new ArrayList<>());
        DeviceGroup saved = this.deviceGroupService.createDeviceGroup(deviceGroup);

        Optional<Layout> existingLayout = this.layoutRepository.findById(layoutId);
        assertEquals(savedLayout.getName(), existingLayout.map(Layout::getName).orElse(null));
        assertEquals(savedLayout.getRows(), existingLayout.map(Layout::getRows).orElse(null));
        assertEquals(savedLayout.getCols(), existingLayout.map(Layout::getCols).orElse(null));

    }


    @Test
    void shouldAssignSingleLayoutToMultipleDeviceGroupWhenCreateDeviceGroup() {
        Layout layout = this.buildLayout("Layout", 1, 1);
        Layout savedLayout = this.layoutRepository.saveAndFlush(layout);

        DeviceGroup deviceGroup1 = this.buildDeviceGroup("Device Group 1", "Description 1", savedLayout, new ArrayList<>());
        DeviceGroup savedDeviceGroup1 = this.deviceGroupService.createDeviceGroup(deviceGroup1);

        DeviceGroup deviceGroup2 = this.buildDeviceGroup("Device Group 2", "Description 2", savedLayout, new ArrayList<>());
        DeviceGroup savedDeviceGroup2 = this.deviceGroupService.createDeviceGroup(deviceGroup2);

        assertDeviceGroup(savedDeviceGroup1, deviceGroup1.getName(), deviceGroup1.getDescription(), savedLayout, deviceGroup1.getDevices());
        assertDeviceGroup(savedDeviceGroup2, deviceGroup2.getName(), deviceGroup2.getDescription(), savedLayout, deviceGroup2.getDevices());

        assertEquals(1, this.layoutRepository.findAll().size());
    }

    @Test
    void shouldCreateDeviceGroupWithDevicesWithoutSavingNewDevices() {
        Layout layout = this.buildLayout("Layout", 1, 1);
        Layout savedLayout = this.layoutRepository.saveAndFlush(layout);

        Device device = this.buildDevice(null, savedLayout, "Device", "101.0.0.1", null);

        DeviceGroup deviceGroup = this.buildDeviceGroup("Device Group", "Description", savedLayout, List.of(device));
        this.deviceGroupService.createDeviceGroup(deviceGroup);
        assertEquals(0, this.deviceRepository.findAll().size());
    }

    @Test
    void shouldAssignExistingDeviceWhenCreateDeviceGroup() {
        Layout layout = this.buildLayout("Layout", 1,1);
        Layout savedLayout = this.layoutRepository.saveAndFlush(layout);

        Device device = this.buildDevice(null, savedLayout, "Device", "101.0.0.1", null);
        Device savedDevice = this.deviceRepository.saveAndFlush(device);

        DeviceGroup deviceGroup = this.buildDeviceGroup("Device Group", "Description", null, List.of(savedDevice));
        DeviceGroup saved = this.deviceGroupService.createDeviceGroup(deviceGroup);

        assertDeviceGroup(saved, deviceGroup.getName(), deviceGroup.getDescription(), null, List.of(savedDevice));
        assertEquals(1, this.deviceRepository.findAll().size());
    }

    @Test
    @Transactional
    void shouldNotUpdateExistingDeviceWhenCreateDeviceGroup() {
        Layout layout = this.buildLayout("Layout", 1, 1);
        Layout savedLayout = this.layoutRepository.saveAndFlush(layout);

        Device device = this.buildDevice(null, savedLayout, "Device", "101.0.0.1", null);
        Device savedDevice = this.deviceRepository.saveAndFlush(device);

        savedDevice.setName("Modified Device Name");

        DeviceGroup deviceGroup = this.buildDeviceGroup("Device Group", "Description", null, List.of(savedDevice));
        DeviceGroup saved = this.deviceGroupService.createDeviceGroup(deviceGroup);

        assertDeviceGroup(saved, deviceGroup.getName(), deviceGroup.getDescription(), null, List.of(savedDevice));

        // verify the device's properties didn't get modified when using this service
        Optional<Device> existingDevice = this.deviceRepository.findById(device.getId());

        assertTrue(existingDevice.isPresent());
        assertEquals("Device", existingDevice.get().getName());
        assertEquals("101.0.0.1", existingDevice.get().getIpAddress());
        assertEquals(savedDevice.getCreatedAt(), existingDevice.get().getCreatedAt());
        assertEquals(savedDevice.getUpdatedAt(), existingDevice.get().getUpdatedAt());
        assertEquals(saved.getId(), existingDevice.get().getDeviceGroup().getId());
    }

    @Test
    void shouldThrowDeviceNotFoundWhenAssignedNonExistentDeviceWhenCreateDeviceGroup() {
        Layout layout = this.buildLayout("Layout", 1, 1);
        Layout savedLayout = this.layoutRepository.saveAndFlush(layout);

        Device device = this.buildDevice(1L, savedLayout, "Device", "101.0.0.1", null);

        DeviceGroup deviceGroup = this.buildDeviceGroup("Device Group", "Description", null, List.of(device));

        assertThrows(DeviceNotFoundException.class, () -> this.deviceGroupService.createDeviceGroup(deviceGroup));

    }

    // update

    @Test
    void shouldThrowWhenUpdateNonExistentDeviceGroup() {
        Layout layout = this.buildLayout("Layout", 1, 1);
        Layout savedLayout = this.layoutRepository.saveAndFlush(layout);

        Device device = this.buildDevice(null, savedLayout, "Device", "101.0.0.1", null);
        Device savedDevice = this.deviceRepository.save(device);

        DeviceGroup deviceGroup = this.buildDeviceGroup("Device Group", "Description", null, List.of(device));

        // replace runtime error with appropiate exception
        assertThrows(RuntimeException.class, () -> this.deviceGroupService.updateDeviceGroup(1L, deviceGroup));
    }

    // TODO
    // Write remaining tests for the update method


    // delete

    @Test
    @Transactional
    void shouldDeleteDeviceGroupById() {
        Layout layout = this.buildLayout("Layout", 1,1);
        Layout savedLayout = this.layoutRepository.saveAndFlush(layout);

        Device device = this.buildDevice(null, savedLayout, "Device", "101.0.0.1", null);
        Device savedDevice = this.deviceRepository.save(device);


        DeviceGroup deviceGroup1 = this.buildDeviceGroup("Device Group 1", "Description 1", null, List.of(device));
        DeviceGroup savedDeviceGroup1 = this.deviceGroupService.createDeviceGroup(deviceGroup1);

        DeviceGroup deviceGroup2 = this.buildDeviceGroup("Device Group 2", "Description 2", savedLayout, new ArrayList<>());
        DeviceGroup savedDeviceGroup2 = this.deviceGroupService.createDeviceGroup(deviceGroup2);

        assertEquals(2, this.deviceGroupRepository.findAll().size());
        this.deviceGroupService.deleteDeviceGroup(deviceGroup1.getId());

        List<DeviceGroup> deviceGroups = this.deviceGroupRepository.findAll();

        assertEquals(1, deviceGroups.size());

        assertDeviceGroup(deviceGroups.get(0), savedDeviceGroup2.getName(), savedDeviceGroup2.getDescription(), savedDeviceGroup2.getLayout(), savedDeviceGroup2.getDevices());

        // should not delete assigned layouts
        assertEquals(1, this.layoutRepository.findAll().size());

        // should not delete assigned devices
        assertEquals(1, this.deviceRepository.findAll().size());
    }

    @Test
    void shouldDeleteAllDeviceGroups() {

        Layout layout = this.buildLayout("Layout", 1, 1);
        Layout savedLayout = this.layoutRepository.saveAndFlush(layout);

        Device device = this.buildDevice(null, savedLayout, "Device", "101.0.0.1", null);
        Device savedDevice = this.deviceRepository.save(device);

        DeviceGroup deviceGroup1 = this.buildDeviceGroup("Device Group 1", "Description 1", null, List.of(device));
        DeviceGroup savedDeviceGroup1 = this.deviceGroupService.createDeviceGroup(deviceGroup1);

        DeviceGroup deviceGroup2 = this.buildDeviceGroup("Device Group 2", "Description 2", savedLayout, new ArrayList<>());
        DeviceGroup savedDeviceGroup2 = this.deviceGroupService.createDeviceGroup(deviceGroup2);

        assertEquals(2, this.deviceGroupRepository.findAll().size());
        this.deviceGroupService.deleteAllDeviceGroups();
        assertEquals(0, this.deviceGroupRepository.findAll().size());

        // should not delete assigned layouts
        assertEquals(1, this.layoutRepository.findAll().size());

        // should not delete assigned devices
        assertEquals(1, this.deviceRepository.findAll().size());
    }


}
