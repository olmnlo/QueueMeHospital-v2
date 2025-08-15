package org.example.queuemehospital.Controller;

import lombok.RequiredArgsConstructor;
import org.example.queuemehospital.Api.ApiResponse;
import org.example.queuemehospital.Model.Appointment;
import org.example.queuemehospital.Service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<?> getAllAppointment(){
        List<Appointment> appointments = appointmentService.getAllAppointment();
        return ResponseEntity.status(HttpStatus.OK).body(appointments);
    }


    @DeleteMapping("/{appointmentId}/user/{userId}")
    public ResponseEntity<?> deleteAppointment(@PathVariable Integer userId, @PathVariable Integer appointmentId){
        appointmentService.deleteAppointment(userId, appointmentId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("appointment deleted successfully"));
    }

    @PutMapping("/{doctorId}/update-status")
    public ResponseEntity<?> updateAppointment(@PathVariable Integer doctorId, @RequestParam String description ){
        appointmentService.processNextWaitingAppointment(doctorId, description);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("appointment updated successfully"));
    }


    @PostMapping("/{userId}/book/doctor/{doctorId}/{day}")
    public ResponseEntity<?> recordAppointment(@PathVariable Integer userId,@PathVariable Integer doctorId,@PathVariable LocalDate day){
        appointmentService.bookAppointment(userId, doctorId, day.atStartOfDay());
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("appointment recorded successfully"));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAppointmentByUserId(@PathVariable Integer userId){
        List<Appointment> appointments = appointmentService.getAppointmentByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(appointments);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<?> getAppointmentByDoctorId(@PathVariable Integer doctorId){
        List<Appointment> appointments = appointmentService.getAppointmentByDoctorId(doctorId);
        return ResponseEntity.status(HttpStatus.OK).body(appointments);
    }

}
