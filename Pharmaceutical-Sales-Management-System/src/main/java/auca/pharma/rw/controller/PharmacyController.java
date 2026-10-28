package auca.pharma.rw.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import auca.pharma.rw.domain.Pharmacy;
import auca.pharma.rw.domain.SalesRepresentative;
import auca.pharma.rw.service.PharmacyService;
import auca.pharma.rw.service.SalesRepresentativeService;

@RestController
@RequestMapping(value = "/api/pharmacies")
public class PharmacyController {

    @Autowired
    private PharmacyService pharmacyService;

    @Autowired
    private SalesRepresentativeService salesRepresentativeService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> savePharmacy(@RequestBody Pharmacy pharmacy) {

        String returnedMessage = pharmacyService.savePharmacy(pharmacy);
        return new ResponseEntity<>(returnedMessage, HttpStatus.CREATED);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllPharmacies() {

        List<Pharmacy> pharmacies = pharmacyService.getAllPharmacies();
        return new ResponseEntity<>(pharmacies, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPharmacyById(@PathVariable UUID id) {

        Optional<Pharmacy> pharmacy = pharmacyService.getPharmacyById(id);

        if (pharmacy.isEmpty()) {
            return new ResponseEntity<>("The pharmacy with that id does not exist", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(pharmacy.get(), HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePharmacy(@PathVariable UUID id, @RequestBody Pharmacy pharmacy) {

        String returnedMessage = pharmacyService.updatePharmacy(id, pharmacy);

        if (returnedMessage.equals("The pharmacy with that id does not exist")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deletePharmacy(@PathVariable UUID id) {

        String returnedMessage = pharmacyService.deletePharmacy(id);

        if (returnedMessage.equals("The pharmacy with that id does not exist")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }
    }

}
