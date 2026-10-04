package kigali.clinic.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.repository.DoctorRepository;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepo;

    public String saveDoctor(Doctor doctor){

        if(doctor.getOffice() != null){
            Optional<Doctor> doctorWithSameOffice = doctorRepo.findByOfficeId(doctor.getOffice().getId());

            if(doctorWithSameOffice.isPresent()){
                return "this office is already taken by another doctor";
            }
        }

        doctorRepo.save(doctor);
        return "saved successfully";
    }

    public List<Doctor> getDoctorsBySpecialization(String name){
        return doctorRepo.findDoctorsBySpecializationName(name);
    }

    public List<Doctor> getDoctorsWithoutOffice(){
        return doctorRepo.findDoctorsWithoutOffice();
    }

    public List<Doctor> getAllDoctors(){
        return doctorRepo.findAll();
    }

    public Optional<Doctor> getDoctorById(UUID id){
        return doctorRepo.findById(id);
    }

    public String updateDoctor(UUID id, Doctor doctor){

        Optional<Doctor> existingDoctor = doctorRepo.findById(id);

        if(existingDoctor.isEmpty()){
            return "we don't have doctor with that id";
        }

        Doctor doctorToUpdate = existingDoctor.get();

        if(doctor.getOffice() != null){
            Optional<Doctor> doctorWithSameOffice = doctorRepo.findByOfficeId(doctor.getOffice().getId());

            if(doctorWithSameOffice.isPresent() && !doctorWithSameOffice.get().getId().equals(id)){
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

    public String deleteDoctor(UUID id){

        Optional<Doctor> existingDoctor = doctorRepo.findById(id);

        if(existingDoctor.isEmpty()){
            return "we don't have doctor with that id";
        }

        doctorRepo.deleteById(id);
        return "deleted successfully";
    }

}
