package com.tp.driverlicensesystem.rest;

import com.tp.driverlicensesystem.assistant.PDFGenerator;
import com.tp.driverlicensesystem.model.License;
import com.tp.driverlicensesystem.model.Owner;
import com.tp.driverlicensesystem.services.ILicenseService;
import com.tp.driverlicensesystem.services.IOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.HttpURLConnection;
import java.util.List;


@RestController
@RequestMapping("/license")
@CrossOrigin(origins = "*")
public class LicenseController {
    @Autowired
    private ILicenseService iLicenseService;

    @Autowired
    private IOwnerService iOwnerService;

    @GetMapping(value = "/expiredLicenses")
    public ResponseEntity<List<License>> getExpiredLicenses(){

        List<License> listReturn = null;
        ResponseEntity<List<License>> responseEntity;

        try {
            //Solicitar la lista de licencias expiradas a LicenseService
            listReturn = iLicenseService.getExpiredLicenses();
            for(License l: listReturn){
                l.getLicenseOwner().setLicensesList(null);
            }
            //Setear en la respuesta al frontend la lista resultado y un codigo HTTP 200
            responseEntity = new ResponseEntity<>(listReturn,HttpStatus.OK);

        }catch (Exception e){
            //Setear en la respuesta al frontend la lista resultado y un codigo HTTP 500
            responseEntity = new ResponseEntity<>(listReturn,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }



    @PostMapping
    public ResponseEntity<String> postLicense(@RequestBody License license, HttpServletResponse response){
        //Se recibe la solicitud para emitir una nueva licencia y
        //LicenseService se encarga de gesitonar dicha solicitud
        String message = iLicenseService.saveLicense(license);

        switch(message){
            case "success":
                return new ResponseEntity<>(license.getIdLicense().toString(),HttpStatus.OK);
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
            license.setLicenseCost(iLicenseService.calculateLicenseCost(licenseClass));
            owner.setLicensesList(null);
            return license;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping(value = "/licensePDF/{idLicense}")
    public void getLicensePDF(HttpServletResponse response,  @PathVariable("idLicense") Integer idLicense){
        System.out.println("entra el metodo");
       try {
           License license = iLicenseService.getLicenseById(idLicense);
           response.setContentType("application/pdf");
           String headerKey = "Content-Disposition";
           String headerValue = "attachment; filename="+license.getLicenseOwner().getDocument()+"-"+idLicense+".pdf";
           response.setHeader(headerKey, headerValue);
           PDFGenerator pdfGenerator = new PDFGenerator();
           pdfGenerator.getNewLicensePDF(response, iLicenseService.getLicenseById(idLicense));

       }catch (Exception e){
           response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
       }
    }



}
