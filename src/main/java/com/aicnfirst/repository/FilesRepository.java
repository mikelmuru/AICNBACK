package com.aicnfirst.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aicnfirst.model.FilePersist;

public interface FilesRepository extends JpaRepository<FilePersist, Integer>{
    
    public Optional<FilePersist> findByImageName(String imageName);

    public List<FilePersist> findByResult(String result);
}
