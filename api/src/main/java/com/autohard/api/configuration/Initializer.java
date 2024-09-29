package com.autohard.api.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.autohard.api.database.DatabaseService;
import com.autohard.api.models.session.Role;
import com.autohard.api.models.session.Role.autoHardPrivilege;
import com.autohard.api.models.session.User;

@Component
public class Initializer implements ApplicationRunner{

    private DatabaseService databaseService;
    private PasswordEncoder passwordEncoder;

    private static final String ADMIN_USER = "admin";

    @Autowired
    public Initializer(DatabaseService databaseService, PasswordEncoder passwordEncoder){
        this.databaseService = databaseService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Role adminRole = databaseService.saveRole(new Role(ADMIN_USER, List.of(autoHardPrivilege.values())));
        databaseService.saveUser(new User(ADMIN_USER, passwordEncoder.encode(ADMIN_USER), adminRole));
    }
    
    
}
