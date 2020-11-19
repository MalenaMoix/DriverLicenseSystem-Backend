package com.tp.driverlicensesystem.services;

import com.tp.driverlicensesystem.model.Owner;
import com.tp.driverlicensesystem.repository.IOwnerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class IOwnerServiceImpl implements IOwnerService{

    @Autowired
    private IOwnerRepo iOwnerRepo;

    @Override
    public Integer getOwnerAge(LocalDate dateOfBirthday) {
        LocalDate now = LocalDate.now();
        LocalDate birthdayThisYear = dateOfBirthday.withYear(now.getYear());

        Integer age = now.getYear() - dateOfBirthday.getYear();

        if (now.isBefore(birthdayThisYear)){
            age--;
        }

        System.out.println("Edad: " + age);

        return age;
    }

    @Override
    public void saveOwner(Owner owner) {
        try {
            iOwnerRepo.save(owner);
        }catch (Exception e){
            //FALLO AL GUARDAR OWNER
        }
    }

    @Override
    public Owner getOwnerById(Integer ownerId) {

        Owner owner = new Owner();

        try {
            owner = iOwnerRepo.findById(ownerId).get();
        }
        catch (Exception e){

        }

        return owner;
    }
}
