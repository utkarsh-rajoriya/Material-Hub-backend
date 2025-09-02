package com.utkarsh.MaterialHub.Services;

import com.utkarsh.MaterialHub.daos.PendingTeacherRepo;
import com.utkarsh.MaterialHub.daos.UserRepo;
import com.utkarsh.MaterialHub.models.PendingTeacher;
import com.utkarsh.MaterialHub.models.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final PendingTeacherRepo pendingTeacherRepo;
    private final UserRepo repo;
    private final AuthenticationManager authManager;
    private final JWTService jwtService;
    private final BCryptPasswordEncoder encoder;

    public UserService(PendingTeacherRepo pendingTeacherRepo , UserRepo repo, AuthenticationManager authManager, JWTService jwtService) {
        this.pendingTeacherRepo = pendingTeacherRepo;
        this.repo = repo;
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.encoder = new BCryptPasswordEncoder();
    }

    public List<User> getUsers() {
        return repo.findAll();
    }

    public String register(User user) {
        Optional<User> usr = repo.findByEmail(user.getEmail());
        if (usr.isPresent()) {
            return "User already exists with email";
        }
        PendingTeacher pendingTeacher = new PendingTeacher(
                user.getName(),
                user.getEmail(),
                encoder.encode(user.getPassword())
        );

        pendingTeacherRepo.save(pendingTeacher);

        return "success";
    }

    public Map<String , String> login(String email, String password) {
        Map<String , String> data = new HashMap<>();
        Optional<User> usr = repo.findByEmail(email);


        if (usr.isEmpty()) {
            data.put("message" , "User not exist");
            return data ;
        }

        User user = usr.get();
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            if (authentication.isAuthenticated()) {
                data.put("token" , jwtService.generateToken(email));
                data.put("name" , user.getName());
                return data;
            }
        } catch (Exception ex) {
            data.put("message" , "Invalid credentials");
            return data ;
        }

        data.put("message" , "Invalid credentials");
        return data ;
    }

}
