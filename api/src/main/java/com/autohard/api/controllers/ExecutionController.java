package com.autohard.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.autohard.api.database.DatabaseService;
import com.autohard.api.models.Execution;
import com.autohard.api.models.Job;
import com.autohard.api.models.session.AuthToken;
import com.autohard.api.models.session.Role.autoHardPrivilege;
import com.autohard.api.models.session.User;

import jakarta.transaction.Transactional;

@Transactional
@RestController
public class ExecutionController {

    private final DatabaseService databaseService;

    @Autowired
    public ExecutionController(DatabaseService databaseService){
        this.databaseService = databaseService;
    }

    @GetMapping("/job/{idJob}/exec/{idExec}")
    public ResponseEntity<Execution> getExecution(@RequestHeader("Authorization") String authHeader, @PathVariable Integer idJob, @PathVariable Integer idExec){
        User currentUser = AuthToken.isValid(databaseService, authHeader);

        if (currentUser == null || !currentUser.hasPrivilege(autoHardPrivilege.READ_EXECUTION)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Job rescuedJob = databaseService.getJobById(idJob);
        Execution rescuedExec = databaseService.getExecutionById(idExec);

        if (rescuedExec == null || rescuedJob == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(rescuedExec, HttpStatus.OK);
    }

    @GetMapping("/job/{idJob}/exec")
    public ResponseEntity<List<Execution>> getExecutions(@RequestHeader("Authorization") String authHeader, @PathVariable Integer idJob){
        User currentUser = AuthToken.isValid(databaseService, authHeader);

        if (currentUser == null || !currentUser.hasPrivilege(autoHardPrivilege.READ_EXECUTION)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Job rescuedJob = databaseService.getJobById(idJob);

        if (rescuedJob == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(databaseService.getAllExecutionsByJob(rescuedJob), HttpStatus.OK);
    }
    
}
