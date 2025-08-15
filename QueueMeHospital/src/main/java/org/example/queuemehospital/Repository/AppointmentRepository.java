package org.example.queuemehospital.Repository;

import org.example.queuemehospital.Model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    Appointment findAppointmentById(Integer id);
    @Query("SELECT a FROM Appointment a WHERE a.status = 'waiting' and a.doctorId =:doctorId")
    List<Appointment> findAppointmentByDoctorId(Integer doctorId);
    List<Appointment> findAppointmentsByUserId(Integer userId);
    @Query("SELECT a FROM Appointment a WHERE a.doctorId = :doctorId AND a.status = 'waiting' AND a.appointmentDay >= CURRENT_DATE ORDER BY a.appointmentDay ASC")
    List<Appointment> findWaitingAppointmentsByDoctorId(@Param("doctorId") Integer doctorId);
    @Modifying
    @Query("UPDATE Appointment a SET a.status = 'done' WHERE a.id = :appointmentId")
    int updateAppointmentStatusToDone(@Param("appointmentId") Integer appointmentId);
    List<Appointment> findAppointmentsByDoctorIdAndUserId(Integer doctorId, Integer userId);
    boolean existsByUserIdAndDoctorIdAndAppointmentDayBetween(Integer userId, Integer doctorId, LocalDateTime startOfDay, LocalDateTime nextDayStart);
    @Query("SELECT MAX(a.appointmentDay) FROM Appointment a " +
            "WHERE a.doctorId = :doctorId AND a.appointmentDay >= :startOfDay AND a.appointmentDay < :endOfDay")
    Optional<LocalDateTime> findMaxAppointmentDayByDoctorIdAndDate(
            @Param("doctorId") Integer doctorId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );
    List<Appointment> findAppointmentsByDoctorId(Integer doctorId);
}
