package org.example.queuemehospital.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.queuemehospital.Api.ApiException;
import org.example.queuemehospital.Model.*;
import org.example.queuemehospital.Repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    private final UserRepository userRepository;
    private final WaitingListRepository waitingListRepository;
    private final DoctorRepository doctorRepository;
    private final SchedulerRepository schedulerRepository;

    public List<Appointment> getAllAppointment() {
        return appointmentRepository.findAll();
    }

    public List<Appointment> getAppointmentByUserId(Integer userId) {
        User user = userRepository.findUserById(userId);
        if (user == null){
            throw new ApiException("user not found");
        }
        return appointmentRepository.findAppointmentsByUserId(userId);
    }

    public List<Appointment> getAppointmentByDoctorId(Integer doctorId) {
        Doctor doctor = doctorRepository.findDoctorById(doctorId);
        if (doctor == null){
            throw new ApiException("doctor not found");
        }
        return appointmentRepository.findAppointmentsByDoctorId(doctorId);
    }

    public void deleteAppointment(Integer userId, Integer appointmentId) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new ApiException("user not found");
        }

        Appointment appointment = appointmentRepository.findAppointmentById(appointmentId);
        if (appointment == null) {
            throw new ApiException("appointment not found");
        }

        if (!appointment.getUserId().equals(userId)) {
            throw new ApiException("you are not allowed to delete appointment");
        }

        if (appointment.getStatus().equals("done")) {
            throw new ApiException("this appointment is done you cannot delete it");
        }


        appointmentRepository.delete(appointment);


        WaitingList waitingListEntry = waitingListRepository.findWaitingListByAppointmentId(appointmentId);
        if (waitingListEntry != null) {
            waitingListRepository.delete(waitingListEntry);
        }


        List<WaitingList> waitingLists = waitingListRepository.findWaitingListByDoctorId(appointment.getDoctorId());
        waitingLists.sort(Comparator.comparingInt(WaitingList::getNumberInQueue));

        for (int i = 0; i < waitingLists.size(); i++) {
            waitingLists.get(i).setNumberInQueue(i + 1);
            waitingListRepository.save(waitingLists.get(i));
        }
    }

    @Transactional
    public void processNextWaitingAppointment(Integer doctorId, String description) {
        List<Appointment> waitingAppointments = appointmentRepository.findWaitingAppointmentsByDoctorId(doctorId);

        if (waitingAppointments.isEmpty()) {
            throw new ApiException("No waiting appointment found");
        }

        Appointment appointment = waitingAppointments.get(0);
        if (appointment.getAppointmentDay().isAfter(LocalDateTime.now())){
            throw new ApiException("you cannot assign done for this appointment");
        }
        appointment.setDescription(description);


        int updatedCount = appointmentRepository.updateAppointmentStatusToDone(appointment.getId());
        if (updatedCount == 0) {
            throw new ApiException("Failed to update appointment status");
        }


        Integer numberInQueue = waitingListRepository.findNumberInQueueByAppointmentId(appointment.getId());
        if (numberInQueue == null) {
            throw new ApiException("Waiting list entry not found for appointment");
        }


        int updatedWaitingListCount = waitingListRepository.decrementNumberInQueueAfter(doctorId, numberInQueue);
        if (updatedWaitingListCount == 0) {

        }
    }

    //endpointLogic
    public void bookAppointment(Integer userId, Integer doctorId, LocalDateTime day) {
        Doctor doctor = doctorRepository.findDoctorById(doctorId);
        if (doctor == null) {
            throw new ApiException("doctor not found");
        }

        String dayName = day.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH).toLowerCase();

        Scheduler scheduler = schedulerRepository.findSchedulerByDoctorId(doctorId);
        if (scheduler == null){
            throw new ApiException("no schedules");
        }
        if (!scheduler.getAvailableDays().contains(dayName.toLowerCase())) {
            throw new ApiException("doctor not available in this day");
        }
        if (doctor.getIsLeave()) {
            throw new ApiException("doctor in leave");
        }

        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new ApiException("user not found");
        }

        LocalDate date = day.toLocalDate();
        LocalDateTime startOfDay = date.atTime(9, 0);
        if (startOfDay.isBefore(LocalDateTime.now())){
            startOfDay = LocalDateTime.now();
        }
        LocalDateTime endDay = date.plusDays(1).atStartOfDay();

        boolean exists = appointmentRepository.existsByUserIdAndDoctorIdAndAppointmentDayBetween(userId, doctorId, startOfDay, endDay);

        if (exists) {
            throw new ApiException("User already has an appointment with this doctor on the same day");
        }
        LocalDateTime lastAppointmentTime = appointmentRepository.findMaxAppointmentDayByDoctorIdAndDate(doctorId, date.atStartOfDay(), date.plusDays(1).atStartOfDay()).orElse(startOfDay.minusMinutes(30));

        LocalDateTime endTime = date.atTime(18, 0, 0);
        LocalDateTime newAppointmentTime = lastAppointmentTime.plusMinutes(30);
        if (newAppointmentTime.isAfter(endTime)) {
            throw new ApiException("Queue is full for this day");
        }


        Appointment appointment = new Appointment();
        appointment.setDoctorId(doctorId);
        appointment.setUserId(userId);
        appointment.setDescription("");
        appointment.setAppointmentDate(LocalDateTime.now());
        appointment.setStatus("waiting");
        appointment.setAppointmentDay(newAppointmentTime);

        appointmentRepository.save(appointment);

        WaitingList waitingList = new WaitingList();
        waitingList.setNumberInQueue(appointmentRepository.findAppointmentByDoctorId(doctorId).size());
        waitingList.setDoctorId(doctorId);
        waitingList.setTimeEntered(LocalDateTime.now());
        waitingList.setAppointmentId(appointment.getId());
        waitingList.setUserId(userId);

        waitingListRepository.save(waitingList);

    }
}
