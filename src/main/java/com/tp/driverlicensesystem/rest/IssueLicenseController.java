package com.tp.driverlicensesystem.rest;

import com.tp.driverlicensesystem.services.ILicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/issueLicence")
@CrossOrigin("*")
public class IssueLicenseController {
    @Autowired
    private ILicenseService iLicenseService;

    @GetMapping
    public void getOwnerById(){
        iLicenseService.getOwnerById();
    }
}
