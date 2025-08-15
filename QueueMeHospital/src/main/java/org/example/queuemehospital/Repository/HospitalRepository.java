package org.example.queuemehospital.Repository;

import org.example.queuemehospital.Model.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Integer> {
    Hospital findHospitalByName(String name);
    Hospital findHospitalById(Integer hospitalId);
}
