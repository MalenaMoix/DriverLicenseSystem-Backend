package com.tp.driverlicensesystem;

import com.tp.driverlicensesystem.services.IOwnerService;
import com.tp.driverlicensesystem.model.License;
import com.tp.driverlicensesystem.model.Owner;
import com.tp.driverlicensesystem.services.ILicenseService;
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
    private ArrayList<String> licenseClassList;
    @Autowired
    ILicenseService iLicenseService;

    @BeforeEach
    void setUp (){

        //Cargar datos  a la lista de fechas de nacimiento
        startBirthDateList();

        //Cargar datos a la lista de clases de licencia
        startLicenseClassList();

        //Lista de Owners que tendran una fecha de nacimiento segun la lista birthDatesList
        ownerArrayList = new ArrayList<>();
        licenseList = new ArrayList<>();

        //Cargamos la lista de Owners que seran usados para calcular la vigencia de las licencias a testear
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
    void testCalculateLicenseCost(){
        //Inicializar la lista de resultados de precios que deberian coincidir con lo que retorna el metodo calculateLicenseCost()
        ArrayList<Double> licenseCostResultList = new ArrayList<>();
        startLicenseCostResultList(licenseCostResultList);

        //Agregamos licencias cuya fecha de expiracion se calcula con el metodo calculateLicenseTerm(owner) para luego calcular el precio
        for(int i=0; i<8; i++){
            License license = new License();
            license.setLicenseTerm(iLicenseService.calculateLicenseTerm(ownerArrayList.get(i)));
            license.setLicenseCost(iLicenseService.calculateLicenseCost(licenseClassList.get(i)));
            licenseList.add(license);
        }

        for(int j=0; j<8; j++){
            Double response;
            System.out.println("License cost: "+licenseList.get(j).getLicenseCost()+" --- Expected license cost: "+licenseCostResultList.get(j));
            Assert.isTrue((response = licenseList.get(j).getLicenseCost()).equals(licenseCostResultList.get(j)),
                    "Result given: "+response+" --- "+
                            "Expected result: "+licenseCostResultList.get(j));
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

    private void startLicenseClassList() {
        licenseClassList = new ArrayList<>();

        licenseClassList.add("A");
        licenseClassList.add("A");
        licenseClassList.add("B");
        licenseClassList.add("F");
        licenseClassList.add("E");
        licenseClassList.add("G");
        licenseClassList.add("D");
        licenseClassList.add("C");
    }

    private void startLicenseCostResultList(ArrayList<Double> licenseCostResultList) {
        //5 años - clase A
        licenseCostResultList.add(48.00);
        //3 años - clase A
        licenseCostResultList.add(33.00);
        //1 año - clase B
        licenseCostResultList.add(28.00);
        //5 años - clase F
        licenseCostResultList.add(67.00);
        //4 años - clase E
        licenseCostResultList.add(52.00);
        //3 años - clase G
        licenseCostResultList.add(33.00);
        //4 años - clase D
        licenseCostResultList.add(52.00);
        //1 año - clase C
        licenseCostResultList.add(31.00);
    }

}
