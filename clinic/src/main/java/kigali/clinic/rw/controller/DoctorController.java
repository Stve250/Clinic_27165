package kigali.clinic.rw.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.service.DoctorService;

@RestController
@RequestMapping(value = {"/api/doctor", "/api/doctors"})
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> saveDoctor(@RequestBody Doctor doctor){

        String returnedMessage = doctorService.saveDoctor(doctor);
        return new ResponseEntity<>(returnedMessage, HttpStatus.CREATED);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllDoctors(){

        List<Doctor> doctors = doctorService.getAllDoctors();
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDoctorById(@PathVariable UUID id){

        Optional<Doctor> doctor = doctorService.getDoctorById(id);

        if(doctor.isEmpty()){
            return new ResponseEntity<>("we don't have doctor with that id", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(doctor.get(), HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updateDoctor(@PathVariable UUID id, @RequestBody Doctor doctor){

        String returnedMessage = doctorService.updateDoctor(id, doctor);
        return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteDoctor(@PathVariable UUID id){

        String returnedMessage = doctorService.deleteDoctor(id);
        return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
    }

    @GetMapping(value = "/by-specialization", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDoctorsBySpecialization(@RequestParam String name){

        List<Doctor> doctors = doctorService.getDoctorsBySpecialization(name);
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @GetMapping(value = "/without-office", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDoctorsWithoutOffice(){

        List<Doctor> doctors = doctorService.getDoctorsWithoutOffice();
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

}
