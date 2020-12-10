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

    private int licenseValidity;
    private final Double ADMINISTRATIVE_COST = 8.00;

    @Override
    public LocalDate calculateLicenseTerm(Owner owner) {

        //Obtener el titular desde la base de datos y calcularle su edad.
        int ownerAge = iOwnerService.getOwnerAge(owner.getBirthDate());

        //La fecha de vigencia tiene que tener como mes y dia lo mismo que el el cumpleaños del titular
        LocalDate licenseTerm = LocalDate.of(LocalDate.now().getYear(),owner.getBirthDate().getMonthValue(), owner.getBirthDate().getDayOfMonth());

        if (ownerAge<21){
            //El titular tiene menos de 21 años
            if (owner.getLicensesList().isEmpty()){

                //Dar licencia por un año
                licenseValidity = 1;
            }
            else {
                //Dar licencia por tres años
                licenseValidity = 3;
            }
        }
        else {
            //El titular tiene entre 21 y 46 años
            if (ownerAge<=46){
                //Dar licencia por 5 años
                licenseValidity = 5;
            }
            else {
                //El titular tiene entre 47 y 60 años
                if(ownerAge<=60){
                    //Dar licencia por 4 años
                    licenseValidity = 4;
                }
                else{
                    //El titular tiene entre 61 y 70 años
                    if(ownerAge<=70){
                        //Dar licencia por 3 años
                        licenseValidity = 3;
                    }
                    else {
                        // Mayores de 70 años, dar licencia por 1 años
                        licenseValidity = 1;
                    }
                }
            }
        }

        //A la fecha de cumpleaños del titular este año, le sumo la cantidad de años que le corresponde segun su edad actual.
        LocalDate licenseTermReturn = licenseTerm.plusYears(licenseValidity);

        return licenseTermReturn;
    }

    @Override
    public Double calculateLicenseCost(String licenseClass) {
        Double licenseCost = 0.00;

        //AÑOS DE VALIDEZ
        switch(licenseValidity) {
            case 1:
                //CLASE DE LICENCIA
                switch(licenseClass) {
                    case "A":
                    case "B":
                    case "G":
                        licenseCost = ADMINISTRATIVE_COST + 20.00;
                        break;
                    case "C":
                        licenseCost = ADMINISTRATIVE_COST + 23.00;
                        break;
                    default:
                        licenseCost = ADMINISTRATIVE_COST + 29.00;
                        break;
                }
                break;
            case 3:
                switch(licenseClass) {
                    case "A":
                    case "B":
                    case "G":
                        licenseCost = ADMINISTRATIVE_COST + 25.00;
                        break;
                    case "C":
                        licenseCost = ADMINISTRATIVE_COST + 30.00;
                        break;
                    default:
                        licenseCost = ADMINISTRATIVE_COST + 39.00;
                        break;
                }
                break;
            case 4:
                switch(licenseClass) {
                    case "A":
                    case "B":
                    case "G":
                        licenseCost = ADMINISTRATIVE_COST + 30.00;
                        break;
                    case "C":
                        licenseCost = ADMINISTRATIVE_COST + 35.00;
                        break;
                    default:
                        licenseCost = ADMINISTRATIVE_COST + 44.00;
                        break;
                }
                break;
            case 5:
                switch(licenseClass) {
                    case "A":
                    case "B":
                    case "G":
                        licenseCost = ADMINISTRATIVE_COST + 40.00;
                        break;
                    case "C":
                        licenseCost = ADMINISTRATIVE_COST + 47.00;
                        break;
                    default:
                        licenseCost = ADMINISTRATIVE_COST + 59.00;
                        break;
                }
                break;
        }
        return licenseCost;
    }

    @Override
    public String saveLicense(License license) {

        try {

            String licenseClass = license.licenseClass;

            //Obtener el titular con sus licencias desde la base de datos.
            Owner owner = iOwnerService.getOwnerById(license.getLicenseOwner().getDocument());
            List<License> licensesList = owner.getLicensesList();

            //Calculo de la edad del titular de la licencia.
            Integer ownerAge = iOwnerService.getOwnerAge(owner.getBirthDate());

            //Si la licencia es de tipo profesional, este booleano es true.
            boolean areClassesCDE = licenseClass.equals("C") || licenseClass.equals("D") || licenseClass.equals("E");

            //Verificar si el titular no posee una licencia vigente igual a la solicitada.
            for (License license1:licensesList){
                if (!license1.getIsRevoked() && license1.getLicenseTerm().isAfter(LocalDate.now()) && license1.getLicenseClass().equals(license.getLicenseClass())){
                    //Verificar si el titular no posee una licencia vigente igual a la solicitada.
                    if(license1.getLicenseTerm().isBefore(LocalDate.now().plusDays(45))){
                        //En caso de que el titular posea una licencia vigente que este por vencer,
                        // puede optar por trenovar la misma.
                        license1.setIsRevoked(true); //Se revoca la licencia que esta por expirar.
                        licenseRepo.save(license1);
                    }else{
                        return "forbidden"; //Sino, ya tiene una licencia vigente y no puede generar una igual.
                    }
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
                    //return "El solicitante no posee una licencia de tipo B" o
                    // "El solicitante debe tener una licencia de tipo profesional";
                    return "forbidden";
                }

                if(ownerAge > 65) {
                    boolean hasProfessionalLicense = false;
                    int i = 0;
                    while(i<licensesList.size() && !hasProfessionalLicense) {
                        if (licensesList.get(i).getLicenseClass().equals("C") || licensesList.get(i).getLicenseClass().equals("D") || licensesList.get(i).getLicenseClass().equals("E")) {
                            hasProfessionalLicense = true;
                        }
                        i++;
                    }
                    if (!hasProfessionalLicense) {
                        //return "El solicitante debe tener una licencia de tipo profesional" si ya tiene mas de 65 años
                        return "forbidden";
                    }
                }

                //Verificar si el solicitante tiene/tuvo una licencia clase B o clase C
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
                    //return "El solicitante no posee una licencia de tipo B" para poder soliciar una
                    //licencia C, D o E
                    return "forbidden";
                }

                //licensesB.get(0) es la primer licencia "B" que tuvo el titular
                if (hasLicenseB && !licensesB.get(0).getLicenseStart().isBefore(yearAgo)) {
                    //return "El solicitante no posee una licencia de tipo B entregada hace mas de un año";
                    return "forbidden";
                }

                // Si una persona solicita una licencia tipo D o E y tiene licencia tipo B o C,
                // el sistema revoca la licencia existente y se emite la nueva licencia tipo D o E.
                // Si una persona solicita una licencia tipo C y tiene licencia tipo B,
                // el sistema revoca la licencia existente y se emite la nueva licencia tipo C.

                //licensesB.get(licensesB.size()-1) es la ultima licencia de tipo B que tiene el titular
                if (hasLicenseB && !licensesB.get(licensesB.size()-1).getIsRevoked() &&
                        !licensesB.get(licensesB.size()-1).getLicenseTerm().isAfter(LocalDate.now())) {
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
                int i = 0;
                while(i<licensesList.size() && !hasOtherLicense){
                    if (!licensesList.get(i).getIsRevoked() && licensesList.get(i).getLicenseTerm().isAfter(LocalDate.now()) && (licensesList.get(i).getLicenseClass().equals("D")
                            || licensesList.get(i).getLicenseClass().equals("E"))) {
                        //El titular ya tiene una licencia D o E en vigencia.
                        hasOtherLicense = true;
                    }
                    i++;
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

            //Si pasa todas las condiciones anteriores, la licencia puede ser emitida y se setean los datos correspondientes.
            license.setLicenseClass(licenseClass);
            license.setLicenseOwner(owner);
            LocalDate newLicenseDate = LocalDate.now();
            license.setLicenseStart(newLicenseDate);
            LocalDate licenseTerm = calculateLicenseTerm(owner);
            license.setLicenseTerm(licenseTerm);
            license.setIsRevoked(false);

            //Se almacena la licencia en la base de datos.
            try {
                licenseRepo.save(license);
                return "success";
            } catch (Exception e) {
                return "No se ha podido guardar la licencia, intente nuevamente";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "No se ha podido guardar la licencia, intente nuevamente";
        }

    }

    public License getLicenseById(Integer idLicense){
        return licenseRepo.findById(idLicense).get();
    }

    @Override
    public List<License> getExpiredLicenses() {

        //Solicitar a ILicenseRepo la lista de licencias expiradas
        List<License> resultList = licenseRepo.getExpiredLicenses(LocalDate.now());

        return resultList;
    }

    @Override
    public List<License> getCurrentLicenses(Integer ownerId) {
        List<License> listResult = null;
        try{
            listResult=licenseRepo.getCurrentLicenses(ownerId, LocalDate.now());
            return  listResult;
        }catch (Exception e){
            return null;
        }

    }
}
