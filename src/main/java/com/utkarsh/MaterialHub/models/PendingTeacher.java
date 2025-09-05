package com.utkarsh.MaterialHub.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "pending_teachers")
public class PendingTeacher {
    private String id;
    private String name;
    private String email;
    private String password;
    private String role;
    private Date date;

    public PendingTeacher(String name, String email, String password , String role , Date date) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.date = date;
    }
}