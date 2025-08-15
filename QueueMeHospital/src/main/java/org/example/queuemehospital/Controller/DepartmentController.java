package org.example.queuemehospital.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.queuemehospital.Api.ApiResponse;
import org.example.queuemehospital.Model.Department;
import org.example.queuemehospital.Model.Doctor;
import org.example.queuemehospital.Service.DepartmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/department")
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<?> getAllDepartments(){
        List<Department> departments = departmentService.getAllDepartments();
        return ResponseEntity.status(HttpStatus.OK).body(departments);
    }

    @PostMapping("/{adminId}")
    public ResponseEntity<?> addDepartment(@PathVariable Integer adminId, @Valid @RequestBody Department department){
        departmentService.addDepartment(adminId,department);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("department added successfully"));
    }

    @PutMapping("/{adminId}/update/{departmentId}")
    public ResponseEntity<?> updateDepartment(@PathVariable Integer adminId, @PathVariable Integer departmentId,@Valid@RequestBody Department department){
        departmentService.updateDepartment(adminId,departmentId,department);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("department updated successfully"));
    }

    @DeleteMapping("/{adminId}/delete/{departmentId}")
    public ResponseEntity<?> deleteDepartment(@PathVariable Integer adminId, @PathVariable Integer departmentId){
        departmentService.deleteDepartment(adminId,departmentId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("department deleted successfully"));
    }

    @GetMapping("/{departmentId}/doctors")
    public ResponseEntity<?> getDoctorByDepartmentId(@PathVariable Integer departmentId){
        List<Doctor> doctors = departmentService.getDoctorsByDepartmentId(departmentId);
        return ResponseEntity.status(HttpStatus.OK).body(doctors);
    }

}
