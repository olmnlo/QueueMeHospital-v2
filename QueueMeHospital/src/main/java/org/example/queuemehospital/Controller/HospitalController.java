package org.example.queuemehospital.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.queuemehospital.Api.ApiResponse;
import org.example.queuemehospital.Model.Hospital;
import org.example.queuemehospital.Service.HospitalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hospital")
public class HospitalController {

    private final HospitalService hospitalService;


    @GetMapping
    public ResponseEntity<?> getHospitals(){
        List<Hospital> hospitals = hospitalService.getAllHospitals();
        return ResponseEntity.status(HttpStatus.OK).body(hospitals);
    }

    @PostMapping("/{adminId}")
    public ResponseEntity<?> addHospital(@PathVariable Integer adminId, @Valid @RequestBody Hospital hospital){
        hospitalService.addHospital(adminId,hospital);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("hospital added successfully"));
    }

    @PutMapping("/{adminId}/update/{hospitalId}")
    public ResponseEntity<?> updateHospital(@PathVariable Integer adminId,@PathVariable Integer hospitalId , @Valid@RequestBody Hospital hospital){
        hospitalService.updateHospital(adminId,hospitalId,hospital);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("hospital updated successfully"));
    }

    @DeleteMapping("/{adminId}/delete/{hospitalId}")
    public ResponseEntity<?> deleteHospital(@PathVariable Integer adminId,@PathVariable Integer hospitalId){
        hospitalService.deleteHospital(adminId,hospitalId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("hospital deleted successfully"));
    }



    @GetMapping("/fetch-osm")
    public ResponseEntity<?> fetchHospitalsFromOSM(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(defaultValue = "10") double radius) {

        hospitalService.fetchAndSaveHospitalsFromOSM(lat, lon, radius);
        return ResponseEntity.ok(new ApiResponse("fetched successfully"));
    }
}
