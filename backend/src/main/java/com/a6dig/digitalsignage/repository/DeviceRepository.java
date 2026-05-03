package com.a6dig.digitalsignage.repository;

import com.a6dig.digitalsignage.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    List<Device> findByDeviceGroupId(Long deviceGroupId);
    Optional<Device> findByPairingId(Long pairingId);
    boolean existsByPairingId(Long pairingId);

}
