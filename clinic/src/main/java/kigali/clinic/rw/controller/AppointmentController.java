package kigali.clinic.rw.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.service.AppointmentService;

@RestController
@RequestMapping(value = {"/api/appointment", "/api/appointments"})
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> saveAppointment(@RequestBody Appointment appointment){

        if(appointment.getDoctor() != null && appointment.getDoctor().getId() != null
            && appointment.getAppointmentDate() != null
            && appointmentService.isDoctorBooked(appointment.getDoctor().getId(), appointment.getAppointmentDate())){
            return new ResponseEntity<>("Doctor is already booked on that date", HttpStatus.CONFLICT);
        }

        Appointment savedAppointment = appointmentService.saveAppointment(appointment);
        return new ResponseEntity<>(savedAppointment, HttpStatus.CREATED);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllAppointments(){

        List<Appointment> appointments = appointmentService.getAllAppointments();
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAppointmentById(@PathVariable UUID id){

        Optional<Appointment> appointment = appointmentService.getAppointmentById(id);

        if(appointment.isEmpty()){
            return new ResponseEntity<>("we don't have appointment with that id", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(appointment.get(), HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updateAppointment(@PathVariable UUID id, @RequestBody Appointment appointment){

        String returnedMessage = appointmentService.updateAppointment(id, appointment);
        return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteAppointment(@PathVariable UUID id){

        String returnedMessage = appointmentService.deleteAppointment(id);
        return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
    }

    @GetMapping(value = "/by-doctor-and-status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAppointmentsByDoctorAndStatus(
        @RequestParam UUID doctorId,
        @RequestParam(defaultValue = "PENDING") AppointmentStatus status){

        List<Appointment> appointments = appointmentService.getAppointmentsByDoctorAndStatus(doctorId, status);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping(value = "/by-specialization", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAppointmentsByDoctorSpecialization(
        @RequestParam String specializationName){

        List<Appointment> appointments = appointmentService.getAppointmentsByDoctorSpecialization(specializationName);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping(value = "/paginated", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDoctorAppointmentsPaginated(
        @RequestParam UUID doctorId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size){

        Page<Appointment> appointments = appointmentService.getDoctorAppointmentsPaginated(doctorId, page, size);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }
  @GetMapping(value = "/by-status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAppointmentsByStatus(@RequestParam AppointmentStatus status){

        List<Appointment> appointments = appointmentService.getAppointmentsByStatus(status);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping(value = "/between", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAppointmentsBetween(
        @RequestParam String start,
        @RequestParam String end){

        Date startDate = Date.valueOf(LocalDate.parse(start));
        Date endDate = Date.valueOf(LocalDate.parse(end));

        List<Appointment> appointments = appointmentService.getAppointmentsBetween(startDate, endDate);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }
    @GetMapping(value = "/stats/by-status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAppointmentCountPerStatus(){

        List<Object[]> counts = appointmentService.getAppointmentCountPerStatus();
        return new ResponseEntity<>(counts, HttpStatus.OK);
    }
    @PatchMapping(value = "/cancel-day", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> cancelDoctorDay(
        @RequestParam UUID doctorId,
        @RequestParam String date){

        String returnedMessage = appointmentService.cancelDoctorDay(doctorId, Date.valueOf(LocalDate.parse(date)));
        return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
    }

    @GetMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAppointmentsPage(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "appointmentDate,desc") String sort){

        String[] sortParts = sort.split(",");
        Sort.Direction direction = (sortParts.length > 1 && sortParts[1].equalsIgnoreCase("desc"))
            ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortParts[0]));
        Page<Appointment> appointments = appointmentService.getAppointmentsPage(pageable);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @DeleteMapping(value = "/cancelled-before", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteCancelledBefore(@RequestParam String date){

        String returnedMessage = appointmentService.deleteCancelledBefore(Date.valueOf(LocalDate.parse(date)));
        return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
    }

}
