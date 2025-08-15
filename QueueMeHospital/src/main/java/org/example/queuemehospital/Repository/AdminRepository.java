package org.example.queuemehospital.Repository;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.example.queuemehospital.Model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin findAdminById(Integer adminId);
    Admin findAdminByUsername(@NotEmpty(message = "username is required") @Size(max = 20, message = "username max length 20") String username);
}
