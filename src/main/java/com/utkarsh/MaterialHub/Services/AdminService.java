package com.utkarsh.MaterialHub.Services;

import com.utkarsh.MaterialHub.daos.PendingTeacherRepo;
import com.utkarsh.MaterialHub.daos.UserRepo;
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
    private final JWTService jwtService;
    private final EmailService emailService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AdminService(PendingTeacherRepo pendingTeacherRepo, UserRepo userRepo, JWTService jwtService , EmailService emailService) {
        this.pendingTeacherRepo = pendingTeacherRepo;
        this.userRepo = userRepo;
        this.jwtService = jwtService;
        this.emailService = emailService;
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

        PendingTeacher pendingTeacher_Admin = pendingOpt.get();

        User newUser = new User();
        newUser.setName(pendingTeacher_Admin.getName());
        newUser.setEmail(pendingTeacher_Admin.getEmail());
        newUser.setRole(pendingTeacher_Admin.getRole());
        newUser.setPassword(encoder.encode(pendingTeacher_Admin.getPassword()));
        userRepo.save(newUser);

        String subject = String.format(
                "🎉 Congratulations! You are now approved as %s ✅",
                pendingTeacher_Admin.getRole()
        );

        String body = String.format(
                """
                Congratulations, Dear %s!  
        
                We are excited to inform you that your request for the role of **%s** has been approved ✅.  
        
                🚀 You now have access to upload and explore study materials on **Material-Hub**.  
                📚 Start contributing and help learners around the world!  
        
                Wishing you great success ahead,  
                Utkarsh Rajoriya 🌟
                """,
                pendingTeacher_Admin.getName(),
                pendingTeacher_Admin.getRole()
        );
        emailService.sendSimpleEmail(pendingTeacher_Admin.getEmail(), subject , body);
        pendingTeacherRepo.delete(pendingTeacher_Admin);

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
    public Map<String, String> createAdmin(User admin) {
        Map<String, String> res = new HashMap<>();
        Optional<User> adm = userRepo.findByEmail(admin.getEmail());

        if (adm.isPresent()) {
            res.put("message", "Admin already exists");
            return res;
        }

        admin.setPassword(encoder.encode(admin.getPassword()));
        admin.setRole("Admin");
        userRepo.save(admin);

        res.put("message", "Admin created successfully");
        return res;
    }

    // Admin login
    public Map<String, String> login(String email, String password) {
        Map<String, String> adminInfo = new HashMap<>();
        Optional<User> adm = userRepo.findByEmail(email);

        if (adm.isEmpty()) {
            adminInfo.put("message", "Admin not exist");
            return adminInfo;
        }

        User existingAdmin = adm.get();
        if(!existingAdmin.getRole().equals("Admin")){
            adminInfo.put("message", "You are not Admin!");
            return adminInfo;
        }

        if (encoder.matches(password, existingAdmin.getPassword())) {
            adminInfo.put("adminToken", jwtService.generateToken(email));
            adminInfo.put("adminName", existingAdmin.getName());
        } else {
            adminInfo.put("message", "Invalid credentials");
        }

        return adminInfo;
    }
}
