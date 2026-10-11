package kigali.clinic.rw.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.Patient;
import kigali.clinic.rw.service.AppointmentService;
import kigali.clinic.rw.service.DoctorService;
import kigali.clinic.rw.service.PatientService;

@RestController
@RequestMapping(value = { "/api/patient", "/api/patients" })
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> savePatient(@RequestBody Patient patient) {

        Patient savedPatient = patientService.savePatient(patient);
        return new ResponseEntity<>(savedPatient, HttpStatus.CREATED);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllPatients() {

        List<Patient> patients = patientService.getAllPatients();
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPatientById(@PathVariable UUID id) {

        Optional<Patient> patient = patientService.getPatientById(id);

        if (patient.isEmpty()) {
            return new ResponseEntity<>("we don't have patient with that id", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(patient.get(), HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePatient(@PathVariable UUID id, @RequestBody Patient patient) {

        String returnedMessage = patientService.updatePatient(id, patient);
        return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deletePatient(@PathVariable UUID id) {

        String returnedMessage = patientService.deletePatient(id);
        return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
    }

    @GetMapping(value = "/by-last-name", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPatientsByLastName(@RequestParam String lastName) {

        List<Patient> patients = patientService.getPatientsByLastName(lastName);
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @GetMapping(value = "/of-doctor/{doctorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPatientsOfDoctor(@PathVariable UUID doctorId) {

        if (doctorService.getDoctorById(doctorId).isEmpty()) {
            return new ResponseEntity<>("The doctor with that id does not exist", HttpStatus.NOT_FOUND);
        }

        List<Patient> patients = patientService.getPatientsOfDoctor(doctorId);
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @GetMapping(value = "/frequent", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFrequentPatients(@RequestParam long min) {

        List<Patient> patients = patientService.getFrequentPatients(min);
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @GetMapping(value = "/{ssNumber}/upcoming", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUpcomingAppointments(@PathVariable String ssNumber) {

        if (patientService.getPatientBySsNumber(ssNumber).isEmpty()) {
            return new ResponseEntity<>("That patient with that ssnumber does not exist", HttpStatus.NOT_FOUND);
        }

        List<Appointment> appointments = appointmentService.getUpcomingAppointmentsOfPatient(ssNumber);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

}
