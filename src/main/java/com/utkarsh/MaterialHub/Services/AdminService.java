package com.utkarsh.MaterialHub.Services;

import com.utkarsh.MaterialHub.daos.AdminRepo;
import com.utkarsh.MaterialHub.daos.PendingTeacherRepo;
import com.utkarsh.MaterialHub.daos.UserRepo;
import com.utkarsh.MaterialHub.models.Admin;
import com.utkarsh.MaterialHub.models.PendingTeacher;
import com.utkarsh.MaterialHub.models.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AdminService {

    private final PendingTeacherRepo pendingTeacherRepo;
    private final UserRepo userRepo;
    private final AdminRepo adminRepo;
    private final JWTService jwtService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AdminService(PendingTeacherRepo pendingTeacherRepo, UserRepo userRepo, AdminRepo adminRepo, JWTService jwtService) {
        this.pendingTeacherRepo = pendingTeacherRepo;
        this.userRepo = userRepo;
        this.adminRepo = adminRepo;
        this.jwtService = jwtService;
    }

    // Get all requests
    public List<PendingTeacher> getReqs() {
        return pendingTeacherRepo.findAll();
    }

    // Approve teacher request
    public Map<String, String> approveTeacher(String id) {
        Map<String, String> res = new HashMap<>();
        Optional<PendingTeacher> pendingOpt = pendingTeacherRepo.findById(id);

        if (pendingOpt.isEmpty()) {
            res.put("message", "No pending request found for this id");
            return res;
        }

        PendingTeacher pendingTeacher = pendingOpt.get();

        User newUser = new User();
        newUser.setName(pendingTeacher.getName());
        newUser.setEmail(pendingTeacher.getEmail());
        newUser.setPassword(pendingTeacher.getPassword()); // already encoded

        userRepo.save(newUser);
        pendingTeacherRepo.delete(pendingTeacher);

        res.put("message", "Teacher approved successfully");
        return res;
    }

    // Reject teacher request
    public Map<String, String> rejectTeacher(String id) {
        Map<String, String> res = new HashMap<>();
        Optional<PendingTeacher> pendingOpt = pendingTeacherRepo.findById(id);

        if (pendingOpt.isEmpty()) {
            res.put("message", "No pending request found for this id");
            return res;
        }

        pendingTeacherRepo.delete(pendingOpt.get());
        res.put("message", "Teacher request rejected");
        return res;
    }

    // Create a new Admin
    public Map<String, String> createAdmin(Admin admin) {
        Map<String, String> res = new HashMap<>();
        Optional<Admin> adm = adminRepo.findByEmail(admin.getEmail());

        if (adm.isPresent()) {
            res.put("message", "Admin already exists");
            return res;
        }

        admin.setPassword(encoder.encode(admin.getPassword()));
        adminRepo.save(admin);

        res.put("message", "Admin created successfully");
        return res;
    }

    // Admin login
    public Map<String, String> login(String email, String password) {
        Map<String, String> adminInfo = new HashMap<>();
        Optional<Admin> adm = adminRepo.findByEmail(email);

        if (adm.isEmpty()) {
            adminInfo.put("message", "Admin not exist");
            return adminInfo;
        }

        Admin existingAdmin = adm.get();

        if (encoder.matches(password, existingAdmin.getPassword())) {
            adminInfo.put("adminToken", jwtService.generateToken(email));
            adminInfo.put("adminName", existingAdmin.getName());
        } else {
            adminInfo.put("message", "Invalid credentials");
        }

        return adminInfo;
    }
}
