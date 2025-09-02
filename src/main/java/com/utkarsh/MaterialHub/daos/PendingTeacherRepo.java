package com.utkarsh.MaterialHub.daos;

import com.utkarsh.MaterialHub.models.PendingTeacher;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PendingTeacherRepo extends MongoRepository<PendingTeacher, String> {
    boolean existsByEmail(String email);
    Optional<PendingTeacher> findByEmail(String email);
}
