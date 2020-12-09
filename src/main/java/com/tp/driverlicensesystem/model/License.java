package com.tp.driverlicensesystem.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class License {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idLicense;
    @Column
    private LocalDate licenseTerm;
    @Column
    public String licenseClass;
    @Column
    private Double licenseCost;
    @Column (length = 50)
    public String observations;
    @Column
    private LocalDate licenseStart;
    @Column
    private boolean isRevoked;
    @ManyToOne(fetch = FetchType.EAGER)
    public Owner licenseOwner;


    public License (){}

    public Double getLicenseCost() {
        return licenseCost;
    }

    public void setLicenseCost(Double licenseCost) {
        this.licenseCost = licenseCost;
    }

    public Integer getIdLicense() {
        return idLicense;
    }

    public void setIdLicense(Integer idLicense) {
        this.idLicense = idLicense;
    }

    public String getLicenseClass() {
        return licenseClass;
    }

    public void setLicenseClass(String licenseClass) {
        this.licenseClass = licenseClass;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public LocalDate getLicenseStart() {
        return licenseStart;
    }

    public void setLicenseStart(LocalDate licenseStart) {
        this.licenseStart = licenseStart;
    }

    public LocalDate getLicenseTerm() {
        return licenseTerm;
    }

    public void setLicenseTerm(LocalDate licenseTerm) {
        this.licenseTerm = licenseTerm;
    }

    public boolean getIsRevoked() {
        return isRevoked;
    }

    public void setIsRevoked(boolean isRevoked) {
        this.isRevoked = isRevoked;
    }


    @ManyToOne
    public Owner getLicenseOwner() {
        return licenseOwner;
    }

    public void setLicenseOwner(Owner licenseOwner) {
        this.licenseOwner = licenseOwner;
    }

    @Override
    public String toString() {
        return "License{" +
                "idLicense=" + idLicense +
                ", licenseTerm=" + licenseTerm +
                ", licenseClass='" + licenseClass + '\'' +
                ", licenseCost=" + licenseCost +
                ", observations='" + observations + '\'' +
                ", licenseStart=" + licenseStart +
                ", licenseOwner=" + licenseOwner.getDocument() +
                ", licenseOwner=" + licenseOwner.getName() +
                '}';
    }
}

