package com.tp.driverlicensesystem.services;

import com.tp.driverlicensesystem.model.License;
import com.tp.driverlicensesystem.model.Owner;
import com.tp.driverlicensesystem.repository.ILicenseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
public class LicenseService implements ILicenseService{

    @Autowired
    private ILicenseRepo licenseRepo;

    @Autowired
    private IOwnerService iOwnerService;

    @Override
    public LocalDate calculateLicenseTerm(Integer ownerId) {
        //Deberia pedir el Titular a OwnerRepository con el id que llega en license
        Owner owner = new Owner();

        //El owner ya existe porque fue buscado con el boton anteriormente, o creado.
//
        try {
            owner = iOwnerService.getOwnerById(ownerId);
            //System.out.println(owner.getName() + " --- " + owner.getLicensesList().size());
        }catch (Exception e){
            //Fallo la busqueda a la base de datos.
            e.printStackTrace();

        }

        Integer ownerAge = iOwnerService.getOwnerAge(owner.getBirthDate());
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
                        //Mayores de 70 años
                        add = 1;
                    }
                }
            }
        }

        LocalDate licenseTermReturn = licenseTerm.plusYears(add);

        System.out.println("Fecha de expiracion de licencia: " + licenseTermReturn.toString());


        return licenseTermReturn;
    }

    @Override
    public void saveLicense(License license) {
        LocalDate newLicenseDate = LocalDate.now();
        license.setLicenseStart(newLicenseDate);
        try {
            licenseRepo.save(license);
        }catch (Exception e){
            //FALLO EL GUARDADO DE LA LICENCIA
        }
    }


}
