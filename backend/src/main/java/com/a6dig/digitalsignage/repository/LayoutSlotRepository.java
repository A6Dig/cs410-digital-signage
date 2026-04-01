package com.a6dig.digitalsignage.repository;

import com.a6dig.digitalsignage.entity.LayoutSlot;
import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD
import org.springframework.data.jpa.repository.Query;
=======
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
>>>>>>> 28ec52b383dd2e358d2e5711391f9e0f9f3feb92

import java.util.List;

public interface LayoutSlotRepository extends JpaRepository<LayoutSlot, Long> {

    List<LayoutSlot> getAllLayoutSlotsByLayoutId(Long layoutId);

<<<<<<< HEAD
=======
    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM LayoutSlot ls WHERE ls.layout.id = :layoutId")
    void deleteAllLayoutSlotsByLayoutId(Long layoutId);

>>>>>>> 28ec52b383dd2e358d2e5711391f9e0f9f3feb92
}
