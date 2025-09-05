package com.utkarsh.MaterialHub.Services;

import com.utkarsh.MaterialHub.daos.PendingTeacherRepo;
import com.utkarsh.MaterialHub.daos.UserRepo;
import com.utkarsh.MaterialHub.models.PendingTeacher;
import com.utkarsh.MaterialHub.models.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final PendingTeacherRepo pendingTeacherRepo;
    private final UserRepo repo;
    private final AuthenticationManager authManager;
    private final JWTService jwtService;
    private final EmailService emailService;
    private final MakeAbstractRequest makeAbstractRequest;

    public UserService(PendingTeacherRepo pendingTeacherRepo, UserRepo repo, AuthenticationManager authManager, JWTService jwtService, EmailService emailService , MakeAbstractRequest makeAbstractRequest) {
        this.pendingTeacherRepo = pendingTeacherRepo;
        this.repo = repo;
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.makeAbstractRequest = makeAbstractRequest;
    }

    public String register(User user) {
        boolean existEmail = makeAbstractRequest.isValidEmail(user.getEmail());
        if(!existEmail){
            return "Email does not exist";
        }

        Optional<User> usr = repo.findByEmail(user.getEmail());
        Optional<PendingTeacher> pto = pendingTeacherRepo.findByEmail(user.getEmail());

        if (usr.isPresent()) {
            return "User already exists with email";
        } else if (pto.isPresent()) {
            return "Request already Exist with this email";
        }

        PendingTeacher pendingTeacher = new PendingTeacher(user.getName(), user.getEmail(), user.getPassword(), user.getRole(), new Date());
        pendingTeacherRepo.save(pendingTeacher);

        String body = String.format(
                """
                        Dear %s, 👋
                        
                        Thank you for joining Material-Hub! 🚀  
                        Your request has been received and is currently under review. ✅  
                        
                        📚 Once approved, you’ll be able to upload and explore amazing study materials.  
                        💡 Stay tuned — we’ll notify you as soon as your account is activated.  
                        
                        Best regards,  
                        Utkarsh Rajoriya 🌟
                        """,
                user.getName()
        );
        emailService.sendSimpleEmail(user.getEmail(), "🎉 Welcome to Material Hub!", body);
        return "success";
    }

    public Map<String, String> login(String email, String password) {
        Map<String, String> data = new HashMap<>();
        Optional<User> usr = repo.findByEmail(email);


        if (usr.isEmpty()) {
            data.put("message", "User not exist");
            return data;
        }

        User user = usr.get();
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            if (authentication.isAuthenticated()) {
                data.put("token", jwtService.generateToken(email));
                data.put("name", user.getName());
                return data;
            }
        } catch (Exception ex) {
            data.put("message", "Invalid credentials");
            return data;
        }

        data.put("message", "Invalid credentials");
        return data;
    }

}
