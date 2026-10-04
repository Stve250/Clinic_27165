package kigali.clinic.rw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kigali.clinic.rw.domain.Specialization;
import kigali.clinic.rw.service.SpecializationService;

@RestController
@RequestMapping(value = "/api/specializations")
public class SpecializationController {

    @Autowired
    private SpecializationService specializationService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> saveSpecialization(@RequestBody Specialization specialization){

        String returnedMessage = specializationService.saveSpecialization(specialization);
        return new ResponseEntity<>(returnedMessage, HttpStatus.CREATED);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllSpecializations(){

        List<Specialization> specializations = specializationService.getAllSpecializations();
        return new ResponseEntity<>(specializations, HttpStatus.OK);
    }

    @GetMapping(value = "/unused", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUnusedSpecializations(){

        List<Specialization> specializations = specializationService.getUnusedSpecializations();
        return new ResponseEntity<>(specializations, HttpStatus.OK);
    }

}
