package com.utkarsh.MaterialHub.daos;

import com.utkarsh.MaterialHub.models.Notes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepo extends MongoRepository<Notes , String> {
    Page<Notes> findAll(Pageable pageable);

    @Query("{ 'author': ?0 }")
    Page<Notes> myUpload(String author, Pageable pageable);
}
