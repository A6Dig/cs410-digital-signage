package com.a6dig.digitalsignage.integration.service;


import com.a6dig.digitalsignage.entity.Device;
import com.a6dig.digitalsignage.entity.DeviceGroup;
import com.a6dig.digitalsignage.entity.Layout;
import com.a6dig.digitalsignage.repository.DeviceGroupRepository;
import com.a6dig.digitalsignage.repository.DeviceRepository;
import com.a6dig.digitalsignage.repository.LayoutRepository;
import com.a6dig.digitalsignage.service.DeviceServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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


    // helper
    private Device buildDevice(Long id
            , Long layoutId
            , Layout layout
            , String name
            , String ipAddress
            , Long deviceGroupId
            , DeviceGroup deviceGroup) {

        Device device = new Device();
        device.setLayoutId(layoutId);
        device.setLayout(layout);
        device.setName(name);
        device.setIpAddress(ipAddress);
        device.setDeviceGroupId(deviceGroupId);
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
        assertEquals(expectedLayoutId, device.getLayoutId());
        assertEquals(expectedLayout.map(Layout::getId).orElse(null)
                , device.getLayout() != null ? device.getLayout().getId() : null);
        assertEquals(expectedName, device.getName());
        assertEquals(expectedIpAddress, device.getIpAddress());
        assertEquals(expectedDeviceGroupId, device.getDeviceGroupId());
        assertEquals(expectedDeviceGroup.map(DeviceGroup::getId).orElse(null),
                device.getDeviceGroup() != null ?
                device.getDeviceGroupId() : null);
        assertNotNull(device.getCreatedAt());
        assertNotNull(device.getUpdatedAt());
    }


    @Test
    void shouldCreateDevice() {

        Layout layout = new Layout();
        layout.setName("New Layout");
        layout.setCols(1);
        layout.setRows(1);

//        Layout savedLayout = this.layoutRepository.save(layout);

        Device request = this.buildDevice(null,
                layout.getId()
                ,layout
                ,"New Device"
                ,"101.0.0.1"
                , null
                , null);


//        Device saved = this.deviceService.createDevice(request);
//        assertDevice(saved,1L, Optional.of(layout), "New Device", "101.0.0.1", null, Optional.empty());

    }

}
