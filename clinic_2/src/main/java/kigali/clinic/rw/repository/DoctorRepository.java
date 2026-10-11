package kigali.clinic.rw.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kigali.clinic.rw.domain.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {

    Optional<Doctor> findByOfficeId(UUID officeId);

    @Query("SELECT d FROM Doctor d JOIN d.specializations s WHERE LOWER(s.name) = LOWER(:name)")
    List<Doctor> findDoctorsBySpecializationName(@Param("name") String name);

    @Query("SELECT d FROM Doctor d WHERE d.office IS NULL ORDER BY d.lastName")
    List<Doctor> findDoctorsWithoutOffice();

    boolean existsByOfficeIdAndIdNot(UUID officeId, UUID doctorId);

    @Query("SELECT d FROM Doctor d " +
            "WHERE LOWER(d.firstName) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "OR LOWER(d.lastName) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "ORDER BY d.lastName")
    List<Doctor> searchDoctorsByName(@Param("name") String name);

}
