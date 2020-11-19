package com.tp.driverlicensesystem.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class License {

    //Necesitamos una fecha de inicio de vigencia? La task de fecha de vencimiento lo dice.

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idLicense;
    @Column
    private LocalDate licenseTerm;
    @Column
    private String licenceClass;
    @Column
    private Double licenseCost;
    @Column (length = 50)
    private String observations;
    @Column
    private LocalDate licenseStart;

    @ManyToOne(fetch = FetchType.LAZY)
    private Owner licenseOwner;

    public License (){}

    public LocalDate getLicenseTerm() {
        return licenseTerm;
    }

    public void setLicenseTerm(LocalDate licenseTerm) {
        this.licenseTerm = licenseTerm;
    }

//    public Owner getLicenseOwner() {
//        return licenseOwner;
//    }

//    public void setLicenseOwner(Owner licenseOwner) {
//        this.licenseOwner = licenseOwner;
//    }

    @ManyToOne
    public Owner getLicenseOwner() {
        return licenseOwner;
    }

    public void setLicenseOwner(Owner licenseOwner) {
        this.licenseOwner = licenseOwner;
    }

}
