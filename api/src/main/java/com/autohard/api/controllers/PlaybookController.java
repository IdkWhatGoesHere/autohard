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
import com.autohard.api.models.Playbook;
import com.autohard.api.models.session.AuthToken;
import com.autohard.api.models.session.Role.autoHardPrivilege;
import com.autohard.api.models.session.User;

import jakarta.transaction.Transactional;

@Transactional
@RestController
public class PlaybookController {

    private final DatabaseService databaseService;

    @Autowired
    public PlaybookController(DatabaseService databaseService){
        this.databaseService = databaseService;
    }

    @GetMapping("/playbook")
    public ResponseEntity<List<Playbook>> getPlaybooks(@RequestHeader("Authorization") String authHeader){
        User currentUser = AuthToken.isValid(databaseService, authHeader);

        if (currentUser == null || !currentUser.hasPrivilege(autoHardPrivilege.READ_PLAYBOOK)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(databaseService.getAllPlaybooks(), HttpStatus.OK);
    }

    @GetMapping("/playbook/{idPlaybook}")
    public ResponseEntity<Playbook> getPlaybook(@RequestHeader("Authorization") String authHeader, @PathVariable Integer idPlaybook){
        User currentUser = AuthToken.isValid(databaseService, authHeader);

        if (currentUser == null || !currentUser.hasPrivilege(autoHardPrivilege.READ_PLAYBOOK)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Playbook rescuedPb = databaseService.getPlaybookById(idPlaybook);

        if (rescuedPb == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(rescuedPb, HttpStatus.OK);
    }
    
}
