package com.tp.driverlicensesystem.rest;

import com.tp.driverlicensesystem.assistant.PDFGenerator;
import com.tp.driverlicensesystem.model.License;
import com.tp.driverlicensesystem.model.Owner;
import com.tp.driverlicensesystem.services.ILicenseService;
import com.tp.driverlicensesystem.services.IOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/license")
@CrossOrigin("*")
public class LicenseController {
    @Autowired
    private ILicenseService iLicenseService;

    @Autowired
    private IOwnerService iOwnerService;

    @PostMapping
    public ResponseEntity<String> postLicense(@RequestBody License license){
        String message = iLicenseService.saveLicense(license);
        switch(message){
            case "success":
                return new ResponseEntity<>("Se guardo la licencia",HttpStatus.OK);
            case "forbidden":
                return new ResponseEntity<>("Titular no apto para esta licencia.",HttpStatus.FORBIDDEN);
            default:
                return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{id}/{licenseClass}")
    public License getCostAndValidUntil(@PathVariable("id") Integer document, @PathVariable("licenseClass") String licenseClass){
        System.out.println(document);
        License license = new License();
        license.setLicenseClass(licenseClass);

        try {
            Owner owner = new Owner();
            owner = iOwnerService.getOwnerById(document);
            license.setLicenseOwner(owner);
            license.setLicenseTerm(iLicenseService.calculateLicenseTerm(owner));

            //TODO hacer el metodo para calcular el costo
            license.setLicenseCost(42.50);
            return license;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/licensePDF/{idLicense}")
    public void getLicensePDF(HttpServletResponse response, @PathVariable("idLicense") Integer idLicense){
       try {
           response.setContentType("application/pdf");
           String headerKey = "Content-Disposition";
           String headerValue = "attachment; filename=prueba1.pdf";
           response.setHeader(headerKey, headerValue);
           PDFGenerator pdfGenerator = new PDFGenerator();
           pdfGenerator.getNewLicensePDF(response, iLicenseService.getLicenseById(idLicense));
       }catch (Exception e){
           response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
       }
    }
}
