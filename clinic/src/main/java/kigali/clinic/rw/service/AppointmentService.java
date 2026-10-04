package kigali.clinic.rw.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.repository.AppointmentRepository;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepo;

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
        return appointmentRepo.save(appointment);
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
        appointmentToUpdate.setStatus(appointment.getStatus());
        appointmentToUpdate.setPatient(appointment.getPatient());
        appointmentToUpdate.setDoctor(appointment.getDoctor());

        appointmentRepo.save(appointmentToUpdate);
        return "updated successfully";
    }

    public String deleteAppointment(UUID id) {

        Optional<Appointment> existingAppointment = appointmentRepo.findById(id);

        if (existingAppointment.isEmpty()) {
            return "we don't have appointment with that id";
        }

        appointmentRepo.deleteById(id);
        return "deleted successfully";
    }

    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
        return appointmentRepo.findByStatusOrderByAppointmentDateAsc(status);
    }

    public List<Appointment> getAppointmentsBetween(Date start, Date end) {
        return appointmentRepo.findByAppointmentDateBetweenOrderByAppointmentDateAsc(start, end);
    }

    public boolean isDoctorBooked(UUID doctorId, Date date) {
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
    public String cancelDoctorDay(UUID doctorId, Date date) {
        int updated = appointmentRepo.cancelDoctorDay(doctorId, date);
        return updated + " appointments cancelled";
    }

    public Page<Appointment> getAppointmentsPage(Pageable pageable) {
        return appointmentRepo.findAll(pageable);
    }

    @Transactional
    public String deleteCancelledBefore(Date date) {
        int deleted = appointmentRepo.deleteCancelledBefore(date);
        return deleted + " appointments deleted";
    }

}
