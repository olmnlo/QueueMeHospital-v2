package org.example.queuemehospital.Service;

import lombok.RequiredArgsConstructor;
import org.example.queuemehospital.Api.ApiException;
import org.example.queuemehospital.Model.Admin;
import org.example.queuemehospital.Model.Department;
import org.example.queuemehospital.Model.Hospital;
import org.example.queuemehospital.Repository.AdminRepository;
import org.example.queuemehospital.Repository.DepartmentRepository;
import org.example.queuemehospital.Repository.HospitalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final AdminRepository adminRepository;
    private final DepartmentRepository departmentRepository;
    private final FetchHospitalsService fetchHospitalsService;

    public List<Hospital> getAllHospitals() {
        return hospitalRepository.findAll();
    }

    public void addHospital(Integer adminId, Hospital hospital) {
        Hospital oldHospital = hospitalRepository.findHospitalByName(hospital.getName());
        if (oldHospital != null) {
            throw new ApiException("hospital is duplicated");
        }
        Admin admin = adminRepository.findAdminById(adminId);
        if (admin == null) {
            throw new ApiException("user is not found");
        }

        hospitalRepository.save(hospital);
    }

    public void updateHospital(Integer adminId, Integer hospitalId, Hospital hospital) {
        Hospital oldHospital = hospitalRepository.findHospitalById(hospitalId);
        if (oldHospital == null) {
            throw new ApiException("hospital not found");
        }
        Admin admin = adminRepository.findAdminById(adminId);
        if (admin == null) {
            throw new ApiException("user is not found");
        }

        oldHospital.setName(hospital.getName());
        oldHospital.setLongitude(hospital.getLongitude());
        oldHospital.setLatitude(hospital.getLatitude());

        hospitalRepository.save(oldHospital);
    }

    public void deleteHospital(Integer adminId, Integer hospitalId) {
        Hospital hospital = hospitalRepository.findHospitalById(hospitalId);
        if (hospital == null) {
            throw new ApiException("hospital not found");
        }
        Admin admin = adminRepository.findAdminById(adminId);
        if (admin == null) {
            throw new ApiException("user is not found");
        }

        List<Department> department = departmentRepository.findDepartmentsByHospitalId(hospital.getId());

        if (!department.isEmpty()) {
            throw new ApiException("you must delete all department with this hospital id");
        }

        hospitalRepository.delete(hospital);
    }


    public void fetchAndSaveHospitalsFromOSM(double lat, double lon, double radiusKm) {
        List<Hospital> hospitals = fetchHospitalsService.fetchAndSaveHospitalsFromOSM(lat,lon,radiusKm);

        for (Hospital h : hospitals){
            addHospital(1,h);
        }
    }

}
