package org.example.queuemehospital.Repository;

import org.example.queuemehospital.Model.WaitingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaitingListRepository extends JpaRepository<WaitingList,Integer> {
    List<WaitingList> findWaitingListByDoctorId(Integer appointmentId);
    WaitingList findWaitingListByAppointmentId(Integer appointmentId);
    @Query("SELECT w.numberInQueue FROM WaitingList w WHERE w.appointmentId = :appointmentId")
    Integer findNumberInQueueByAppointmentId(@Param("appointmentId") Integer appointmentId);
    @Modifying
    @Query("""
    UPDATE WaitingList w 
    SET w.numberInQueue = w.numberInQueue - 1 
    WHERE w.doctorId = :doctorId AND w.numberInQueue > :numberInQueue
""")
    int decrementNumberInQueueAfter(@Param("doctorId") Integer doctorId, @Param("numberInQueue") Integer numberInQueue);
    List<WaitingList> findByDoctorIdOrderByNumberInQueueAsc(Integer doctorId);
    List<WaitingList> findWaitingListsByUserId(Integer userId);
}
