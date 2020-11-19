package com.tp.driverlicensesystem.services;

import com.tp.driverlicensesystem.model.License;
import com.tp.driverlicensesystem.model.Owner;
import com.tp.driverlicensesystem.repository.ILicenseRepo;
import com.tp.driverlicensesystem.repository.IOwnerRepo;
import com.tp.driverlicensesystem.services.IOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;


@Service
public class ILicenseServiceImpl implements ILicenseService{

    @Autowired
    private ILicenseRepo licenseRepo;

    @Autowired
    private IOwnerRepo iOwnerRepo;

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
            System.out.println(owner.getName() + " --- " + owner.getLicensesList().size());
        }catch (Exception e){
            //Fallo la busqueda a la base de datos.
            e.printStackTrace();

        }

        Integer ownerAge = iOwnerService.getOwnerAge(owner.getDateOfBirthday());
        int add;

        LocalDate licenseTerm = LocalDate.of(LocalDate.now().getYear(),owner.getDateOfBirthday().getMonthValue(), owner.getDateOfBirthday().getDayOfMonth());

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
        license.setLicenseTerm(this.calculateLicenseTerm(license.getLicenseOwner().getDocument()));
        try {
            licenseRepo.save(license);
        }catch (Exception e){
            //FALLO EL GUARDADO DE LA LICENCIA
        }
    }


}
