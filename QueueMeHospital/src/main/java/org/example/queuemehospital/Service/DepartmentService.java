package org.example.queuemehospital.Service;

import lombok.RequiredArgsConstructor;
import org.example.queuemehospital.Api.ApiException;
import org.example.queuemehospital.Model.Admin;
import org.example.queuemehospital.Model.Department;
import org.example.queuemehospital.Model.Doctor;
import org.example.queuemehospital.Model.Hospital;
import org.example.queuemehospital.Repository.AdminRepository;
import org.example.queuemehospital.Repository.DepartmentRepository;
import org.example.queuemehospital.Repository.DoctorRepository;
import org.example.queuemehospital.Repository.HospitalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final AdminRepository adminRepository;
    private final HospitalRepository hospitalRepository;
    private final DoctorRepository doctorRepository;

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public void addDepartment(Integer adminId, Department department) {
        List<Department> oldDepartments = departmentRepository.findDepartmentByName(department.getName());
        if (oldDepartments != null && !oldDepartments.isEmpty()) {
            for (Department d : oldDepartments) {
                if (d.getHospitalId().equals(department.getHospitalId())) {
                    throw new ApiException("department is duplicated");
                }
            }
        }

        Hospital hospital = hospitalRepository.findHospitalById(department.getHospitalId());
        if (hospital == null) {
            throw new ApiException("hospital is not found");
        }

        Admin admin = adminRepository.findAdminById(adminId);
        if (admin == null) {
            throw new ApiException("user is not found");
        }
        departmentRepository.save(department);
    }

    public void updateDepartment(Integer adminId, Integer departmentId, Department department) {
        Department oldDepartment = departmentRepository.findDepartmentById(departmentId);
        if (oldDepartment == null) {
            throw new ApiException("department not found");
        }
        List<Department> oldDepartments = departmentRepository.findDepartmentByName(department.getName());
        if (oldDepartments != null && !oldDepartments.isEmpty()) {
            for (Department d : oldDepartments) {
                if (d.getHospitalId().equals(department.getHospitalId())) {
                    throw new ApiException("department is duplicated");
                }
            }
        }

        Admin admin = adminRepository.findAdminById(adminId);
        if (admin == null) {
            throw new ApiException("user is not found");
        }

        oldDepartment.setDescription(department.getDescription());
        oldDepartment.setName(oldDepartment.getName());
        oldDepartment.setHospitalId(oldDepartment.getHospitalId());
    }

    public void deleteDepartment(Integer adminId, Integer departmentId) {
        Department department = departmentRepository.findDepartmentById(departmentId);
        if (department == null) {
            throw new ApiException("department not found");
        }

        Admin admin = adminRepository.findAdminById(adminId);
        if (admin == null) {
            throw new ApiException("user is not found");
        }

        List<Doctor> doctors = doctorRepository.findDoctorsByDepartmentId(departmentId);

        if (!doctors.isEmpty()){
            throw new ApiException("you must delete all doctors with this department id");
        }

        departmentRepository.delete(department);
    }

    public List<Doctor> getDoctorsByDepartmentId(Integer departmentId){
        return doctorRepository.findDoctorsByDepartmentId(departmentId);
    }
}
