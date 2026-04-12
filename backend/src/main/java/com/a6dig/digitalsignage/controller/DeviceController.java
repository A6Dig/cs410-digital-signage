package com.a6dig.digitalsignage.controller;

<<<<<<< HEAD
import com.a6dig.digitalsignage.entity.Device;
import com.a6dig.digitalsignage.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
=======
import com.a6dig.digitalsignage.constant.AppConstant;
import com.a6dig.digitalsignage.entity.Device;
import com.a6dig.digitalsignage.service.DeviceService;
import com.a6dig.digitalsignage.util.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

>>>>>>> 28ec52b383dd2e358d2e5711391f9e0f9f3feb92
import java.util.List;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    @Autowired
<<<<<<< HEAD
    private DeviceRepository deviceRepository;

    // Endpoint for receivers to check-in/register [cite: 52]
    @PostMapping("/register")
    public Device registerDevice(@RequestBody Device device) {
        device.setLastConnected(LocalDateTime.now());
        // Simple registration: assume all receivers are legitimate 
        return deviceRepository.save(device);
    }
    
}
=======
    private DeviceService deviceService;

    // Reads
    
    @GetMapping("")
    public ResponseEntity<APIResponse<List<Device>>> getAllDevices() {
        return ResponseEntity.ok(APIResponse.success(deviceService.getAllDevices()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<Device>> getDeviceById(@PathVariable Long id) {
        return ResponseEntity.ok(APIResponse.success(deviceService.getDeviceById(id)));
    }

    @GetMapping("/group/{deviceGroupId}")
    public ResponseEntity<APIResponse<List<Device>>> getDevicesByDeviceGroupId(@PathVariable Long deviceGroupId) {
        return ResponseEntity.ok(APIResponse.success(deviceService.getDevicesByDeviceGroupId(deviceGroupId)));
    }

    // Creates

    @PostMapping("")
    public ResponseEntity<APIResponse<Device>> createDevice(@RequestBody Device device) {
        Device createdDevice = deviceService.createDevice(device);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(APIResponse.created(AppConstant.SuccessMessage.Device.CREATED, createdDevice));
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<Device>> updateDevice(@PathVariable Long id, @RequestBody Device device) {
        Device updatedDevice = deviceService.updateDevice(id, device);
        return ResponseEntity.ok(APIResponse.success(AppConstant.SuccessMessage.Device.UPDATED, updatedDevice));
    }

    // Deletes

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> deleteDevice(@PathVariable Long id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.ok(APIResponse.success(AppConstant.SuccessMessage.Device.DELETED));
    }

    @DeleteMapping("")
    public ResponseEntity<APIResponse<Void>> deleteAllDevices() {
        deviceService.deleteAllDevices();
        return ResponseEntity.ok(APIResponse.success(AppConstant.SuccessMessage.Device.DELETED_ALL));
    }
}
>>>>>>> 28ec52b383dd2e358d2e5711391f9e0f9f3feb92
