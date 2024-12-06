package com.autohard.api.database.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.autohard.api.models.Execution;
import com.autohard.api.models.Job;

@Repository
public interface ExecutionRepository extends JpaRepository<Execution, Integer>{
    public List<Execution> findByJob(Job job);
    
}
