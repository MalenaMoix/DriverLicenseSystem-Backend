package com.tp.driverlicensesystem.model;

import java.time.LocalDate;

//Habria que etiquetarlo como ENTITY
public class License {

    //Necesitamos una fecha de inicio de vigencia? La task de fecha de vencimiento lo dice.

    private LocalDate licenseTerm;
    private Owner licenseOwner;

    public License (){}

    public LocalDate getLicenseTerm() {
        return licenseTerm;
    }

    public void setLicenseTerm(LocalDate licenseTerm) {
        this.licenseTerm = licenseTerm;
    }

    public Owner getLicenseOwner() {
        return licenseOwner;
    }

    public void setLicenseOwner(Owner licenseOwner) {
        this.licenseOwner = licenseOwner;
    }
}
