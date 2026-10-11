package kigali.clinic.rw.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.service.AppointmentService;
import kigali.clinic.rw.service.DoctorService;

@RestController
@RequestMapping(value = { "/api/doctor", "/api/doctors" })
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveDoctor(@RequestBody Doctor doctor) {

        String returnedMessage = doctorService.saveDoctor(doctor);
        return new ResponseEntity<>(returnedMessage, HttpStatus.CREATED);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllDoctors() {

        List<Doctor> doctors = doctorService.getAllDoctors();
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDoctorById(@PathVariable UUID id) {

        Optional<Doctor> doctor = doctorService.getDoctorById(id);

        if (doctor.isEmpty()) {
            return new ResponseEntity<>("we don't have doctor with that id", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(doctor.get(), HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateDoctor(@PathVariable UUID id, @RequestBody Doctor doctor) {

        String returnedMessage = doctorService.updateDoctor(id, doctor);
        return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteDoctor(@PathVariable UUID id) {

        String returnedMessage = doctorService.deleteDoctor(id);
        return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
    }

    @GetMapping(value = "/by-specialization", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDoctorsBySpecialization(@RequestParam String name) {

        List<Doctor> doctors = doctorService.getDoctorsBySpecialization(name);
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @GetMapping(value = "/without-office", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDoctorsWithoutOffice() {

        List<Doctor> doctors = doctorService.getDoctorsWithoutOffice();
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @PutMapping(value = "/{doctorId}/specializations/{specializationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addSpecialization(
            @PathVariable UUID doctorId,
            @PathVariable UUID specializationId) {

        String returnedMessage = doctorService.addSpecialization(doctorId, specializationId);

        if (returnedMessage.equals("The doctor with that id does not exist")
                || returnedMessage.equals("That specialization does not exist")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        } else if (returnedMessage.equals("The doctor already has that specialization")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "/{doctorId}/specializations/{specializationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> removeSpecialization(
            @PathVariable UUID doctorId,
            @PathVariable UUID specializationId) {

        String returnedMessage = doctorService.removeSpecialization(doctorId, specializationId);

        if (returnedMessage.equals("The doctor with that id does not exist")
                || returnedMessage.equals("That specialization does not exist")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        } else if (returnedMessage.equals("The doctor does not have that specialization")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }
    }

    @PatchMapping(value = "/{doctorId}/office", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changeOffice(@PathVariable UUID doctorId, @RequestParam int officeNumber) {

        String returnedMessage = doctorService.changeOffice(doctorId, officeNumber);

        if (returnedMessage.equals("The doctor with that id does not exist")
                || returnedMessage.equals("The office with that office number does not exist")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        } else if (returnedMessage.equals("That office is taken by other doctor")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/{doctorId}/agenda", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDoctorAgenda(@PathVariable UUID doctorId, @RequestParam String date) {

        if (doctorService.getDoctorById(doctorId).isEmpty()) {
            return new ResponseEntity<>("The doctor with that id does not exist", HttpStatus.NOT_FOUND);
        }

        List<Appointment> agenda = appointmentService.getDoctorAgenda(doctorId, LocalDate.parse(date));
        return new ResponseEntity<>(agenda, HttpStatus.OK);
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> searchDoctorsByName(@RequestParam String name) {

        List<Doctor> doctors = doctorService.searchDoctorsByName(name);
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @GetMapping(value = "/workload", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDoctorWorkload(@RequestParam String start, @RequestParam String end) {

        List<Object[]> workload = appointmentService.getDoctorWorkload(
                LocalDate.parse(start), LocalDate.parse(end));
        return new ResponseEntity<>(workload, HttpStatus.OK);
    }

}
