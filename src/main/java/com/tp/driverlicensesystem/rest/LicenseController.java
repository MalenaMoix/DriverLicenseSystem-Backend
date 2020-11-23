package com.tp.driverlicensesystem.rest;

import com.tp.driverlicensesystem.model.License;
import com.tp.driverlicensesystem.model.Owner;
import com.tp.driverlicensesystem.services.ILicenseService;
import com.tp.driverlicensesystem.services.IOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/license")
@CrossOrigin("*")
public class LicenseController {
    @Autowired
    private ILicenseService iLicenseService;

    @Autowired
    private IOwnerService iOwnerService;

    @PostMapping
    public ResponseEntity<Object> postLicense(@RequestBody License license){
        String message = iLicenseService.saveLicense(license);
        switch(message){
            case "success":
                return new ResponseEntity<>(HttpStatus.OK);
            case "forbidden":
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            default:
                return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{id}/{licenseClass}")
    public License getCostAndValidUntil(@PathVariable("id") Integer document, @PathVariable("licenseClass") String licenseClass){
        try {
            Owner owner = new Owner();
            owner = iOwnerService.getOwnerById(document);
            Integer ownerAge = iOwnerService.getOwnerAge(owner.getBirthDate());

            System.out.println(document);
            License license = new License();
            license.setLicenseClass(licenseClass);
            license.setLicenseTerm(iLicenseService.calculateLicenseTerm(document, license, ownerAge, owner));

            //TODO hacer el metodo para calcular el costo
            license.setLicenseCost(42.50);
            return license;
        } catch (Exception e) {
            return null;
        }
    }
}
