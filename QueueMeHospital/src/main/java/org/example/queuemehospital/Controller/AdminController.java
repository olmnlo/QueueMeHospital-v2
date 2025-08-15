package org.example.queuemehospital.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.queuemehospital.Api.ApiResponse;
import org.example.queuemehospital.Model.Admin;
import org.example.queuemehospital.Service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminService adminService;


    @GetMapping("/{adminId}")
    public ResponseEntity<?> getAllAdmins(@PathVariable Integer adminId){
        return ResponseEntity.status(HttpStatus.OK).body(adminService.findAllAdmins(adminId));
    }

    @PutMapping("/{adminId}/update/{updateId}")
    public ResponseEntity<?> updateAdmin(@PathVariable Integer adminId,@PathVariable Integer updateId, @Valid @RequestBody Admin admin){
        adminService.updateAdmin(adminId,updateId,admin);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("admin updated successfully"));
    }

    @PostMapping("/{adminId}")
    public ResponseEntity<?> add(@PathVariable Integer adminId, @Valid @RequestBody Admin admin){
        adminService.addAdmin(adminId, admin);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("admin added successfully"));
    }

}
