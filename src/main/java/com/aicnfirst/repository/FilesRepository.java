package com.aicnfirst.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aicnfirst.model.FilePersist;

public interface FilesRepository extends JpaRepository<FilePersist, Integer>{
    
}
