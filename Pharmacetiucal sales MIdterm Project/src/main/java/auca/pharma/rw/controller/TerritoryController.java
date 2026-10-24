package auca.pharma.rw.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import auca.pharma.rw.domain.Territory;
import auca.pharma.rw.service.TerritoryService;

@RestController
@RequestMapping(value = "/api/territories")
public class TerritoryController {

    @Autowired
    private TerritoryService territoryService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveTerritory(@RequestBody Territory territory) {

        String returnedMessage = territoryService.saveTerritory(territory);
        return new ResponseEntity<>(returnedMessage, HttpStatus.CREATED);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllTerritories() {

        List<Territory> territories = territoryService.getAllTerritories();
        return new ResponseEntity<>(territories, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTerritoryById(@PathVariable UUID id) {

        Optional<Territory> territory = territoryService.getTerritoryById(id);

        if (territory.isEmpty()) {
            return new ResponseEntity<>("The territory with that id does not exist", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(territory.get(), HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateTerritory(@PathVariable UUID id, @RequestBody Territory territory) {

        String returnedMessage = territoryService.updateTerritory(id, territory);

        if (returnedMessage.equals("The territory with that id does not exist")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteTerritory(@PathVariable UUID id) {

        String returnedMessage = territoryService.deleteTerritory(id);

        if (returnedMessage.equals("The territory with that id does not exist")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }
    }

    // C2
    @GetMapping(value = "/free", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFreeTerritories() {

        List<Territory> territories = territoryService.getFreeTerritories();
        return new ResponseEntity<>(territories, HttpStatus.OK);
    }

}
