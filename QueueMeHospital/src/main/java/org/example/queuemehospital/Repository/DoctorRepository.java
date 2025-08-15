package org.example.queuemehospital.Repository;

import org.example.queuemehospital.Model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor,Integer> {
    Doctor findDoctorByUsername(String username);
    Doctor findDoctorById(Integer id);
    List<Doctor> findDoctorsByDepartmentId(Integer departmentId);
    List<Doctor> findDoctorsByHospitalId(Integer hospitalId);
}
