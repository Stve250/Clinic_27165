package kigali.clinic.rw.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.repository.AppointmentRepository;
import kigali.clinic.rw.repository.PrescriptionRepository;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private PrescriptionRepository prescriptionRepo;

    public List<Appointment> getAppointmentsByDoctorAndStatus(UUID doctorId, AppointmentStatus status) {
        return appointmentRepo.findByDoctorIdAndStatusOrderByAppointmentDateAsc(doctorId, status);
    }

    public List<Appointment> getAppointmentsByDoctorSpecialization(String specializationName) {
        return appointmentRepo.findByDoctorSpecialization(specializationName);
    }

    public Page<Appointment> getDoctorAppointmentsPaginated(UUID doctorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("appointmentDate").ascending());
        return appointmentRepo.findByDoctorId(doctorId, pageable);
    }

    public Appointment saveAppointment(Appointment appointment) {
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        return appointmentRepo.save(appointment);
    }

    public boolean isPastDate(LocalDate date) {
        return date.isBefore(LocalDate.now());
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepo.findAll();
    }

    public Optional<Appointment> getAppointmentById(UUID id) {
        return appointmentRepo.findById(id);
    }

    public String updateAppointment(UUID id, Appointment appointment) {

        Optional<Appointment> existingAppointment = appointmentRepo.findById(id);

        if (existingAppointment.isEmpty()) {
            return "we don't have appointment with that id";
        }

        Appointment appointmentToUpdate = existingAppointment.get();
        appointmentToUpdate.setAppointmentDate(appointment.getAppointmentDate());
        appointmentToUpdate.setReason(appointment.getReason());
        appointmentToUpdate.setPatient(appointment.getPatient());
        appointmentToUpdate.setDoctor(appointment.getDoctor());

        appointmentRepo.save(appointmentToUpdate);
        return "updated successfully";
    }

    public String deleteAppointment(UUID id) {

        Optional<Appointment> existingAppointment = appointmentRepo.findById(id);

        if (existingAppointment.isEmpty()) {
            return "The appointment with that id does not exist";
        }

        if (prescriptionRepo.existsByAppointmentId(id)) {
            return "That appointment has prescriptions, delete them first";
        }

        appointmentRepo.deleteById(id);
        return "deleted successfully";
    }

    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
        return appointmentRepo.findByStatusOrderByAppointmentDateAsc(status);
    }

    public List<Appointment> getAppointmentsBetween(LocalDate start, LocalDate end) {
        return appointmentRepo.findByAppointmentDateBetweenOrderByAppointmentDateAsc(start, end);
    }

    public boolean isDoctorBooked(UUID doctorId, LocalDate date) {
        return appointmentRepo.existsByDoctorIdAndAppointmentDateAndStatusNot(doctorId, date,
                AppointmentStatus.CANCELLED);
    }

    public List<Object[]> getAppointmentCountPerStatus() {
        return appointmentRepo.countAppointmentsByStatus();
    }

    public List<Object[]> getBusiestOffice() {
        return appointmentRepo.findBusiestOffice(PageRequest.of(0, 1));
    }

    @Transactional
    public String cancelDoctorDay(UUID doctorId, LocalDate date) {
        int updated = appointmentRepo.cancelDoctorDay(doctorId, date);
        return updated + " appointments cancelled";
    }

    public Page<Appointment> getAppointmentsPage(Pageable pageable) {
        return appointmentRepo.findAll(pageable);
    }

    @Transactional
    public String deleteCancelledBefore(LocalDate date) {
        int deleted = appointmentRepo.deleteCancelledBefore(date);
        return deleted + " appointments deleted";
    }

    public String changeStatus(UUID id, AppointmentStatus newStatus) {

        Optional<Appointment> existingAppointment = appointmentRepo.findById(id);

        if (existingAppointment.isEmpty()) {
            return "The appointment with that id does not exist";
        }

        Appointment appointment = existingAppointment.get();
        AppointmentStatus currentStatus = appointment.getStatus();

        boolean allowed = false;

        if (currentStatus == AppointmentStatus.SCHEDULED) {
            allowed = newStatus == AppointmentStatus.CONFIRMED
                    || newStatus == AppointmentStatus.CANCELLED;
        } else if (currentStatus == AppointmentStatus.CONFIRMED) {
            allowed = newStatus == AppointmentStatus.COMPLETED
                    || newStatus == AppointmentStatus.CANCELLED;
        }

        if (!allowed) {
            return "Cannot change status from " + currentStatus + " to " + newStatus;
        }

        appointment.setStatus(newStatus);
        appointmentRepo.save(appointment);
        return "Appointment status updated successfully";
    }

    public String rescheduleAppointment(UUID id, LocalDate newDate) {

        Optional<Appointment> existingAppointment = appointmentRepo.findById(id);

        if (existingAppointment.isEmpty()) {
            return "The appointment with that id does not exist";
        }

        Appointment appointment = existingAppointment.get();
        AppointmentStatus currentStatus = appointment.getStatus();

        if (currentStatus == AppointmentStatus.COMPLETED || currentStatus == AppointmentStatus.CANCELLED) {
            return "A " + currentStatus + " appointment cannot be rescheduled";
        }

        if (isPastDate(newDate)) {
            return "Appointment date cannot be in the past";
        }

        if (appointment.getDoctor() != null
                && isDoctorBooked(appointment.getDoctor().getId(), newDate)) {
            return "Doctor is already booked on that date";
        }

        appointment.setAppointmentDate(newDate);
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointmentRepo.save(appointment);
        return "Appointment rescheduled successfully";
    }

    public List<Appointment> getDoctorAgenda(UUID doctorId, LocalDate date) {
        return appointmentRepo.findByDoctorIdAndAppointmentDateAndStatusNot(doctorId, date,
                AppointmentStatus.CANCELLED);
    }

    public List<Appointment> getUpcomingAppointmentsOfPatient(String ssNumber) {
        List<AppointmentStatus> statuses = List.of(AppointmentStatus.SCHEDULED, AppointmentStatus.CONFIRMED);
        return appointmentRepo
                .findByPatientSsNumberAndAppointmentDateGreaterThanEqualAndStatusInOrderByAppointmentDateAsc(
                        ssNumber, LocalDate.now(), statuses);
    }

    public List<Object[]> getDoctorWorkload(LocalDate start, LocalDate end) {
        return appointmentRepo.findDoctorWorkload(start, end);
    }

}
