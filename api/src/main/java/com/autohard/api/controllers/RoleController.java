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
import com.autohard.api.models.session.AuthToken;
import com.autohard.api.models.session.Role;
import com.autohard.api.models.session.Role.autoHardPrivilege;
import com.autohard.api.models.session.User;

import jakarta.transaction.Transactional;

@Transactional
@RestController
public class RoleController {
    
    private final DatabaseService databaseService;

    @Autowired
    public RoleController(DatabaseService databaseService){
        this.databaseService = databaseService;
    }

    @GetMapping("/role")
    public ResponseEntity<List<Role>> getRoles(@RequestHeader("Authorization") String authHeader){
        User currentUser = AuthToken.isValid(databaseService, authHeader);

        if (currentUser == null || !currentUser.hasPrivilege(autoHardPrivilege.READ_ROLE)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(databaseService.getRoles(), HttpStatus.OK);
    }

    @GetMapping("/role/{idRole}")
    public ResponseEntity<Role> getRole(@RequestHeader("Authorization") String authHeader, @PathVariable Integer idRole){
        User currentUser = AuthToken.isValid(databaseService, authHeader);

        if (currentUser == null || !currentUser.hasPrivilege(autoHardPrivilege.READ_ROLE)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Role rescuedRole = databaseService.getRoleById(idRole);

        if (rescuedRole == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(rescuedRole, HttpStatus.OK);
    }

    @PostMapping("/role")
    public ResponseEntity<Role> createRole(@RequestHeader("Authorization") String authHeader, @RequestBody Role role){
        User currentUser = AuthToken.isValid(databaseService, authHeader);

        if (currentUser == null || !currentUser.hasPrivilege(autoHardPrivilege.WRITE_ROLE)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (role.getName() == null || role.getPrivileges() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (databaseService.getRoleByName(role.getName()) != null){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(databaseService.saveRole(new Role(role.getName(), role.getPrivileges())), HttpStatus.OK);
    }

    @DeleteMapping("/role/{idRole}")
    public ResponseEntity<Void> deleteRole(@RequestHeader("Authorization") String authHeader, @PathVariable Integer idRole){
        User currentUser = AuthToken.isValid(databaseService, authHeader);

        if (currentUser == null || !currentUser.hasPrivilege(autoHardPrivilege.WRITE_ROLE)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (databaseService.getRoleById(idRole) == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        databaseService.deleteRoleById(idRole);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/role")
    public ResponseEntity<Role> updateRole(@RequestHeader("Authorization") String authHeader, @RequestBody Role role){
        User currentUser = AuthToken.isValid(databaseService, authHeader);

        if (currentUser == null || !currentUser.hasPrivilege(autoHardPrivilege.WRITE_ROLE)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (role.getId() == null || role.getName() == null || role.getPrivileges() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (databaseService.getRoleById(role.getId()) == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(databaseService.saveRole(role), HttpStatus.OK);
    }
}
