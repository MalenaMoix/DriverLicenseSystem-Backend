package com.tp.driverlicensesystem;

import com.tp.driverlicensesystem.model.License;
import com.tp.driverlicensesystem.model.Owner;
import com.tp.driverlicensesystem.services.ILicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class DriverLicenseSystemApplication{

    @Autowired
    private ILicenseService iLicenseService;
    public static void main(String[] args) {
        SpringApplication.run(DriverLicenseSystemApplication.class, args);
    }
}
