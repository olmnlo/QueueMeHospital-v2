package org.example.queuemehospital.Service;

import lombok.RequiredArgsConstructor;
import org.example.queuemehospital.Api.ApiException;
import org.example.queuemehospital.Model.Admin;
import org.example.queuemehospital.Model.Doctor;
import org.example.queuemehospital.Model.Scheduler;
import org.example.queuemehospital.Repository.AdminRepository;
import org.example.queuemehospital.Repository.DoctorRepository;
import org.example.queuemehospital.Repository.SchedulerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final SchedulerRepository schedulerRepository;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;

    public List<Scheduler> getAllScheduler(Integer adminId){
        if (adminRepository.findAdminById(adminId) == null){
            throw new ApiException("you are not admin");
        }
        return schedulerRepository.findAll();
    }

    public void scheduleDoctorsDays(Integer adminId, Integer doctorId, LocalDate day) {
        LocalDateTime startDay = day.atStartOfDay();
        LocalDateTime today = LocalDate.now().atStartOfDay();

        if (startDay.isBefore(today)) {
            throw new ApiException("Date cannot be in the past");
        }
        Admin admin = adminRepository.findAdminById(adminId);
        if (admin == null){
            throw new ApiException("user not found");
        }

        LocalDate date;
        try {
            date = startDay.toLocalDate();
        } catch (DateTimeParseException e) {
            throw new ApiException("Invalid date format, expected yyyy-MM-dd");
        }

        String dayName = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        Doctor doctor = doctorRepository.findDoctorById(doctorId);
        if (doctor == null){
            throw new ApiException("doctor not found");
        }

        if (doctor.getIsLeave()){
            throw new ApiException("doctor in leave");
        }

        Scheduler schedule = schedulerRepository.findSchedulerByDoctorId(doctorId);
        if (schedule == null){
            schedule = new Scheduler();
            schedule.setDoctorId(doctorId);
            schedule.setAvailableDays(new ArrayList<>());
        }
        if (schedule.getAvailableDays().contains(dayName.toLowerCase())){
            throw new ApiException("doctor is available in this day");
        }

        schedule.getAvailableDays().add(dayName.toLowerCase());
        schedulerRepository.save(schedule);
    }

    public void resetDoctorsSchedule(Integer adminId) {
        if (adminRepository.findAdminById(adminId) == null){
            throw new ApiException("admin not found");
        }
        List<Scheduler> schedulers = schedulerRepository.findAll();
        for (Scheduler s : schedulers){
            s.setAvailableDays(new ArrayList<>());
            schedulerRepository.save(s);
        }
    }

    public Scheduler getScheduleByDoctorId(Integer doctorId){
        Scheduler scheduler = schedulerRepository.findSchedulerByDoctorId(doctorId);
        if (scheduler == null){
            throw new ApiException("no schedule");
        }
        return scheduler;
    }
}
