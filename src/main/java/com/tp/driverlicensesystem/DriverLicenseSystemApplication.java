package com.tp.driverlicensesystem;

import com.tp.driverlicensesystem.model.Owner;
import com.tp.driverlicensesystem.services.ILicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class DriverLicenseSystemApplication implements CommandLineRunner {

    @Autowired
    private ILicenseService iLicenseService;
    public static void main(String[] args) {
        SpringApplication.run(DriverLicenseSystemApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception{

        Owner owner = new Owner();
        owner.setDocument(40905305);
        //iLicenseService.calculateLicenseTerm(owner.getDocument());
    }

}
