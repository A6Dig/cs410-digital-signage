package com.a6dig.digitalsignage.repository;

import com.a6dig.digitalsignage.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
<<<<<<< HEAD
public interface DeviceRepository extends JpaRepository<Device, String> {

    List<Device> findByGroup(String deviceGroup);
}
=======
public interface DeviceRepository extends JpaRepository<Device, Long> {

    List<Device> findByDeviceGroupId(Long deviceGroupId);

}
>>>>>>> 28ec52b383dd2e358d2e5711391f9e0f9f3feb92
