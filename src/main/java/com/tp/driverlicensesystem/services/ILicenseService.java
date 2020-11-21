package com.tp.driverlicensesystem.services;

import com.tp.driverlicensesystem.model.License;
import com.tp.driverlicensesystem.model.Owner;

import java.time.LocalDate;


public interface ILicenseService {
    LocalDate calculateLicenseTerm(Integer ownerId, License license, Integer ownerAge, Owner owner);
    String saveLicense(License license);
}
