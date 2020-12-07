package com.tp.driverlicensesystem.rest;

import com.tp.driverlicensesystem.model.Owner;
import com.tp.driverlicensesystem.services.ILicenseService;
import com.tp.driverlicensesystem.services.IOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/owner")
@CrossOrigin("*")
public class OwnerController {
    @Autowired
    private IOwnerService iOwnerService;

    @GetMapping(value = "/{id}")
    public Owner getOwnerById(@PathVariable("id") Integer ownerId){
        Owner owner = null;
        try{
            owner=iOwnerService.getOwnerByIdWithCurrentLicenses(ownerId);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return owner;
    }

    @PostMapping
    public void saveOwner(Owner owner){
        iOwnerService.saveOwner(owner);
    }
}
