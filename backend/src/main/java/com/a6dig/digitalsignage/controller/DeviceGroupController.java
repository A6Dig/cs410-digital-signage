package com.a6dig.digitalsignage.controller;

import com.a6dig.digitalsignage.constant.AppConstant;
import com.a6dig.digitalsignage.entity.DeviceGroup;
import com.a6dig.digitalsignage.service.DeviceGroupService;
import com.a6dig.digitalsignage.util.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/device-groups")
public class DeviceGroupController {

    @Autowired
    private DeviceGroupService deviceGroupService;

    @GetMapping("")
    public ResponseEntity<APIResponse<List<DeviceGroup>>> getAllDeviceGroups() {
        return ResponseEntity.ok(APIResponse.success(deviceGroupService.getAllDeviceGroups()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<DeviceGroup>> getDeviceGroupById(@PathVariable Long id) {
        DeviceGroup deviceGroup = deviceGroupService.getDeviceGroupById(id);
        if (deviceGroup == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(APIResponse.notFound(AppConstant.ExceptionMessage.DeviceGroup.NOT_FOUND));
        }

        return ResponseEntity.ok(APIResponse.success(deviceGroup));
    }

    @PostMapping("")
    public ResponseEntity<APIResponse<DeviceGroup>> createDeviceGroup(@RequestBody DeviceGroup deviceGroup) {
        DeviceGroup createdDeviceGroup = deviceGroupService.createDeviceGroup(deviceGroup);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.created(AppConstant.SuccessMessage.DeviceGroup.CREATED, createdDeviceGroup));
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<DeviceGroup>> updateDeviceGroup(@PathVariable Long id, @RequestBody DeviceGroup deviceGroup) {
        DeviceGroup updatedDeviceGroup = deviceGroupService.updateDeviceGroup(id, deviceGroup);
        if (updatedDeviceGroup == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(APIResponse.notFound(AppConstant.ExceptionMessage.DeviceGroup.NOT_FOUND));
        }

        return ResponseEntity.ok(APIResponse.success(AppConstant.SuccessMessage.DeviceGroup.UPDATED, updatedDeviceGroup));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> deleteDeviceGroup(@PathVariable Long id) {
        boolean deleted = deviceGroupService.deleteDeviceGroup(id);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(APIResponse.notFound(AppConstant.ExceptionMessage.DeviceGroup.NOT_FOUND));
        }

        return ResponseEntity.ok(APIResponse.success(AppConstant.SuccessMessage.DeviceGroup.DELETED));
    }

    @DeleteMapping("")
    public ResponseEntity<APIResponse<Void>> deleteAllDeviceGroups() {
        deviceGroupService.deleteAllDeviceGroups();
        return ResponseEntity.ok(APIResponse.success(AppConstant.SuccessMessage.DeviceGroup.DELETED_ALL));
    }
}
