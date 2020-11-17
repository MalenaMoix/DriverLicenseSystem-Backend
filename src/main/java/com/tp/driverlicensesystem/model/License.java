package com.tp.driverlicensesystem.model;

import java.util.Calendar;

//Habria que etiquetarlo como ENTITY
public class License {

    //Necesitamos una fecha de inicio de vigencia? La task de fecha de vencimiento lo dice.

    private Calendar licenseTerm;
    private Owner licenseOwner;

    public License (){}

    public Calendar getLicenseTerm() {
        return licenseTerm;
    }

    public void setLicenseTerm(Calendar licenseTerm) {
        this.licenseTerm = licenseTerm;
    }

    public Owner getLicenseOwner() {
        return licenseOwner;
    }

    public void setLicenseOwner(Owner licenseOwner) {
        this.licenseOwner = licenseOwner;
    }
}
