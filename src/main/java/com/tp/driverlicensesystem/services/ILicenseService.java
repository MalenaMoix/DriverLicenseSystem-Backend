package com.tp.driverlicensesystem.services;

import com.tp.driverlicensesystem.model.License;
import com.tp.driverlicensesystem.model.Owner;

import java.time.LocalDate;
import java.util.List;


public interface ILicenseService {
    LocalDate calculateLicenseTerm(Owner owner);
    String saveLicense(License license);
    License getLicenseById(Integer idLicense);
    List<License> getCurrentLicenses(Integer ownerId);
}
