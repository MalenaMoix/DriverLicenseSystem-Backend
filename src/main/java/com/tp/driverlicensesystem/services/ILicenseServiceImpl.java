package com.tp.driverlicensesystem.services;

import com.tp.driverlicensesystem.model.License;
import com.tp.driverlicensesystem.model.Owner;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.GregorianCalendar;

@Service
public class ILicenseServiceImpl implements ILicenseService{

    @Override
    public Calendar calculateLicenseTerm(License license) {
        //Deberia pedir el Titular a OwnerRepository con el id que llega en license
        Owner owner = new Owner();
        Calendar test = new GregorianCalendar(1959, Calendar.APRIL, 13);
        owner.setDateOfBirthday(test);


        Integer ownerAge = getOwnerAge(owner.getDateOfBirthday());
        int add;
        Calendar licenseTerm = Calendar.getInstance();
        licenseTerm.set(Calendar.MONTH, owner.getDateOfBirthday().get(Calendar.MONTH));
        licenseTerm.set(Calendar.DAY_OF_MONTH, owner.getDateOfBirthday().get(Calendar.DAY_OF_MONTH));


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

        licenseTerm.set(Calendar.YEAR, licenseTerm.get(Calendar.YEAR) + add);

        System.out.println(licenseTerm.get(Calendar.YEAR) + "/"+(licenseTerm.get(Calendar.MONTH)+1)+"/"+licenseTerm.get(Calendar.DAY_OF_MONTH));

        return licenseTerm;
    }

    private Integer getOwnerAge (Calendar dateOfBirthday){
        Calendar now = Calendar.getInstance();
        Integer age = now.get(Calendar.YEAR) - dateOfBirthday.get(Calendar.YEAR);

        if (now.get(Calendar.DAY_OF_YEAR) < dateOfBirthday.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        System.out.println(age);

        return age;
    }
}
