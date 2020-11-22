package com.tp.driverlicensesystem.rest;

import com.tp.driverlicensesystem.model.Owner;
import com.tp.driverlicensesystem.services.IOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/owner")
@CrossOrigin("*")
public class OwnerController {

    @Autowired
    private IOwnerService iOwnerService;

    @GetMapping(value = "/{id}")
    public Owner getOwnerById(@PathVariable("id") Integer ownerId){
        return iOwnerService.getOwnerById(ownerId);
    }

    @PostMapping
    public ResponseEntity<Object> saveOwner(@RequestBody Owner owner){
        String message = iOwnerService.saveOwner(owner);

        switch(message){
            case "exito":
                return new ResponseEntity<>(HttpStatus.OK);
            default:
                return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
    }
}