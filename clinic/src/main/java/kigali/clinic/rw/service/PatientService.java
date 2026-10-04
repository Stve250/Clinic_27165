package kigali.clinic.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Patient;
import kigali.clinic.rw.repository.PatientRepository;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepo;

    public List<Patient> getPatientsByLastName(String lastName){
        return patientRepo.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }

    public List<Patient> getPatientsOfDoctor(UUID doctorId){
        return patientRepo.findPatientsByDoctorId(doctorId);
    }

    public List<Patient> getFrequentPatients(long min){
        return patientRepo.findFrequentPatients(min);
    }

    public Patient savePatient(Patient patient){
        return patientRepo.save(patient);
    }

    public List<Patient> getAllPatients(){
        return patientRepo.findAll();
    }

    public Optional<Patient> getPatientById(UUID id){
        return patientRepo.findById(id);
    }

    public String updatePatient(UUID id, Patient patient){

        Optional<Patient> existingPatient = patientRepo.findById(id);

        if(existingPatient.isEmpty()){
            return "we don't have patient with that id";
        }

        Patient patientToUpdate = existingPatient.get();
        patientToUpdate.setFirstName(patient.getFirstName());
        patientToUpdate.setLastName(patient.getLastName());
        patientToUpdate.setDateOfBirth(patient.getDateOfBirth());

        patientRepo.save(patientToUpdate);
        return "updated successfully";
    }

    public String deletePatient(UUID id){

        Optional<Patient> existingPatient = patientRepo.findById(id);

        if(existingPatient.isEmpty()){
            return "we don't have patient with that id";
        }

        patientRepo.deleteById(id);
        return "deleted successfully";
    }

}
