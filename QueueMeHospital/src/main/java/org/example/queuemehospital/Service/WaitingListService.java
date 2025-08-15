package org.example.queuemehospital.Service;

import lombok.RequiredArgsConstructor;
import org.example.queuemehospital.Api.ApiException;
import org.example.queuemehospital.Model.Appointment;
import org.example.queuemehospital.Model.WaitingList;
import org.example.queuemehospital.Repository.AppointmentRepository;
import org.example.queuemehospital.Repository.DoctorRepository;
import org.example.queuemehospital.Repository.UserRepository;
import org.example.queuemehospital.Repository.WaitingListRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WaitingListService {

    private final WaitingListRepository waitingListRepository;
    private final AppointmentRepository appointmentRepository;

    public List<WaitingList> getAllWaitingLists(){
        return waitingListRepository.findAll();
    }


    //endpoints
    public void swapAppointment(Integer userId, Integer doctorId,Integer appointmentId ,Integer appointmentSwappingNumber){
        Appointment appointment = appointmentRepository.findAppointmentById(appointmentId);
        if (appointment == null){
            throw new ApiException("appointment not found");
        }
        if (!appointment.getUserId().equals(userId) || !appointment.getDoctorId().equals(doctorId)){
            throw new ApiException("you are not allowed to change this appointment data");
        }

        WaitingList waitingList = waitingListRepository.findWaitingListByAppointmentId(appointmentId);
        //swap numbers of queue by appointmentSwappingNumber

    }


    public int getPatientPosition(Integer appointmentId) {
        WaitingList target = waitingListRepository.findAll()
                .stream()
                .filter(w -> w.getAppointmentId().equals(appointmentId))
                .findFirst()
                .orElseThrow(() -> new ApiException("Patient not found in waiting list"));

        List<WaitingList> queue = waitingListRepository.findByDoctorIdOrderByNumberInQueueAsc(target.getDoctorId());

        for (int i = 0; i < queue.size(); i++) {
            if (queue.get(i).getAppointmentId().equals(appointmentId)) {
                return i + 1; // Position (1-based index)
            }
        }
        throw new ApiException("Patient not found in waiting list");
    }

}
