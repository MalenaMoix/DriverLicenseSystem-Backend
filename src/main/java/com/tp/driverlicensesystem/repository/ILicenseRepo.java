package com.tp.driverlicensesystem.repository;

import com.tp.driverlicensesystem.model.License;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ILicenseRepo extends JpaRepository <License, Integer>{

}
