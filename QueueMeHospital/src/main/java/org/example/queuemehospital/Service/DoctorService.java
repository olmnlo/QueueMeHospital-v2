package org.example.queuemehospital.Service;

import lombok.RequiredArgsConstructor;
import org.example.queuemehospital.Api.ApiException;
import org.example.queuemehospital.Model.Admin;
import org.example.queuemehospital.Model.Appointment;
import org.example.queuemehospital.Model.Department;
import org.example.queuemehospital.Model.Doctor;
import org.example.queuemehospital.Repository.AdminRepository;
import org.example.queuemehospital.Repository.AppointmentRepository;
import org.example.queuemehospital.Repository.DepartmentRepository;
import org.example.queuemehospital.Repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    private final DepartmentRepository departmentRepository;
    private final AdminRepository adminRepository;
    private final AppointmentRepository appointmentRepository;

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public void addDoctor(Integer adminId, Doctor doctor) {
        Doctor oldDoctor = doctorRepository.findDoctorByUsername(doctor.getName());
        if (oldDoctor != null) {
            throw new ApiException("doctor is duplicated");
        }

        Department department = departmentRepository.findDepartmentById(doctor.getDepartmentId());
        if (department == null) {
            throw new ApiException("department not found");
        }

        Admin admin = adminRepository.findAdminById(adminId);
        if (admin == null) {
            throw new ApiException("user is not found");
        }
        doctorRepository.save(doctor);
    }

    public void updateDoctor(Integer adminId, Integer doctorId, Doctor doctor) {
        Doctor oldDoctor = doctorRepository.findDoctorById(doctorId);
        if (oldDoctor == null) {
            throw new ApiException("doctor not found");
        }
        Admin admin = adminRepository.findAdminById(adminId);
        if (admin == null) {
            throw new ApiException("user is not found");
        }

        oldDoctor.setName(doctor.getName());
        oldDoctor.setUsername(doctor.getUsername());
        oldDoctor.setSpecialization(doctor.getSpecialization());
        oldDoctor.setIsLeave(doctor.getIsLeave());
        oldDoctor.setDepartmentId(doctor.getDepartmentId());

        doctorRepository.save(oldDoctor);
    }

    public void deleteDoctor(Integer adminId, Integer doctorId) {
        Doctor doctor = doctorRepository.findDoctorById(doctorId);
        if (doctor == null) {
            throw new ApiException("doctor not found");
        }
        Admin admin = adminRepository.findAdminById(adminId);
        if (admin == null) {
            throw new ApiException("user is not found");
        }

        List<Appointment> appointment = appointmentRepository.findAppointmentByDoctorId(doctorId);
        if (!appointment.isEmpty()){
            throw new ApiException("you must delete all appointment with this docter id");
        }
        doctorRepository.delete(doctor);
    }

    public void updateLeave(Integer adminId, Integer doctorId){
        Doctor doctor = doctorRepository.findDoctorById(doctorId);
        if (doctor == null){
            throw new ApiException("doctor not found");
        }

        Admin admin = adminRepository.findAdminById(adminId);
        if (admin == null) {
            throw new ApiException("user is not found");
        }

        doctor.setIsLeave(!doctor.getIsLeave());

        doctorRepository.save(doctor);
    }

    public List<Appointment> showAppointmentByDoctorId(Integer doctorId){
        List<Appointment> appointments = appointmentRepository.findAppointmentByDoctorId(doctorId);
        return appointments;
    }

    public List<Appointment> showAppointmentByUserId(Integer doctorId,Integer userId){
        List<Appointment> appointments = appointmentRepository.findAppointmentsByDoctorIdAndUserId(doctorId,userId);
        return appointments;
    }


}
