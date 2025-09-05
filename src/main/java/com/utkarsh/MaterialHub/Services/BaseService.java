package com.utkarsh.MaterialHub.Services;

import com.utkarsh.MaterialHub.daos.NoteRepo;
import com.utkarsh.MaterialHub.models.Notes;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


@Service
public class BaseService {
    private final NoteRepo noteRepo;
    private final CloudinaryService cloudinaryService;


    public BaseService(NoteRepo noteRepo, CloudinaryService cloudinaryService) {
        this.noteRepo = noteRepo;
        this.cloudinaryService = cloudinaryService;
    }

    public Notes saveNotes(Notes note, MultipartFile file) throws IOException {
        String fileUrl = cloudinaryService.uploadNotes(file);
        note.setFileUrl(fileUrl);
        note.setPublish(new Date());
        return noteRepo.save(note);
    }

    public Page<Notes> getNotes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return noteRepo.findAll(pageable);
    }

    public Page<Notes> myUploads(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return noteRepo.myUpload(name, pageable);
    }

    public String deleteNote(String id) {
        Notes note = noteRepo.findById(id).orElse(null);
        if (note == null) {
            return "Not not present";
        }

        cloudinaryService.dltNote(note.getFileUrl());
        noteRepo.deleteById(id);
        return "Note deleted successfully";
    }

}
