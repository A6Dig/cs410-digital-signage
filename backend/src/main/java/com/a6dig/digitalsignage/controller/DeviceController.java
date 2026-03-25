package com.a6dig.digitalsignage.controller;

import com.a6dig.digitalsignage.entity.Device;
import com.a6dig.digitalsignage.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    @Autowired
    private DeviceRepository deviceRepository;

    @GetMapping
    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    @GetMapping("/{id}")
    public Device getDeviceById(@PathVariable Integer id) {
        return deviceRepository.findById(id).orElse(null);
    }

    
}