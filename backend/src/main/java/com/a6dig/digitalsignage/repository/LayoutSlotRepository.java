package com.a6dig.digitalsignage.repository;

import com.a6dig.digitalsignage.entity.LayoutSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LayoutSlotRepository extends JpaRepository<LayoutSlot, Long> {

    List<LayoutSlot> getAllLayoutSlotsByLayoutId(Long layoutId);

}
