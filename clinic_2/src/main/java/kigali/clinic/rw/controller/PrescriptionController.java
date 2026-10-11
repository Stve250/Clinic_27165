package kigali.clinic.rw.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.Prescription;
import kigali.clinic.rw.service.AppointmentService;
import kigali.clinic.rw.service.PrescriptionService;

@RestController
@RequestMapping(value = "/api/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> savePrescription(
            @RequestParam UUID appointmentId,
            @RequestBody Prescription prescription) {

        String returnedMessage = prescriptionService.savePrescription(appointmentId, prescription);

        if (returnedMessage.equals("The appointment with that id does not exist")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        } else if (returnedMessage.equals("Prescriptions are only for COMPLETED appointments")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.CREATED);
        }
    }

    @GetMapping(value = "/appointment/{appointmentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPrescriptionsByAppointment(@PathVariable UUID appointmentId) {

        Optional<Appointment> appointment = appointmentService.getAppointmentById(appointmentId);

        if (appointment.isEmpty()) {
            return new ResponseEntity<>("The appointment with that id does not exist", HttpStatus.NOT_FOUND);
        }

        List<Prescription> prescriptions = prescriptionService.getPrescriptionsByAppointment(appointment.get());
        return new ResponseEntity<>(prescriptions, HttpStatus.OK);
    }

    @GetMapping(value = "/patient/{ssNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPrescriptionsOfPatient(@PathVariable String ssNumber) {

        List<Prescription> prescriptions = prescriptionService.getPrescriptionsOfPatient(ssNumber);
        return new ResponseEntity<>(prescriptions, HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePrescription(@PathVariable UUID id, @RequestBody Prescription prescription) {

        String returnedMessage = prescriptionService.updatePrescription(id, prescription);

        if (returnedMessage.equals("The prescription with that id does not exist")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deletePrescription(@PathVariable UUID id) {

        String returnedMessage = prescriptionService.deletePrescription(id);

        if (returnedMessage.equals("The prescription with that id does not exist")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/stats/top-medicines", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTopMedicines() {

        List<Object[]> topMedicines = prescriptionService.getTopMedicines();
        return new ResponseEntity<>(topMedicines, HttpStatus.OK);
    }

}
