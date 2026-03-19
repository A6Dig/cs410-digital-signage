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

    // Endpoint for receivers to check-in/register [cite: 52]
    @PostMapping("/register")
    public Device registerDevice(@RequestBody Device device) {
        device.setLastConnected(LocalDateTime.now());
        // Simple registration: assume all receivers are legitimate 
        return deviceRepository.save(device);
    }
    
}