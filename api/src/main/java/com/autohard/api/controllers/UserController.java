package com.autohard.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.autohard.api.models.session.User;
import com.autohard.api.models.session.Role.autoHardPrivilege;
import com.autohard.api.security.LoginRequest;

import jakarta.transaction.Transactional;
import net.bytebuddy.utility.RandomString;

@Transactional
@RestController
public class UserController {

    private final DatabaseService databaseService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(DatabaseService databaseService, PasswordEncoder passwordEncoder){
        this.databaseService = databaseService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/user/login")
    public ResponseEntity<AuthToken> login(@RequestBody LoginRequest request){

        if (request.getPassword() == null || request.getUsername() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        User rescuedUser = databaseService.getUserByName(request.getUsername());

        if (rescuedUser == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (!passwordEncoder.matches(request.getPassword(), rescuedUser.getPassword())){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(databaseService.saveAuthToken(new AuthToken(RandomString.make(64), rescuedUser)), HttpStatus.OK);
    }

    @GetMapping("/user/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader){
        User currentUser = AuthToken.isValid(databaseService, authHeader);

        if (currentUser == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        databaseService.deleteAuthTokenByValue(authHeader.substring(7));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<User>> getAllUsers(@RequestHeader("Authorization") String authHeader){
        User currentUser = AuthToken.isValid(databaseService, authHeader);

        if (currentUser == null || !currentUser.hasPrivilege(autoHardPrivilege.READ_USER)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
        return new ResponseEntity<>(databaseService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/user/{idUser}")
    public ResponseEntity<User> getUser(@RequestHeader("Authorization") String authHeader, @PathVariable Integer idUser){
        User currentUser = AuthToken.isValid(databaseService, authHeader);
        User rescuedUser = databaseService.getUserById(idUser);

        if (currentUser == null || (!currentUser.hasPrivilege(autoHardPrivilege.READ_USER) && !currentUser.getUsername().equals(rescuedUser.getUsername()))){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(rescuedUser, HttpStatus.OK);
    }

    @PostMapping("/user")
    public ResponseEntity<User> createUser(@RequestHeader("Authorization") String authHeader, @RequestBody User user){
        User currentUser = AuthToken.isValid(databaseService, authHeader);

        if (currentUser == null || !currentUser.hasPrivilege(autoHardPrivilege.WRITE_USER)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (user.getUsername() == null || user.getRole() == null || user.getRole().getId() == null || user.getPassword() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (databaseService.getUserByName(user.getUsername()) != null){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Role rescuedRole = databaseService.getRoleById(user.getRole().getId());

        if (rescuedRole == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(databaseService.saveUser(new User(user.getUsername(), passwordEncoder.encode(user.getPassword()), rescuedRole)), HttpStatus.OK);
    }

    @DeleteMapping("/user/{idUser}")
    public ResponseEntity<Void> deleteUser(@RequestHeader("Authorization") String authHeader, @PathVariable Integer idUser){
        User currentUser = AuthToken.isValid(databaseService, authHeader);

        if (currentUser == null || !currentUser.hasPrivilege(autoHardPrivilege.WRITE_USER)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        User rescuedUser = databaseService.getUserById(idUser);

        if (rescuedUser == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (rescuedUser.getUsername().equals("admin")){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        databaseService.deleteUser(rescuedUser);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/user")
    public ResponseEntity<User> modifyUser(@RequestHeader("Authorization") String authHeader, @RequestBody User user){
        User currentUser = AuthToken.isValidAndModifiable(databaseService, authHeader);

        if (currentUser == null || !currentUser.hasPrivilege(autoHardPrivilege.WRITE_USER)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (user.getId() == null || user.getUsername() == null || user.getRole() == null || user.getRole().getId() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (databaseService.getUserByName(user.getUsername()) != null && !databaseService.getUserByName(user.getUsername()).getId().equals(user.getId())){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        User rescuedUser = databaseService.getUserById(user.getId());

        if (rescuedUser == null || databaseService.getRoleById(user.getRole().getId()) == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (rescuedUser.getUsername().equals("admin")){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(databaseService.saveUser(rescuedUser.update(passwordEncoder, user)), HttpStatus.OK);
    }
}
