package org.example.queuemehospital.Repository;

import org.example.queuemehospital.Model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    Department findDepartmentById(Integer id);
    List<Department> findDepartmentByName(String name);
    List<Department> findDepartmentsByHospitalId(Integer hospitalId);
}
