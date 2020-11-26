package com.tp.driverlicensesystem.repository;

import com.tp.driverlicensesystem.model.License;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILicenseRepo extends JpaRepository <License, Integer>{

}
