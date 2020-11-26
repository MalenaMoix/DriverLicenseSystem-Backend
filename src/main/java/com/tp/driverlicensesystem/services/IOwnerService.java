package com.tp.driverlicensesystem.services;

import com.tp.driverlicensesystem.model.License;
import com.tp.driverlicensesystem.model.Owner;

import java.time.LocalDate;

public interface IOwnerService {
    Integer getOwnerAge(LocalDate dateOfBirthday);
    void saveOwner(Owner owner);
    Owner getOwnerById(Integer ownerId);
    Owner getOwnerByIdWithLicensesList(Integer ownerId);
}
