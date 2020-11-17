package com.tp.driverlicensesystem.services;

import com.tp.driverlicensesystem.model.License;

import java.time.LocalDate;


public interface ILicenseService {
    LocalDate calculateLicenseTerm(License license);
}
