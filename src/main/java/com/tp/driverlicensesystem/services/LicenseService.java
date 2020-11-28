package com.tp.driverlicensesystem.services;

import com.tp.driverlicensesystem.model.License;
import com.tp.driverlicensesystem.model.Owner;
import com.tp.driverlicensesystem.repository.ILicenseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
public class LicenseService implements ILicenseService{

    @Autowired
    private ILicenseRepo licenseRepo;

    @Autowired
    private IOwnerService iOwnerService;

    @Override
    public LocalDate calculateLicenseTerm(Owner owner) {

        int ownerAge = iOwnerService.getOwnerAge(owner.getBirthDate());
        int add;
        LocalDate licenseTerm = LocalDate.of(LocalDate.now().getYear(),owner.getBirthDate().getMonthValue(), owner.getBirthDate().getDayOfMonth());

        if (ownerAge<21){
            if (owner.getLicensesList().isEmpty()){
                //Dar por un año
                add = 1;
            }
            else {
                //Dar por tres años
                add = 3;
            }
        }
        else {
            if (ownerAge<=46){
                add = 5;
            }
            else {
                if(ownerAge<=60){
                    add = 4;
                }
                else{
                    if(ownerAge<=70){
                        add = 3;
                    }
                    else {
                        // Mayores de 70 años
                        add = 1;
                    }
                }
            }
        }
        LocalDate licenseTermReturn = licenseTerm.plusYears(add);

        return licenseTermReturn;
    }

    @Override
    public String saveLicense(License license) {
        // TODO Ver por que el save esta dando error

        String licenseClass = license.licenseClass;
        String stringReturn = "success";

        try {
            System.out.println(license.toString());
            Owner owner = iOwnerService.getOwnerByIdWithLicensesList(license.getLicenseOwner().getDocument());
            List<License> licensesList = owner.getLicensesList();
            Integer ownerAge = iOwnerService.getOwnerAge(owner.getBirthDate());
            boolean areClassesCDE = licenseClass.equals("C") || licenseClass.equals("D") || licenseClass.equals("E");

            //TODO REFACTORIZAR este FOR por un WHILE.
           for (License license1:licensesList){
                if (!license1.getIsRevoked() && license1.getLicenseClass().equals(license.getLicenseClass())){
                    stringReturn = "forbidden";
                }
           }
            if (ownerAge < 17) {
                //return "El solicitante debe tener 17 años";
                stringReturn = "forbidden";
            }else{
                if (areClassesCDE && ownerAge < 21) {
                    //return "El solicitante debe tener 21 años";
                    stringReturn = "forbidden";
                }
            }
            if (licenseClass.equals("D") || licenseClass.equals("E")) {

            }
            if (areClassesCDE) {
                LocalDate yearAgo = LocalDate.now().minusYears(1);
                boolean hasLicenseB = false;
                boolean hasLicenseC = false;
                License licenseB = null;
                License licenseC = null;

                if (licensesList == null || licensesList.isEmpty()) {
                    //return "El solicitante no posee una licencia de tipo B";
                    stringReturn = "forbidden";
                }
                for (License lic: licensesList) {
                    if (lic.getLicenseClass().equals("B")) {
                        hasLicenseB = true;
                        licenseB = lic;
                    }
                    if (lic.getLicenseClass().equals("C")) {
                        hasLicenseC = true;
                        licenseC = lic;
                    }
                }
                if (!hasLicenseB) {
                    //return "El solicitante no posee una licencia de tipo B";
                    stringReturn = "forbidden";
                }

                if (hasLicenseB && !licenseB.getLicenseStart().isAfter(yearAgo)) {
                    //return "El solicitante no posee una licencia de tipo B entregado en el ultimo año";
                    stringReturn = "forbidden";
                }

                // Si una persona solicita una licencia tipo D o E y tiene licencia tipo B o C, el sistema revoca la licencia existente y se emite la nueva licencia tipo D o E.
                // Si una persona solicita una licencia tipo C y tiene licencia tipo B, el sistema revoca la licencia existente y se emite la nueva licencia tipo C.
                if (hasLicenseB) {
                    licenseB.setIsRevoked(true);
                }
                if (hasLicenseC) {
                    licenseC.setIsRevoked(true);
                }
            }
            if (ownerAge > 65 && areClassesCDE) {
                if (licensesList == null) {
                    //return "El solicitante debe tener una licencia de tipo profesional";
                    stringReturn = "forbidden";
                }
                if (licensesList.size() == 0) {
                    //return "El solicitante debe tener una licencia de tipo profesional";
                    stringReturn = "forbidden";
                }

                boolean hasProfesionalLicense = false;
                for (License lic: licensesList) {
                    if (lic.getLicenseClass().equals("C") || lic.getLicenseClass().equals("D") || lic.getLicenseClass().equals("D")) {
                        hasProfesionalLicense = true;
                    }
                }
                if (!hasProfesionalLicense) {
                    //return "El solicitante debe tener una licencia de tipo profesional";
                    stringReturn = "forbidden";
                }
            }
            if (licenseClass.equals("B") || licenseClass.equals("C")) {
                boolean hasOtherLicense = false;
                for (License lic: licensesList) {
                    if (lic.getLicenseClass().equals("D") || lic.getLicenseClass().equals("E")) {
                        hasOtherLicense = true;
                    }
                }
                if (!hasOtherLicense) {
                    //return "El solicitante debe tener una licencia de tipo D o de tipo E";
                    stringReturn = "forbidden";
                }
            }
            if (licenseClass.equals("B")) {
                boolean hasLicenseC = false;
                for (License lic: licensesList) {
                    if (lic.getLicenseClass().equals("C")) {
                        hasLicenseC = true;
                    }
                }
                if (!hasLicenseC) {
                    //return "El solicitante debe tener una licencia de tipo C";
                    stringReturn = "forbidden";
                }
            }

            LocalDate newLicenseDate = LocalDate.now();
            license.setLicenseStart(newLicenseDate);

            System.out.println(license.toString());

            if(stringReturn.equals("success")){
                try {
                    licenseRepo.save(license);
                } catch (Exception e) {
                    System.out.println("Viene a este catch");
                    stringReturn = "No se ha podido guardar la licencia, intente nuevamente";
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            stringReturn = "No se ha podido guardar la licencia, intente nuevamente";
        }
        return stringReturn;
    }
}
