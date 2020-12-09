package com.tp.driverlicensesystem.repository;

import com.tp.driverlicensesystem.model.License;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ILicenseRepo extends JpaRepository <License, Integer>{
    @Query(value = "SELECT * FROM license l WHERE l.license_owner_document = ?1 AND l.is_revoked = false AND l.license_term >= ?2",nativeQuery = true)
    List<License> getCurrentLicenses(Integer ownerId, LocalDate today);
}
