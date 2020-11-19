package com.tp.driverlicensesystem.services;

import com.tp.driverlicensesystem.model.License;
import com.tp.driverlicensesystem.model.Owner;
import com.tp.driverlicensesystem.repository.ILicenseRepo;
import com.tp.driverlicensesystem.repository.IOwnerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    @Override
    public LocalDate calculateLicenseTerm(License license) {
        //Deberia pedir el Titular a OwnerRepository con el id que llega en license
        Owner owner = new Owner();
        LocalDate test = LocalDate.of(1975, Month.NOVEMBER, 15);

        owner.setDateOfBirthday(test);

        Integer ownerAge = getOwnerAge(owner.getDateOfBirthday());
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

    //TODO implementar metodo
    @Override
    public void getOwnerById() {
        System.out.println("Buscando titular");
        Owner owner= iOwnerRepo.findById(40905305).get();;
        owner.setName("TOMAS");
        owner.setLastName("RAVELLI");
        owner.setDateOfBirthday(LocalDate.of(1998,4,13));
        owner.setAddress("15 DE AGOSTO, 495, MARCELINO ESCALADA");
        owner.setDocument(40905305);
        owner.setDonor(true);
        owner.setBloodType("O+");
        License license = new License();
        License license2 = new License();
        License license3 = new License();
        owner.addLicense(license);
        owner.addLicense(license2);
        owner.addLicense(license3);
        license.setLicenseOwner(owner);
        license2.setLicenseOwner(owner);
        license3.setLicenseOwner(owner);
        iOwnerRepo.save(owner);


        owner = iOwnerRepo.findById(40905305).get();
        System.out.println(owner.getLicensesList().size());
    }

    private Integer getOwnerAge (LocalDate dateOfBirthday){
        LocalDate now = LocalDate.now();
        LocalDate birthdayThisYear = dateOfBirthday.withYear(now.getYear());

        Integer age = now.getYear() - dateOfBirthday.getYear();

        if (now.isBefore(birthdayThisYear)){
            age--;
        }

        System.out.println("Edad: " + age);

        return age;
    }
}
