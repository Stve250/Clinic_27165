package kigali.clinic.rw.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

       List<Appointment> findByDoctorIdAndStatusOrderByAppointmentDateAsc(UUID doctorId, AppointmentStatus status);

       @Query("SELECT a FROM Appointment a " +
                     "JOIN a.doctor d " +
                     "JOIN d.specializations s " +
                     "WHERE s.name = :specializationName")
       List<Appointment> findByDoctorSpecialization(@Param("specializationName") String specializationName);

       Page<Appointment> findByDoctorId(UUID doctorId, Pageable pageable);

       List<Appointment> findByStatusOrderByAppointmentDateAsc(AppointmentStatus status);

       List<Appointment> findByAppointmentDateBetweenOrderByAppointmentDateAsc(LocalDate start, LocalDate end);

       boolean existsByDoctorIdAndAppointmentDateAndStatusNot(UUID doctorId, LocalDate appointmentDate,
                     AppointmentStatus status);

       @Query("SELECT a.status, COUNT(a) FROM Appointment a GROUP BY a.status")
       List<Object[]> countAppointmentsByStatus();

       @Query("SELECT o.name, o.officeNumber, COUNT(a) " +
                     "FROM Appointment a JOIN a.doctor d JOIN d.office o " +
                     "GROUP BY o.id, o.name, o.officeNumber " +
                     "ORDER BY COUNT(a) DESC, o.officeNumber ASC")
       List<Object[]> findBusiestOffice(Pageable pageable);

       @Modifying(clearAutomatically = true)
       @Query("UPDATE Appointment a " +
                     "SET a.status = kigali.clinic.rw.domain.AppointmentStatus.CANCELLED " +
                     "WHERE a.doctor.id = :doctorId AND a.appointmentDate = :date " +
                     "AND a.status <> kigali.clinic.rw.domain.AppointmentStatus.COMPLETED")
       int cancelDoctorDay(@Param("doctorId") UUID doctorId, @Param("date") LocalDate date);

       @Modifying
       @Query("DELETE FROM Appointment a " +
                     "WHERE a.status = kigali.clinic.rw.domain.AppointmentStatus.CANCELLED " +
                     "AND a.appointmentDate < :date")
       int deleteCancelledBefore(@Param("date") LocalDate date);

       List<Appointment> findByDoctorIdAndAppointmentDateAndStatusNot(
                     UUID doctorId, LocalDate date, AppointmentStatus status);

       List<Appointment> findByPatientSsNumberAndAppointmentDateGreaterThanEqualAndStatusInOrderByAppointmentDateAsc(
                     String ssNumber, LocalDate date, List<AppointmentStatus> statuses);

       long countByStatus(AppointmentStatus status);

       long countByAppointmentDate(LocalDate date);

       @Query("SELECT d.firstName, d.lastName, COUNT(a) " +
                     "FROM Appointment a JOIN a.doctor d " +
                     "WHERE a.status = kigali.clinic.rw.domain.AppointmentStatus.COMPLETED " +
                     "AND a.appointmentDate BETWEEN :start AND :end " +
                     "GROUP BY d.id, d.firstName, d.lastName " +
                     "ORDER BY COUNT(a) DESC, d.lastName ASC")
       List<Object[]> findDoctorWorkload(@Param("start") LocalDate start, @Param("end") LocalDate end);

}
