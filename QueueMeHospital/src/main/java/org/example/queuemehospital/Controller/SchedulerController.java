package org.example.queuemehospital.Controller;

import lombok.RequiredArgsConstructor;
import org.example.queuemehospital.Api.ApiResponse;
import org.example.queuemehospital.Service.SchedulerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/scheduler")
public class SchedulerController {

    private final SchedulerService schedulerService;

    @GetMapping("/{adminId}")
    public ResponseEntity<?> getAllSchedules(@PathVariable Integer adminId){
        return ResponseEntity.status(HttpStatus.OK).body(schedulerService.getAllScheduler(adminId));
    }

    @PostMapping("/{adminId}/doctor/{doctorId}/{day}")
    public ResponseEntity<?> scheduleDoctor(@PathVariable Integer adminId,@PathVariable Integer doctorId, @PathVariable LocalDate day){
        schedulerService.scheduleDoctorsDays(adminId, doctorId, day);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("doctor schedule done"));
    }

    @PostMapping("/{adminId}/reset")
    public ResponseEntity<?> resetDoctorSchedule(@PathVariable Integer adminId){
        schedulerService.resetDoctorsSchedule(adminId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("doctor schedule reset successfully"));
    }

    @GetMapping("/{doctorId}/schedule")
    public ResponseEntity<?> getScheduleByDoctorId(@PathVariable Integer doctorId){
        return ResponseEntity.status(HttpStatus.OK).body(schedulerService.getScheduleByDoctorId(doctorId));
    }




}
