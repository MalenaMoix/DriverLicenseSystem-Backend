package com.tp.driverlicensesystem.model;

import java.time.LocalDate;
import java.util.ArrayList;

//Deberia etiquetarse con @Entity
public class Owner {

    //Ver si algun tipo de dato es distinto.
    private String typeOfDocument; //Me parece que solo ibamos a aceptar DNI.
    private Integer document;
    private String name;
    private String lastName;
    private LocalDate dateOfBirthday;
    private String address;
    private String bloodType;
    private String rhFactor;
    private Boolean isDonor;


    private ArrayList<License> licensesList;

    public Owner() {
        licensesList = new ArrayList<>();
    }

    public String getTypeOfDocument() {
        return typeOfDocument;
    }

    public void setTypeOfDocument(String typeOfDocument) {
        this.typeOfDocument = typeOfDocument;
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

    public LocalDate getDateOfBirthday() {
        return dateOfBirthday;
    }

    public void setDateOfBirthday(LocalDate dateOfBirthday) {
        this.dateOfBirthday = dateOfBirthday;
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

    public ArrayList<License> getLicensesList() {
        return licensesList;
    }

    public void setLicensesList(ArrayList<License> licensesList) {
        this.licensesList = licensesList;
    }

    public void addLicense(License license){
        licensesList.add(license);
    }
}
