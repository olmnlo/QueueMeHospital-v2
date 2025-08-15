package org.example.queuemehospital.Repository;

import org.example.queuemehospital.Model.Scheduler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchedulerRepository extends JpaRepository<Scheduler, Integer> {
    Scheduler findSchedulerByDoctorId(Integer doctorId);
}
