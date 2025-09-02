package com.utkarsh.MaterialHub.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "admins")
public class Admin {
    private String id;
    private String name;
    private String email;
    private String password;
}
