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
import com.autohard.api.models.OperatingSystem;
import com.autohard.api.models.session.AuthToken;
import com.autohard.api.models.session.User;
import com.autohard.api.models.session.Role.autoHardPrivilege;

import jakarta.transaction.Transactional;

@Transactional
@RestController
public class OperatingSystemController {

    private final DatabaseService databaseService;

    @Autowired
    public OperatingSystemController(DatabaseService databaseService){
        this.databaseService = databaseService;
    }

    @GetMapping("/os")
    public ResponseEntity<List<OperatingSystem>> getOperatingSystems(@RequestHeader("Authorization") String authHeader){
        User currentUser = AuthToken.isValid(databaseService, authHeader);

        if (currentUser == null || !currentUser.hasPrivilege(autoHardPrivilege.READ_OPERATING_SYSTEM)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(databaseService.getAllOperatingSystems(), HttpStatus.OK);
    }

    @GetMapping("/os/{idOs}")
    public ResponseEntity<OperatingSystem> getOperatingSystem(@RequestHeader("Authorization") String authHeader, @PathVariable Integer idOs){
        User currentUser = AuthToken.isValid(databaseService, authHeader);

        if (currentUser == null || !currentUser.hasPrivilege(autoHardPrivilege.READ_OPERATING_SYSTEM)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        OperatingSystem rescuedOS = databaseService.getOperatingSystemById(idOs);

        if (rescuedOS == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(rescuedOS, HttpStatus.OK);
    }
    
}
