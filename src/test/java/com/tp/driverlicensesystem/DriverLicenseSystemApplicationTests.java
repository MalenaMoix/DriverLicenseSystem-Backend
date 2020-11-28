package com.tp.driverlicensesystem;

import com.tp.driverlicensesystem.model.License;
import com.tp.driverlicensesystem.model.Owner;
import com.tp.driverlicensesystem.services.ILicenseService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.ArrayList;

@SpringBootTest
class DriverLicenseSystemApplicationTests {
    private ArrayList<Owner> ownerArrayList;
    private ArrayList<License> licenseList;
    private ArrayList<LocalDate> birthDatesList;
    @Autowired
    ILicenseService iLicenseService;

    @BeforeEach
    void setUp (){

        //Cargar datos  a la lista de fechas de nacimiento
        startBirthDateList();

        //Lista de Owners que tendra una fecha de nacimiento segun la lista birthDatesList
        ownerArrayList = new ArrayList<>();
        licenseList = new ArrayList<>();

        //Cargamos la lista de Owners a testear con fechas de nacimiento correspondientes a birthDatesList
        for (int i=0; i<8; i++){
            Owner owner = new Owner();
            owner.setBirthDate(birthDatesList.get(i));
            if(i==1){
                owner.addLicense(new License());
            }
            ownerArrayList.add(owner);
        }
    }


    @Test
    void contextLoads() {
    }

    @Test
    void testCalculateLicenseTerm(){
        ArrayList<LocalDate> birthDatesResultList = new ArrayList<>();
        //Inicializar la lista de resultados de fechas que deberia coincidir con lo que retorna el metodo
        //calculateLicenseTerm()
        startBirthDatResultList(birthDatesResultList);

        int i;

        //agregamos licencias, cuya fecha de expiracion se calcula con el metodo a probar
        // "calculateLicenseTerm(owner), a la lista de licencias que luego se comparara.
        for(i = 0; i<8; i++){
            License license = new License();
            license.setLicenseTerm(iLicenseService.calculateLicenseTerm(ownerArrayList.get(i)));
            licenseList.add(license);
        }

        for(i = 0; i<8; i++){
            Assertions.assertThat(licenseList.get(i).getLicenseTerm().equals(birthDatesResultList.get(i)));
        }
    }

    private void startBirthDateList() {
        birthDatesList = new ArrayList<>();
        LocalDate localDate = LocalDate.of(1998,4,13);
        birthDatesList.add(localDate);
        localDate = LocalDate.of(2000,4,13);
        birthDatesList.add(localDate);
        localDate = LocalDate.of(2000,4,13);
        birthDatesList.add(localDate);
        localDate = LocalDate.of(1976,4,13);
        birthDatesList.add(localDate);
        localDate = LocalDate.of(1973,4,13);
        birthDatesList.add(localDate);
        localDate = LocalDate.of(1953,4,13);
        birthDatesList.add(localDate);
        localDate = LocalDate.of(1960,12,20);
        birthDatesList.add(localDate);
        localDate = LocalDate.of(1949,4,13);
        birthDatesList.add(localDate);
    }


    private void startBirthDatResultList(ArrayList<LocalDate> birthDatesResultList) {
        LocalDate localDate = LocalDate.of(2025,4,13);
        birthDatesResultList.add(localDate);
        localDate = LocalDate.of(2023,4,13);
        birthDatesResultList.add(localDate);
        localDate = LocalDate.of(2021,4,13);
        birthDatesResultList.add(localDate);
        localDate = LocalDate.of(2025,4,13);
        birthDatesResultList.add(localDate);
        localDate = LocalDate.of(2024,4,13);
        birthDatesResultList.add(localDate);
        localDate = LocalDate.of(2023,4,13);
        birthDatesResultList.add(localDate);
        localDate = LocalDate.of(2024,12,20);
        birthDatesResultList.add(localDate);
        localDate = LocalDate.of(2021,4,13);
        birthDatesResultList.add(localDate);
    }


}
