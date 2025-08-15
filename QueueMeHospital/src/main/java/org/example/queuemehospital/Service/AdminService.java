package org.example.queuemehospital.Service;

import lombok.RequiredArgsConstructor;
import org.example.queuemehospital.Api.ApiException;
import org.example.queuemehospital.Model.Admin;
import org.example.queuemehospital.Repository.AdminRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;

    public List<Admin> findAllAdmins(Integer adminId){
        if (adminRepository.findAdminById(adminId) == null){
            throw new ApiException("admin not found");
        }
        return adminRepository.findAll();
    }

    public void updateAdmin(Integer adminId, Integer updateId ,Admin admin){
        Admin isAdmin = adminRepository.findAdminById(adminId);
        if (isAdmin == null){
            throw new ApiException("admin not found");
        }
    }

    public void addAdmin(Integer adminId, Admin admin){
        Admin isAdmin = adminRepository.findAdminById(adminId);
        if (isAdmin == null){
            throw new ApiException("admin not found");
        }

        Admin isDuplicateAdmin = adminRepository.findAdminByUsername(admin.getUsername());
        if (isDuplicateAdmin != null){
            throw new ApiException("this username is duplicated");
        }
        adminRepository.save(admin);
    }

}
