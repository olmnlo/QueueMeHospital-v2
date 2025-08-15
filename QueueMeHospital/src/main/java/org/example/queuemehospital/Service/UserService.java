package org.example.queuemehospital.Service;

import lombok.RequiredArgsConstructor;
import org.example.queuemehospital.Api.ApiException;
import org.example.queuemehospital.Model.*;
import org.example.queuemehospital.Repository.*;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final DoctorRepository doctorRepository;
    private final WaitingListRepository waitingListRepository;
    private final AppointmentRepository appointmentRepository;
    private final HospitalRepository hospitalRepository;
    private final AdminRepository adminRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void addUser(User user) {
        User oldUser = userRepository.findUserByUsername(user.getUsername());
        if (oldUser != null) {
            throw new ApiException("user is duplicated");
        }
        userRepository.save(user);
    }

    public void updateUser(Integer adminId, Integer userId, User user) {
        User oldUser = userRepository.findUserById(userId);
        if (oldUser == null) {
            throw new ApiException("user not found");
        }
        Admin admin = adminRepository.findAdminById(adminId);
        if (admin == null) {
            throw new ApiException("admin not found");
        }
        if (!oldUser.getEmail().equals(user.getEmail()) || !oldUser.getUsername().equals(user.getUsername())) {
            throw new ApiException("email or username duplicated");
        }
        oldUser.setName(user.getName());
        oldUser.setUsername(user.getUsername());
        oldUser.setPassword(user.getPassword());
        oldUser.setEmail(user.getEmail());
        oldUser.setAge(user.getAge());
        oldUser.setBloodType(user.getBloodType());
        oldUser.setLatitude(user.getLatitude());
        oldUser.setLongitude(user.getLongitude());
        oldUser.setRole(user.getRole());
        userRepository.save(oldUser);
    }

    public void deleteUser(Integer adminId, Integer userId) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new ApiException("user not found");
        }

        Admin admin = adminRepository.findAdminById(adminId);
        if (admin == null) {
            throw new ApiException("user not found");
        }

        List<Appointment> appointments = appointmentRepository.findAppointmentsByUserId(userId);
        if (!appointments.isEmpty()) {
            throw new ApiException("you must delete all appointment with this user id");
        }
        userRepository.delete(user);
    }



    public List<Hospital> findNearestHospitalsByUserId(Integer userId) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new ApiException("User not found");
        }

        double userLat = user.getLatitude();
        double userLon = user.getLongitude();

        List<Hospital> hospitals = hospitalRepository.findAll();

        // Sort hospitals by calculated distance
        hospitals.sort(Comparator.comparingDouble(hospital
                -> calculateDistance(userLat, userLon, hospital.getLatitude(), hospital.getLongitude())
        ));

        return hospitals;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    public List<Doctor> getDoctorsFromHospitalId(Integer hospitalId) {
        Hospital hospital = hospitalRepository.findHospitalById(hospitalId);
        if (hospital == null) {
            throw new ApiException("hospital not found");
        }
        return doctorRepository.findDoctorsByHospitalId(hospitalId);
    }

    public List<WaitingList> getWaitingListsByUserId(Integer userId){
        List<WaitingList> waitingLists = waitingListRepository.findWaitingListsByUserId(userId);
        return waitingLists;
    }
}
