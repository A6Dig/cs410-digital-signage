package com.a6dig.digitalsignage.repository;

import com.a6dig.digitalsignage.entity.LayoutSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LayoutSlotRepository extends JpaRepository<LayoutSlot, Long> {

    List<LayoutSlot> getAllLayoutSlotsByLayoutId(Long layoutId);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM LayoutSlot ls WHERE ls.layout.id = :layoutId")
    void deleteAllLayoutSlotsByLayoutId(Long layoutId);

}
