package com.tp.driverlicensesystem;

import com.tp.driverlicensesystem.model.License;
import com.tp.driverlicensesystem.services.ILicenseServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class DriverLicenseSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(DriverLicenseSystemApplication.class, args);
        ILicenseServiceImpl iLicenseService = new ILicenseServiceImpl();
        iLicenseService.calculateLicenseTerm(new License());
    }

}
