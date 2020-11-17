package com.tp.driverlicensesystem.services;

import com.tp.driverlicensesystem.model.License;
import com.tp.driverlicensesystem.model.Owner;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;


@Service
public class ILicenseServiceImpl implements ILicenseService{

    @Override
    public LocalDate calculateLicenseTerm(License license) {
        //Deberia pedir el Titular a OwnerRepository con el id que llega en license
        Owner owner = new Owner();
        LocalDate test = LocalDate.of(1976, Month.NOVEMBER, 16);

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

    private Integer getOwnerAge (LocalDate dateOfBirthday){
        LocalDate now = LocalDate.now();
        Integer age = now.getYear() - dateOfBirthday.getYear();

        if (now.getDayOfYear() < dateOfBirthday.getDayOfYear()){
            age--;
        }

        System.out.println("Edad: " + age);

        return age;
    }
}
