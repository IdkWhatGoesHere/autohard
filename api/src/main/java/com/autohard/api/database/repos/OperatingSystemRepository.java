package com.autohard.api.database.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.autohard.api.models.OperatingSystem;

@Repository
public interface OperatingSystemRepository extends JpaRepository<OperatingSystem, Integer>{
    
}
