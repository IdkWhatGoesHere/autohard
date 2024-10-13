package com.autohard.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.autohard.api.database.DatabaseService;

import jakarta.transaction.Transactional;

@Transactional
@RestController
public class NodeController {
    
    private final DatabaseService databaseService;

    @Autowired
    public NodeController(DatabaseService databaseService){
        this.databaseService = databaseService;
    }
}
