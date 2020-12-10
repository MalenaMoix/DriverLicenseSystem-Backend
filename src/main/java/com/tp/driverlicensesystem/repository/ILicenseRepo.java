package com.tp.driverlicensesystem.repository;

import com.tp.driverlicensesystem.model.License;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ILicenseRepo extends JpaRepository <License, Integer>{

    //Query a la base de datos para poder traer la lista de licencias expiradas, es decir, aquellas cuyas fechas de expiracion sean menor
    // a la fecha actual, o que hayan sido revocadas.
    @Query(value = "select * from License l where l.license_term < ?1 or l.is_revoked=true order by l.license_start asc", nativeQuery = true)
    List<License> getExpiredLicenses(LocalDate today);

    @Query(value = "SELECT * FROM license l WHERE l.license_owner_document = ?1 AND l.is_revoked = false AND l.license_term >= ?2",nativeQuery = true)
    List<License> getCurrentLicenses(Integer ownerId, LocalDate today);

}
