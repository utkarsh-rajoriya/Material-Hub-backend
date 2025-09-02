package com.utkarsh.MaterialHub.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "pending_teachers")
public class PendingTeacher {
    private String id;
    private String name;
    private String email;
    private String password;

    public PendingTeacher(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}