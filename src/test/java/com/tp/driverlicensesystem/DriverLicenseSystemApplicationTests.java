package com.tp.driverlicensesystem;

import com.tp.driverlicensesystem.services.IOwnerService;
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
      
      //Lista de Owners/Titulares a testear
        ownerList = new ArrayList<>();

        for(int i=0; i<8; i++){
            Owner owner = new Owner();

            owner.setDocument(40235870);
            owner.setName("Juan");
            owner.setLastName("Perez");
            owner.setBirthDate(LocalDate.of(1994,11,3));
            owner.setBloodType("A");
            owner.setRhFactor("+");

            switch (i) {
                case 1:
                    owner.setDocument(38447221);
                    break;
                case 2:
                    owner.setDocument(402358);
                    break;
                case 3:
                    owner.setDocument(40235871);
                    owner.setName("Juan ");
                    break;
                case 4:
                    owner.setDocument(40235872);
                    owner.setLastName("Perez ");
                    break;
                case 5:
                    owner.setDocument(40235873);
                    owner.setBirthDate(LocalDate.of(2004,11,3));
                    break;
                case 6:
                    owner.setDocument(40235874);
                    owner.setBloodType("C");
                    break;
                case 7:
                    owner.setDocument(40235875);
                    owner.setRhFactor("positivo");
                    break;
            }

            ownerList.add(owner);
        }
    }


    private ArrayList<Owner> ownerList;

    @Autowired
    IOwnerService iOwnerService;

    @Test
    void contextLoads() {
    }


    @Test
    void testRegisterOwner(){
        ArrayList<String> stringsList = new ArrayList<>();
        String respuesta;

        //Cargar lista de resultados esperados
        startStringsList(stringsList);

        for(int i=0; i<8; i++) {
            System.out.print("Titular numero: "+i+ " --- Resultado esperado: "+stringsList.get(i));
            Assert.isTrue((respuesta = iOwnerService.saveOwner(ownerList.get(i))).equals(stringsList.get(i)),
                    "Respuesta esperada: "+stringsList.get(i)+" --- "+
                            "Respuesta dada: "+respuesta);
        }
    }

    private void startStringsList(ArrayList<String> stringsList) {
        stringsList.add("Exito");
        stringsList.add("Titular ya existente");
        stringsList.add("Documento erroneo");
        stringsList.add("Nombre erroneo");
        stringsList.add("Apellido erroneo");
        stringsList.add("Edad erronea");
        stringsList.add("Tipo de sangre erronea");
        stringsList.add("Factor rh erroneo");
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
            System.out.println(licenseList.get(i).getLicenseTerm() + "--- " + birthDatesResultList.get(i));
            Assert.isTrue(licenseList.get(i).getLicenseTerm().equals(birthDatesResultList.get(i)),"The dates should be equals");
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
        localDate = LocalDate.of(1959,12,20);
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
