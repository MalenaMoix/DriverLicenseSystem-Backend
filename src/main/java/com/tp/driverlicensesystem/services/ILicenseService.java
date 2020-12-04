package com.tp.driverlicensesystem.services;

import com.tp.driverlicensesystem.model.License;
import com.tp.driverlicensesystem.model.Owner;

import java.time.LocalDate;


public interface ILicenseService {
    LocalDate calculateLicenseTerm(Owner owner);
    Double calculateLicenseCost(String licenseClass);
    String saveLicense(License license);
}
