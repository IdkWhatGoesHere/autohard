package com.autohard.api.configuration;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.autohard.api.database.DatabaseService;
import com.autohard.api.models.OperatingSystem;
import com.autohard.api.models.Playbook;
import com.autohard.api.models.Playbook.playbookType;
import com.autohard.api.models.session.Role;
import com.autohard.api.models.session.Role.autoHardPrivilege;
import com.autohard.api.models.session.User;

@Component
public class Initializer implements ApplicationRunner{

    private DatabaseService databaseService;
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(Initializer.class);

    private static final String ADMIN_USER = "admin";

    @Autowired
    public Initializer(DatabaseService databaseService, PasswordEncoder passwordEncoder){
        this.databaseService = databaseService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        /*
         * INTIIALIZE DATABASE
         */

        /*
         * CREATE ADMIN USER
         */

        Role adminRole = databaseService.saveRole(new Role(ADMIN_USER, List.of(autoHardPrivilege.values())));
        databaseService.saveUser(new User(ADMIN_USER, passwordEncoder.encode(ADMIN_USER), adminRole));

        /*
         * CREATE OPERATING SYSTEMS (temporary)
         */

        OperatingSystem os1 = new OperatingSystem("Linux", "Ubuntu", "22.04");
        OperatingSystem os2 = new OperatingSystem("Windows", "10", "Pro_Independent_Client");
        OperatingSystem os3 = new OperatingSystem("Linux", "Suse", "SP5");

        OperatingSystem rescuedOs = databaseService.saveOperatingSystem(os1);
        databaseService.saveOperatingSystem(os2);
        databaseService.saveOperatingSystem(os3);

        /*
         * CREATE PLAYBOOKS (temporary)
         */

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get("/etc/autohard/playbooks"))) {
            for (Path file : stream) {
                if (Files.isRegularFile(file)) { 
                    String fileName = file.getFileName().toString();
                    databaseService.savePlaybook(new Playbook(rescuedOs, fileName.substring(0, fileName.length() - 4), playbookType.HARDENING));
                }
            }
        } catch (Exception e) {
            logger.error("Error while importing playbooks to the database");
            logger.error(e.getMessage());
        }
    }
    
    
}
