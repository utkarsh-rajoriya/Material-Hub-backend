package com.utkarsh.MaterialHub.controllers;

import com.utkarsh.MaterialHub.Services.BaseService;
import com.utkarsh.MaterialHub.Services.SearchQuery;
import com.utkarsh.MaterialHub.models.Notes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api")
public class BaseController {
    private final SearchQuery searchQuery;
    BaseService baseService;

    public BaseController(BaseService baseService, SearchQuery searchQuery) {
        this.baseService = baseService;
        this.searchQuery = searchQuery;
    }

    @GetMapping("test")
    public String test(){
        return "This is testing url";
    }

    @GetMapping("testing")
    public String testing(){
        return "This is testing";
    }

    @GetMapping("testing2")
    public String testing2(){
        return "This is testing 2 functions \n BTW u made it utkrash, You are Gr8🔥";
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

//    @GetMapping("getNotes")
//    public Page<Notes> getNotes(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "5") int size) {
//        return baseService.getNotes(page, size);
//    }

    @GetMapping("/getNoteBySearch")
    public Page<Notes> searchNotes(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String course,
            @RequestParam(required = false) String semester,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {

        List<Notes> notes = searchQuery.searchNotes(query, course, semester, page, size);
        long total = searchQuery.countNotes(query, course, semester);

        return new PageImpl<>(notes, PageRequest.of(page, size), total);
    }

    @GetMapping("myUploads/{name}")
    public Page<Notes> myUploads(@PathVariable("name") String name,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "4") int size){
        return baseService.myUploads(name , page, size);
    }

    @DeleteMapping("deleteNote/{id}")
    public String deleteNote(@PathVariable("id") String noteId){
        return baseService.deleteNote(noteId);
    }
}