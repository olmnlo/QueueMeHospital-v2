package org.example.queuemehospital.Controller;

import lombok.RequiredArgsConstructor;
import org.example.queuemehospital.Api.ApiResponse;
import org.example.queuemehospital.Model.WaitingList;
import org.example.queuemehospital.Service.WaitingListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/waiting-list")
public class WaitingListController{

    private final WaitingListService waitingListService;


    @GetMapping
    public ResponseEntity<?> getAllWaitingList(){
        List<WaitingList> waitingLists = waitingListService.getAllWaitingLists();
        return ResponseEntity.status(HttpStatus.OK).body(waitingLists);
    }

    @GetMapping("/position/{appointmentId}")
    public ResponseEntity<?> getPatientPosition(@PathVariable Integer appointmentId) {
        int position = waitingListService.getPatientPosition(appointmentId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("{\"position\": " + position + "}"));
    }
}