package com.autohard.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.autohard.api.database.DatabaseService;
import com.autohard.api.models.Node;
import com.autohard.api.models.OperatingSystem;
import com.autohard.api.models.session.AuthToken;
import com.autohard.api.models.session.User;
import com.autohard.api.models.session.Role.autoHardPrivilege;

import jakarta.transaction.Transactional;

@Transactional
@RestController
public class NodeController {
    
    private final DatabaseService databaseService;

    @Autowired
    public NodeController(DatabaseService databaseService){
        this.databaseService = databaseService;
    }

    @GetMapping("/node")
    public ResponseEntity<List<Node>> getNodes(@RequestHeader("Authorization") String authHeader){
        User currentUser = AuthToken.isValid(databaseService, authHeader);

        if (currentUser == null || !currentUser.hasPrivilege(autoHardPrivilege.READ_NODE)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(databaseService.getAllNodes(), HttpStatus.OK);
    }

    @GetMapping("/node/{idNode}")
    public ResponseEntity<Node> getNode(@RequestHeader("Authorization") String authHeader, @PathVariable Integer idNode){
        User currentUser = AuthToken.isValid(databaseService, authHeader);

        if (currentUser == null || !currentUser.hasPrivilege(autoHardPrivilege.READ_NODE)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Node rescuedNode = databaseService.getNodeById(idNode);

        if (rescuedNode == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(rescuedNode, HttpStatus.OK);
    }

    @PostMapping("/node")
    public ResponseEntity<Node> createNode(@RequestHeader("Authorization") String authHeader, @RequestBody Node node){
        User currentUser = AuthToken.isValid(databaseService, authHeader);

        if (currentUser == null || !currentUser.hasPrivilege(autoHardPrivilege. WRITE_NODE)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (node.getUsername() == null || node.getPassword() == null || node.getOperatingSystem() == null 
            || node.getOperatingSystem().getId() == null || node.getHostname() == null || node.getIp() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        OperatingSystem rescuedOs = databaseService.getOperatingSystemById(node.getOperatingSystem().getId());

        if (rescuedOs == null || Node.isIpValid(node.getIp())){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Node createdNode = new Node(node.getHostname(), node.getIp(), node.getUsername(), node.getPassword(), rescuedOs);

        return new ResponseEntity<>(databaseService.saveNode(createdNode), HttpStatus.OK);
    }

    @PutMapping("/node")
    public ResponseEntity<Node> modifyNode(@RequestHeader("Authorization") String authHeader, @RequestBody Node node){  
        User currentUser = AuthToken.isValid(databaseService, authHeader);

        if (currentUser == null || !currentUser.hasPrivilege(autoHardPrivilege. WRITE_NODE)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (node.getId() == null || node.getUsername() == null || node.getPassword() == null || node.getOperatingSystem() == null 
            || node.getOperatingSystem().getId() == null || node.getHostname() == null || node.getIp() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        OperatingSystem rescuedOs = databaseService.getOperatingSystemById(node.getOperatingSystem().getId());

        if (rescuedOs == null || Node.isIpValid(node.getIp())){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(databaseService.saveNode(node), HttpStatus.OK);
    }

    @DeleteMapping("/node/{idNode}")
    public ResponseEntity<Void> deleteNode(@RequestHeader("Authorization") String authHeader, @PathVariable Integer idNode){
        User currentUser = AuthToken.isValid(databaseService, authHeader);

        if (currentUser == null || !currentUser.hasPrivilege(autoHardPrivilege.WRITE_NODE)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Node rescuedNode = databaseService.getNodeById(idNode);

        if (rescuedNode == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        databaseService.deleteNodeById(idNode);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
