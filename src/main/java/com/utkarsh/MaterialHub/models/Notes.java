package com.utkarsh.MaterialHub.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "notes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notes {
    private String id;
    private String course;
    private String notesName;
    private String semester;
    private String fileUrl;
    private String author;
    private Date publish;
}
