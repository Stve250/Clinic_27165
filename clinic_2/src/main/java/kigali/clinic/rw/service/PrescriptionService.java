package kigali.clinic.rw.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.domain.Prescription;
import kigali.clinic.rw.repository.AppointmentRepository;
import kigali.clinic.rw.repository.PrescriptionRepository;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    public String savePrescription(UUID appointmentId, Prescription prescription) {

        Optional<Appointment> existingAppointment = appointmentRepo.findById(appointmentId);

        if (existingAppointment.isEmpty()) {
            return "The appointment with that id does not exist";
        }

        Appointment appointment = existingAppointment.get();

        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            return "Prescriptions are only for COMPLETED appointments";
        }

        prescription.setIssuedDate(LocalDate.now());
        prescription.setAppointment(appointment);

        prescriptionRepo.save(prescription);
        return "Prescription saved successfully";
    }

    public List<Prescription> getPrescriptionsByAppointment(Appointment appointment) {
        return prescriptionRepo.findByAppointment(appointment);
    }

    public List<Prescription> getPrescriptionsOfPatient(String ssNumber) {
        return prescriptionRepo.findPrescriptionsByPatientSsNumber(ssNumber);
    }

    public String updatePrescription(UUID id, Prescription prescription) {

        Optional<Prescription> existingPrescription = prescriptionRepo.findById(id);

        if (existingPrescription.isEmpty()) {
            return "The prescription with that id does not exist";
        }

        Prescription prescriptionToUpdate = existingPrescription.get();
        prescriptionToUpdate.setMedicineName(prescription.getMedicineName());
        prescriptionToUpdate.setDosage(prescription.getDosage());
        prescriptionToUpdate.setDurationDays(prescription.getDurationDays());

        prescriptionRepo.save(prescriptionToUpdate);
        return "Prescription updated successfully";
    }

    public String deletePrescription(UUID id) {

        Optional<Prescription> existingPrescription = prescriptionRepo.findById(id);

        if (existingPrescription.isEmpty()) {
            return "The prescription with that id does not exist";
        }

        prescriptionRepo.deleteById(id);
        return "Prescription deleted successfully";
    }

    public List<Object[]> getTopMedicines() {
        return prescriptionRepo.findTopMedicines();
    }

}
