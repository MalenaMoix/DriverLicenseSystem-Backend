package com.tp.driverlicensesystem.services;

import com.tp.driverlicensesystem.model.License;
import com.tp.driverlicensesystem.model.Owner;
import com.tp.driverlicensesystem.repository.ILicenseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
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

        try {
            String licenseClass = license.licenseClass;
            Owner owner = iOwnerService.getOwnerByIdWithLicensesList(license.getLicenseOwner().getDocument());
            List<License> licensesList = owner.getLicensesList();
            Integer ownerAge = iOwnerService.getOwnerAge(owner.getBirthDate());
            boolean areClassesCDE = licenseClass.equals("C") || licenseClass.equals("D") || licenseClass.equals("E");

            //TODO REFACTORIZAR este FOR por un WHILE.
            for (License license1:licensesList){
                if (!license1.getIsRevoked() && license1.getLicenseTerm().isAfter(LocalDate.now()) && license1.getLicenseClass().equals(license.getLicenseClass())){
                    //Ya posee una licencia de esta clase.
                    return "forbidden";
                }
            }

            if (ownerAge < 17) {
                //return "El solicitante debe tener 17 años";
                return "forbidden";
            }
            if (areClassesCDE && ownerAge < 21) {
                //return "El solicitante debe tener 21 años";
                return "forbidden";
            }

            if (areClassesCDE) {

                LocalDate yearAgo = LocalDate.now().minusYears(1);
                boolean hasLicenseB = false;
                boolean hasLicenseC = false;
                ArrayList<License> licensesB = new ArrayList<>();
                License licenseC = null;

                if (licensesList == null || licensesList.isEmpty()) {
                    //return "El solicitante no posee una licencia de tipo B" o "El solicitante debe tener una licencia de tipo profesional";
                    return "forbidden";
                }

                if(ownerAge > 65) {
                    boolean hasProfessionalLicense = false;
                    for (License lic : licensesList) {
                        if (lic.getLicenseClass().equals("C") || lic.getLicenseClass().equals("D") || lic.getLicenseClass().equals("E")) {
                            hasProfessionalLicense = true;
                        }
                    }
                    if (!hasProfessionalLicense) {
                        //return "El solicitante debe tener una licencia de tipo profesional";
                        return "forbidden";
                    }
                }
                for (License lic: licensesList) {
                    if (lic.getLicenseClass().equals("B") ) {
                        hasLicenseB = true;
                        licensesB.add(lic);
                    }
                    if (lic.getLicenseClass().equals("C") && licenseC==null) {
                        hasLicenseC = true;
                        licenseC = lic;
                    }
                }
                if (!hasLicenseB) {
                    //return "El solicitante no posee una licencia de tipo B";
                    return "forbidden";
                }

                //licensesB.get(0) es la primer licencia "B" que tuvo el titular
                if (hasLicenseB && !licensesB.get(0).getLicenseStart().isBefore(yearAgo)) {
                    //return "El solicitante no posee una licencia de tipo B entregada hace mas de un año";
                    return "forbidden";
                }

                // Si una persona solicita una licencia tipo D o E y tiene licencia tipo B o C, el sistema revoca la licencia existente y se emite la nueva licencia tipo D o E.
                // Si una persona solicita una licencia tipo C y tiene licencia tipo B, el sistema revoca la licencia existente y se emite la nueva licencia tipo C.
                //licensesB.get(licensesB.size()-1) es la ultima licencia de tipo B que tiene el titular
                if (hasLicenseB && !licensesB.get(licensesB.size()-1).getIsRevoked() && !licensesB.get(licensesB.size()-1).getLicenseTerm().isAfter(LocalDate.now())) {
                    licensesB.get(licensesB.size()-1).setIsRevoked(true);
                    //Actualizar la licencia en el repositorio, con el atributo isRevoked modificado.
                    licenseRepo.save(licensesB.get(licensesB.size()-1));
                }
                if (hasLicenseC && !licenseC.getIsRevoked() && licenseC.getLicenseTerm().isAfter(LocalDate.now())) {
                    licenseC.setIsRevoked(true);
                    //Actualizar la licencia en el repositorio, con el atributo isRevoked modificado.
                    licenseRepo.save(licenseC);
                }
            }

            if (licenseClass.equals("B") || licenseClass.equals("C")) {
                boolean hasOtherLicense = false;
                for (License lic: licensesList) {
                    if (!lic.getIsRevoked() && lic.getLicenseTerm().isAfter(LocalDate.now()) && (lic.getLicenseClass().equals("D") || lic.getLicenseClass().equals("E"))) {
                        hasOtherLicense = true;
                    }
                }
                if (hasOtherLicense) {
                    //return "El solicitante no debe tener una licencia de tipo D o de tipo E";
                    return "forbidden";
                }
            }
            if (licenseClass.equals("B")) {
                boolean hasLicenseC = false;
                for (License lic: licensesList) {
                    if (lic.getLicenseClass().equals("C") && !lic.getIsRevoked() && lic.getLicenseTerm().isAfter(LocalDate.now())) {
                        hasLicenseC = true;
                    }
                }
                if (hasLicenseC) {
                    //return "El solicitante ya tiene una licencia de tipo C";
                    return "forbidden";
                }
            }

            license.setLicenseClass(licenseClass);
            license.setLicenseOwner(owner);
            LocalDate newLicenseDate = LocalDate.now();
            license.setLicenseStart(newLicenseDate);
            LocalDate licenseTerm = calculateLicenseTerm(owner);
            license.setLicenseTerm(licenseTerm);
            license.setIsRevoked(false);
            System.out.println(license.toString());

            try {
                licenseRepo.save(license);
                return "success";
            } catch (Exception e) {
                System.out.println("Viene a este catch");
                return "No se ha podido guardar la licencia, intente nuevamente";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "No se ha podido guardar la licencia, intente nuevamente";
        }

    }

}
