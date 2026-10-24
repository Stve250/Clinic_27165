package auca.pharma.rw.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import auca.pharma.rw.domain.SalesRepresentative;
import auca.pharma.rw.service.SalesRepresentativeService;

@RestController
@RequestMapping(value = "/api/sales-representatives")
public class SalesRepresentativeController {

    @Autowired
    private SalesRepresentativeService salesRepresentativeService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveSalesRepresentative(@RequestBody SalesRepresentative salesRepresentative) {

        String returnedMessage = salesRepresentativeService.saveSalesRepresentative(salesRepresentative);
        return new ResponseEntity<>(returnedMessage, HttpStatus.CREATED);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllSalesRepresentatives() {

        List<SalesRepresentative> salesRepresentatives = salesRepresentativeService.getAllSalesRepresentatives();
        return new ResponseEntity<>(salesRepresentatives, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSalesRepresentativeById(@PathVariable UUID id) {

        Optional<SalesRepresentative> salesRepresentative = salesRepresentativeService
                .getSalesRepresentativeById(id);

        if (salesRepresentative.isEmpty()) {
            return new ResponseEntity<>("The sales representative with that id does not exist", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(salesRepresentative.get(), HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateSalesRepresentative(@PathVariable UUID id,
            @RequestBody SalesRepresentative salesRepresentative) {

        String returnedMessage = salesRepresentativeService.updateSalesRepresentative(id, salesRepresentative);

        if (returnedMessage.equals("The sales representative with that id does not exist")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteSalesRepresentative(@PathVariable UUID id) {

        String returnedMessage = salesRepresentativeService.deleteSalesRepresentative(id);

        if (returnedMessage.equals("The sales representative with that id does not exist")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }
    }

    // C1
    @GetMapping(value = "/by-product-line", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSalesRepresentativesByProductLine(@RequestParam String name) {

        List<SalesRepresentative> salesRepresentatives = salesRepresentativeService
                .getSalesRepresentativesByProductLine(name);
        return new ResponseEntity<>(salesRepresentatives, HttpStatus.OK);
    }

    // D3
    @PutMapping(value = "/{salesRepresentativeId}/product-lines/{productLineId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addProductLine(
            @PathVariable UUID salesRepresentativeId,
            @PathVariable UUID productLineId) {

        String returnedMessage = salesRepresentativeService.addProductLine(salesRepresentativeId, productLineId);

        if (returnedMessage.equals("The sales representative with that id does not exist")
                || returnedMessage.equals("That product line does not exist")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        } else if (returnedMessage.equals("The sales representative already has that product line")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }
    }

    // D4
    @PatchMapping(value = "/{salesRepresentativeId}/territory", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changeTerritory(
            @PathVariable UUID salesRepresentativeId,
            @RequestParam int territoryNumber) {

        String returnedMessage = salesRepresentativeService.changeTerritory(salesRepresentativeId, territoryNumber);

        if (returnedMessage.equals("The sales representative with that id does not exist")
                || returnedMessage.equals("The territory with that territoryNumber does not exist")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        } else if (returnedMessage.equals("That territory is taken by another sales representative")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }
    }

}
