package com.a6dig.digitalsignage.controller;

import com.a6dig.digitalsignage.constant.AppConstant;
import com.a6dig.digitalsignage.dto.DeviceGroupAssignRequestDto;
import com.a6dig.digitalsignage.dto.DevicePairRequestDto;
import com.a6dig.digitalsignage.dto.DevicePairResponseDto;
import com.a6dig.digitalsignage.dto.DeviceRegistrationResponseDto;
import com.a6dig.digitalsignage.dto.DeviceResponseDto;
import com.a6dig.digitalsignage.dto.DeviceStatusUpdateRequestDto;
import com.a6dig.digitalsignage.dto.DeviceVerifyRegistrationRequestDto;
import com.a6dig.digitalsignage.entity.Device;
import com.a6dig.digitalsignage.service.DeviceService;
import com.a6dig.digitalsignage.util.APIResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @GetMapping("/register")
    public ResponseEntity<APIResponse<DeviceRegistrationResponseDto>> registerDevice(HttpServletRequest request) {
        DeviceRegistrationResponseDto responseDto = deviceService.registerDevice(request.getRemoteAddr());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.created(AppConstant.SuccessMessage.Device.REGISTERED, responseDto));
    }

    @PostMapping("/verify-register")
    public ResponseEntity<APIResponse<DevicePairResponseDto>> verifyRegistration(
            @RequestBody DeviceVerifyRegistrationRequestDto requestDto) {
        DevicePairResponseDto responseDto = deviceService.verifyRegistration(requestDto);
        String message = Boolean.TRUE.equals(responseDto.getPairing().getPaired())
                ? AppConstant.SuccessMessage.Device.ALREADY_PAIRED
                : AppConstant.SuccessMessage.Device.PENDING_PAIRING;
        return ResponseEntity.ok(APIResponse.success(message, responseDto));
    }

    @GetMapping("")
    public ResponseEntity<APIResponse<List<DeviceResponseDto>>> getAllDevices() {
        return ResponseEntity.ok(APIResponse.success(deviceService.getAllDevices()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<DeviceResponseDto>> getDeviceById(@PathVariable Long id) {
        return ResponseEntity.ok(APIResponse.success(deviceService.getDeviceById(id)));
    }

    @GetMapping("/group/{deviceGroupId}")
    public ResponseEntity<APIResponse<List<DeviceResponseDto>>> getDevicesByDeviceGroupId(@PathVariable Long deviceGroupId) {
        return ResponseEntity.ok(APIResponse.success(deviceService.getDevicesByDeviceGroupId(deviceGroupId)));
    }

    @PostMapping("")
    public ResponseEntity<APIResponse<DeviceResponseDto>> createDevice(@RequestBody Device device) {
        DeviceResponseDto createdDevice = deviceService.createDevice(device);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(APIResponse.created(AppConstant.SuccessMessage.Device.CREATED, createdDevice));
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<DeviceResponseDto>> updateDevice(@PathVariable Long id, @RequestBody Device device) {
        DeviceResponseDto updatedDevice = deviceService.updateDevice(id, device);
        return ResponseEntity.ok(APIResponse.success(AppConstant.SuccessMessage.Device.UPDATED, updatedDevice));
    }

    @PatchMapping("/{id}/pair")
    public ResponseEntity<APIResponse<DevicePairResponseDto>> pairDevice(
            @PathVariable Long id,
            @RequestBody DevicePairRequestDto requestDto) {
        DevicePairResponseDto responseDto = deviceService.pairDevice(id, requestDto);
        return ResponseEntity.ok(APIResponse.success(AppConstant.SuccessMessage.Device.PAIRED, responseDto));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<APIResponse<DeviceResponseDto>> updateDeviceStatus(
            @PathVariable Long id,
            @RequestBody DeviceStatusUpdateRequestDto requestDto) {
        DeviceResponseDto responseDto = deviceService.updateDeviceStatus(id, requestDto);
        return ResponseEntity.ok(APIResponse.success(AppConstant.SuccessMessage.Device.STATUS_UPDATED, responseDto));
    }

    @PatchMapping("/{id}/group")
    public ResponseEntity<APIResponse<DeviceResponseDto>> assignDeviceGroup(
            @PathVariable Long id,
            @RequestBody DeviceGroupAssignRequestDto requestDto) {
        DeviceResponseDto responseDto = deviceService.assignDeviceGroup(id, requestDto);
        return ResponseEntity.ok(APIResponse.success(AppConstant.SuccessMessage.Device.GROUP_UPDATED, responseDto));
    }

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
