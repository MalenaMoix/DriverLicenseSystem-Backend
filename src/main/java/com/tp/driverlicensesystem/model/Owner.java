package com.tp.driverlicensesystem.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
//Deberia etiquetarse con @Entity
public class Owner {

    //Ver si algun tipo de dato es distinto.

    @Id
    private Integer document;
    @Column(length = 50)
    private String name;
    @Column(length = 50)
    private String lastName;
    @Column
    private LocalDate birthDate;
    @Column(length = 50)
    private String address;
    @Column(length = 50)
    private String observations;
    @Column
    private String bloodType;
    @Column
    private String rhFactor;
    @Column
    private Boolean isDonor;
    @OneToMany(mappedBy = "licenseOwner", fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<License> licensesList;

    public Owner() {
        licensesList = new ArrayList<>();
    }

    public Integer getDocument() {
        return document;
    }

    public void setDocument(Integer document) {
        this.document = document;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate dateOfBirthday) {
        this.birthDate = dateOfBirthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getRhFactor() {
        return rhFactor;
    }

    public void setRhFactor(String rhFactor) {
        this.rhFactor = rhFactor;
    }

    public Boolean getDonor() {
        return isDonor;
    }

    public void setDonor(Boolean donor) {
        isDonor = donor;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public List<License> getLicensesList() {
        return licensesList;
    }

    public void setLicensesList(ArrayList<License> licensesList) {
        this.licensesList = licensesList;
    }

    public void addLicense(License license){
        licensesList.add(license);}
}

