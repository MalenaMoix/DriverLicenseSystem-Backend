package com.tp.driverlicensesystem.services;

import com.tp.driverlicensesystem.model.License;

import java.util.Calendar;
import java.util.Date;

public interface ILicenseService {
    Calendar calculateLicenseTerm(License license);
}
