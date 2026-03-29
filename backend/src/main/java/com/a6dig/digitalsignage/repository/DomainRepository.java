package com.a6dig.digitalsignage.repository;

import com.a6dig.digitalsignage.entity.Domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DomainRepository extends JpaRepository<Domain, Long> {

    @Query("SELECT d.alphaNumCode FROM Domain d WHERE d.type = :type")
    List<String> findValuesByType(@Param("type") String type);

    @Query("SELECT d FROM Domain d WHERE d.type = :type")
    List<Domain> findDomainByType(@Param("type") String type);

    @Query("SELECT d FROM Domain d WHERE d.alphaNumCode = :code")
    Domain findDomainByAlphaNumCode(@Param("code") String code);
}
