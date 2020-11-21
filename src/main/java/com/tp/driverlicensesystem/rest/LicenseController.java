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
    public void postLicense(@RequestBody License license){
        iLicenseService.saveLicense(license);
    }

    @GetMapping(value = "/{id}/{licenseClass}")
    public License getCostAndValidUntil(@PathVariable("id") Integer document, @PathVariable("licenseClass") String licenseClass){
        System.out.println(document);
        License license = new License();
        license.setLicenseClass(licenseClass);
        license.setLicenseTerm(iLicenseService.calculateLicenseTerm(document, license));

        //TODO hacer el metodo para calcular el costo
        license.setLicenseCost(42.50);
        System.out.println(license.toString());
        return license;
    }
}
