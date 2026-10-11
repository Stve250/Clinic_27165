package kigali.clinic.rw.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kigali.clinic.rw.domain.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

    List<Patient> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);

    @Query("SELECT DISTINCT p FROM Patient p JOIN p.appointments a WHERE a.doctor.id = :doctorId")
    List<Patient> findPatientsByDoctorId(@Param("doctorId") UUID doctorId);

    @Query("SELECT p FROM Patient p JOIN p.appointments a " +
            "GROUP BY p.id, p.firstName, p.lastName, p.dateOfBirth " +
            "HAVING COUNT(a) >= :min " +
            "ORDER BY COUNT(a) DESC")
    List<Patient> findFrequentPatients(@Param("min") long min);

    Optional<Patient> findBySsNumber(String ssNumber);

}
