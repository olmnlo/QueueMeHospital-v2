package org.example.queuemehospital.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.queuemehospital.Api.ApiResponse;
import org.example.queuemehospital.Model.Appointment;
import org.example.queuemehospital.Model.Doctor;
import org.example.queuemehospital.Service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/doctor")
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping
    public ResponseEntity<?> getDoctors(){
        List<Doctor> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    @PostMapping("/{adminId}")
    public ResponseEntity<?> addDoctor(@PathVariable Integer adminId, @Valid @RequestBody Doctor doctor){
        doctorService.addDoctor(adminId,doctor);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("doctor added successfully"));
    }

    @PutMapping("/{adminId}/update/{doctorId}")
    public ResponseEntity<?> updateDoctor(@PathVariable Integer adminId,@PathVariable Integer doctorId, @Valid @RequestBody Doctor doctor){
        doctorService.updateDoctor(adminId,doctorId, doctor);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("doctor updated successfully"));
    }

    @DeleteMapping("/{adminId}/delete/{doctorId}")
    public ResponseEntity<?> deleteDoctor(@PathVariable Integer adminId, @PathVariable Integer doctorId){
        doctorService.deleteDoctor(adminId,doctorId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("doctor deleted successfully"));
    }

    //request leave
    @PostMapping("/{doctorId}/request-leave/{adminId}")
    public ResponseEntity<?> updateIsLeave(@PathVariable Integer adminId,@PathVariable Integer doctorId){
        doctorService.updateLeave(adminId, doctorId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("doctor updated leave successfully"));
    }

    @GetMapping("/{doctorId}/appointments")
    public ResponseEntity<?> getAllAppointmentByDoctorId(@PathVariable Integer doctorId){
        List<Appointment> appointments = doctorService.showAppointmentByDoctorId(doctorId);
        return ResponseEntity.status(HttpStatus.OK).body(appointments);
    }

    @GetMapping("/{doctorId}/appointments/{userId}")
    public ResponseEntity<?> getAllAppointmentByDoctorId(@PathVariable Integer doctorId, @PathVariable Integer userId){
        List<Appointment> appointments = doctorService.showAppointmentByUserId(doctorId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(appointments);
    }
}
