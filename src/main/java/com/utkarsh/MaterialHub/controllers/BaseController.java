package com.utkarsh.MaterialHub.controllers;

import com.utkarsh.MaterialHub.Services.BaseService;
import com.utkarsh.MaterialHub.models.Notes;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@RestController
@RequestMapping("api")
public class BaseController {

    BaseService baseService;

    public BaseController(BaseService baseService) {
        this.baseService = baseService;
    }

    @PostMapping("uploadNotes")
    public ResponseEntity<Notes> uploadNotes(
            @RequestPart("note") Notes note,
            @RequestPart("file") MultipartFile file) {

        try {
            Notes savedNote = baseService.saveNotes(note, file);
            return ResponseEntity.ok(savedNote);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("getNotes")
    public Page<Notes> getNotes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        System.out.println(page + size);
        return baseService.getNotes(page, size);
    }


}