package com.utkarsh.MaterialHub.controllers;

import com.utkarsh.MaterialHub.Services.AdminService;
import com.utkarsh.MaterialHub.models.Admin;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/admin")
public class AdminController {
    AdminService adminService;
    AdminController(AdminService adminService){
        this.adminService = adminService;
    }

    @PostMapping("create")
    public ResponseEntity<?> createAdmin(@RequestBody Admin admin){
        return new ResponseEntity<>(adminService.createAdmin(admin) , HttpStatus.OK);
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody Map<String , String> credentials){
        String email = credentials.get("email");
        String password = credentials.get("password");
        return new ResponseEntity<>(adminService.login(email , password) , HttpStatus.OK);
    }

    @GetMapping("getReqs")
    public ResponseEntity<?> getReqs(){
        System.out.println("get Req called");
        return new ResponseEntity<>(adminService.getReqs() , HttpStatus.OK);
    }

    @PostMapping("approve/{id}")
    public ResponseEntity<?> approve(@PathVariable("id") String email){
        System.out.println("id to approve : "+email);
        return new ResponseEntity<>(adminService.approveTeacher(email) , HttpStatus.OK);
    }

    @PostMapping("reject/{id}")
    public ResponseEntity<?> reject(@PathVariable("id") String id){
        System.out.println("id for reject : "+id);
        return new ResponseEntity<>(adminService.rejectTeacher(id) , HttpStatus.OK);
    }
}
