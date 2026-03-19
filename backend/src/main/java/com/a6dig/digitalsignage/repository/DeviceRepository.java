package com.a6dig.digitalsignage.repository;

import com.a6dig.digitalsignage.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, String> {

    List<Device> findByGroup(String deviceGroup);
}