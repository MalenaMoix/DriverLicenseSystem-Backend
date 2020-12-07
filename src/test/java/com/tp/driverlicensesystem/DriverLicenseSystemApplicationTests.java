package com.tp.driverlicensesystem;

import com.tp.driverlicensesystem.model.Owner;
import com.tp.driverlicensesystem.services.IOwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.ArrayList;

@SpringBootTest
class DriverLicenseSystemApplicationTests {

    private ArrayList<Owner> ownerList;

    @Autowired
    IOwnerService iOwnerService;

    @Test
    void contextLoads() {
    }

    @BeforeEach
    void setUp(){
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
}
