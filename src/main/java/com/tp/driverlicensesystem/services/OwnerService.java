package com.tp.driverlicensesystem.services;

import com.tp.driverlicensesystem.model.Owner;
import com.tp.driverlicensesystem.repository.IOwnerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class OwnerService implements IOwnerService{

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
    public String saveOwner(Owner owner) {

        //CHECKEAR ANTES DE VALIDAR LOS DATOS QUE NO EXISTA UN TITULAR CON EL MISMO DOCUMENTO
        Owner auxOwner = getOwnerById(owner.getDocument());

        if(auxOwner.getDocument() == owner.getDocument()){
            return "Titular ya existente";
        }

        //VALIDACIONES DE LOS ATRIBUTOS DEL TITULAR

        if(String.valueOf(owner.getDocument()).matches("[0-9]{7}|[0-9]{8}")){
            //lee el documento y lo pasa a string para validar mediante expresion regular
            //que el documento sea un conjunto de 7 u 8 numeros

            if(owner.getName().matches("[a-zA-Z]+\\s[a-zA-Z]+|[a-zA-Z]+")){
                //valida mediante una expresion regular que el nombre sea un string que contiene
                //solo letras y que puede tener un espacio entre medio en caso que el titular
                //tenga dos nombres
                //ejemplos: "Juan" - valido
                //          "Juan Pablo" - valido
                //          " Juan" - no valido
                //          "Juan " - no valido
                //          "Juan Pablo " - no valido

                if(owner.getLastName().matches("[a-zA-Z]+\\s[a-zA-Z]+|[a-zA-Z]+")){
                    //mismo funcionamiento que con el nombre

                    if(getOwnerAge(owner.getBirthDate()) > 16){
                        //valida que el titular tenga la edad apropiada

                        if(owner.getBloodType().matches("[A]|[B]|[AB]|[O]")){
                            //valida mediante una expresion regular que el tipo de sangre sea
                            //A, B, AB u O

                            if(owner.getRhFactor().matches("[+]|[-]")){
                                //valida mediante una expresion regular que el factor de sangre
                                //sea positivo(+) o negativo(-)

                                try {
                                    //guardar el titular en la base de datos
                                    iOwnerRepo.save(owner);
                                    return "Exito";
                                }catch (Exception e){
                                    //error al ejecutar el metodo para guardar en la base de datos
                                    System.out.println(e.getMessage());
                                }
                            } else {
                                return "Factor rh erroneo";
                            }
                        } else {
                            return "Tipo de sangre erronea";
                        }
                    } else {
                        return "Edad erronea";
                    }
                } else {
                    return "Apellido erroneo";
                }
            } else {
                return "Nombre erroneo";
            }
        } else {
            return "Documento erroneo";
        }
        return "error";
    }

    @Override
    public Owner getOwnerById(Integer ownerId) {

        Owner owner = new Owner();

        try {
            owner = iOwnerRepo.findById(ownerId).get();
            //Hacemos esto para no generar un StackOverflow del JSON
            //xq un owner tiene licencias y una licencia tiene un owner y en el JSON se llaman recursivamente
            //Aca una licencia tiene un OBJETO owner
            owner.setLicensesList(null);
        }
        catch (Exception e){

        }

        return owner;
    }
}