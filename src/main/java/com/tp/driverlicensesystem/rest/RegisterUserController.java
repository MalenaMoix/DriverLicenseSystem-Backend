package com.tp.driverlicensesystem.rest;

import com.tp.driverlicensesystem.model.Owner;
import com.tp.driverlicensesystem.services.ILicenseService;
import com.tp.driverlicensesystem.services.IOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registerUser")
@CrossOrigin("*")
public class RegisterUserController {
    @Autowired
    private IOwnerService iOwnerService;

    @PostMapping
    public void saveOwner(Owner owner){
        iOwnerService.saveOwner(owner);
    }
}
