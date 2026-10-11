package kigali.clinic.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Office;
import kigali.clinic.rw.domain.Specialization;
import kigali.clinic.rw.repository.DoctorRepository;
import kigali.clinic.rw.repository.OfficeRepository;
import kigali.clinic.rw.repository.SpecializationRepository;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private SpecializationRepository specializationRepo;

    @Autowired
    private OfficeRepository officeRepo;

    public String saveDoctor(Doctor doctor) {

        if (doctor.getOffice() != null) {
            Optional<Doctor> doctorWithSameOffice = doctorRepo.findByOfficeId(doctor.getOffice().getId());

            if (doctorWithSameOffice.isPresent()) {
                return "this office is already taken by another doctor";
            }
        }

        doctorRepo.save(doctor);
        return "saved successfully";
    }

    public List<Doctor> getDoctorsBySpecialization(String name) {
        return doctorRepo.findDoctorsBySpecializationName(name);
    }

    public List<Doctor> getDoctorsWithoutOffice() {
        return doctorRepo.findDoctorsWithoutOffice();
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepo.findAll();
    }

    public Optional<Doctor> getDoctorById(UUID id) {
        return doctorRepo.findById(id);
    }

    public String updateDoctor(UUID id, Doctor doctor) {

        Optional<Doctor> existingDoctor = doctorRepo.findById(id);

        if (existingDoctor.isEmpty()) {
            return "we don't have doctor with that id";
        }

        Doctor doctorToUpdate = existingDoctor.get();

        if (doctor.getOffice() != null) {
            Optional<Doctor> doctorWithSameOffice = doctorRepo.findByOfficeId(doctor.getOffice().getId());

            if (doctorWithSameOffice.isPresent() && !doctorWithSameOffice.get().getId().equals(id)) {
                return "this office is already taken by another doctor";
            }
        }

        doctorToUpdate.setFirstName(doctor.getFirstName());
        doctorToUpdate.setLastName(doctor.getLastName());
        doctorToUpdate.setDateOfBirth(doctor.getDateOfBirth());
        doctorToUpdate.setOffice(doctor.getOffice());
        doctorToUpdate.setSpecializations(doctor.getSpecializations());

        doctorRepo.save(doctorToUpdate);
        return "updated successfully";
    }

    public String deleteDoctor(UUID id) {

        Optional<Doctor> existingDoctor = doctorRepo.findById(id);

        if (existingDoctor.isEmpty()) {
            return "we don't have doctor with that id";
        }

        doctorRepo.deleteById(id);
        return "deleted successfully";
    }

    @Transactional
    public String addSpecialization(UUID doctorId, UUID specializationId) {

        Optional<Doctor> existingDoctor = doctorRepo.findById(doctorId);

        if (existingDoctor.isEmpty()) {
            return "The doctor with that id does not exist";
        }

        Optional<Specialization> existingSpecialization = specializationRepo.findById(specializationId);

        if (existingSpecialization.isEmpty()) {
            return "That specialization does not exist";
        }

        Doctor doctor = existingDoctor.get();
        Specialization specialization = existingSpecialization.get();

        if (doctor.getSpecializations().contains(specialization)) {
            return "The doctor already has that specialization";
        }

        doctor.getSpecializations().add(specialization);
        doctorRepo.save(doctor);
        return "Specialization added to the doctor";
    }

    @Transactional
    public String removeSpecialization(UUID doctorId, UUID specializationId) {

        Optional<Doctor> existingDoctor = doctorRepo.findById(doctorId);

        if (existingDoctor.isEmpty()) {
            return "The doctor with that id does not exist";
        }

        Optional<Specialization> existingSpecialization = specializationRepo.findById(specializationId);

        if (existingSpecialization.isEmpty()) {
            return "That specialization does not exist";
        }

        Doctor doctor = existingDoctor.get();
        Specialization specialization = existingSpecialization.get();

        if (!doctor.getSpecializations().contains(specialization)) {
            return "The doctor does not have that specialization";
        }

        doctor.getSpecializations().remove(specialization);
        doctorRepo.save(doctor);
        return "Specialization removed from the doctor";
    }

    @Transactional
    public String changeOffice(UUID doctorId, int officeNumber) {

        Optional<Doctor> existingDoctor = doctorRepo.findById(doctorId);

        if (existingDoctor.isEmpty()) {
            return "The doctor with that id does not exist";
        }

        Optional<Office> existingOffice = officeRepo.findByOfficeNumber(officeNumber);

        if (existingOffice.isEmpty()) {
            return "The office with that office number does not exist";
        }

        Office office = existingOffice.get();

        if (doctorRepo.existsByOfficeIdAndIdNot(office.getId(), doctorId)) {
            return "That office is taken by other doctor";
        }

        Doctor doctor = existingDoctor.get();
        doctor.setOffice(office);
        doctorRepo.save(doctor);
        return "Office changed successfully";
    }

    public List<Doctor> searchDoctorsByName(String name) {
        return doctorRepo.searchDoctorsByName(name);
    }

}
