package com.utkarsh.MaterialHub.daos;

import com.utkarsh.MaterialHub.models.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AdminRepo extends MongoRepository<Admin , String> {
    Optional<Admin> findByEmail(String email);
}
