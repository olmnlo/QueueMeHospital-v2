package org.example.queuemehospital.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.queuemehospital.Api.ApiResponse;
import org.example.queuemehospital.Model.Doctor;
import org.example.queuemehospital.Model.Hospital;
import org.example.queuemehospital.Model.User;
import org.example.queuemehospital.Model.WaitingList;
import org.example.queuemehospital.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getUsers(){
        List<User> users = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @PostMapping
    public ResponseEntity<?> addUser(@Valid @RequestBody User user){
        userService.addUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("user added successfully"));
    }

    @PutMapping("/{adminId}/update/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Integer adminId, @PathVariable Integer userId ,@Valid@RequestBody User user){
        userService.updateUser(adminId,userId,user);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("user updated successfully"));
    }

    @DeleteMapping("/{adminId}/delete/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer adminId, @PathVariable Integer userId){
        userService.deleteUser(adminId,userId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("user deleted successfully"));
    }

    @GetMapping("/{userId}/nearest-hospital")
    public ResponseEntity<?> getNearestHospitals(@PathVariable Integer userId) {
        List<Hospital> hospitals = userService.findNearestHospitalsByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(hospitals);
    }

    @GetMapping("/doctor-hospital/{hospitalId}")
    public ResponseEntity<?> getDoctorFromHospital(@PathVariable Integer hospitalId) {
        List<Doctor> doctors = userService.getDoctorsFromHospitalId(hospitalId);
        return ResponseEntity.status(HttpStatus.OK).body(doctors);
    }

    @GetMapping("/{userId}/waiting")
    public ResponseEntity<?> getWaitingListByUserId(@PathVariable Integer userId) {
        List<WaitingList> waitingLists = userService.getWaitingListsByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(waitingLists);
    }

}
