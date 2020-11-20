package com.tp.driverlicensesystem.rest;

import com.tp.driverlicensesystem.model.License;
import com.tp.driverlicensesystem.model.Owner;
import com.tp.driverlicensesystem.services.ILicenseService;
import com.tp.driverlicensesystem.services.IOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/license")
@CrossOrigin("*")
public class LicenseController {
    @Autowired
    private ILicenseService iLicenseService;

    @PostMapping
    public void postLicense(License license){
        iLicenseService.saveLicense(license);
    }

}
