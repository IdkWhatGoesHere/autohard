package com.autohard.api.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.autohard.api.database.DatabaseService;
import com.autohard.api.models.session.User;

@Component
public class ScheduledRunner {

    private final DatabaseService databaseService;

    @Autowired
    public ScheduledRunner(DatabaseService databaseService){
        this.databaseService = databaseService;
    }

    @Scheduled(fixedRate = 1800000)
    public void updateUsersPasswordExpiration(){
        List<User> users = databaseService.getAllUsers();

        for (User user : users){
            if (user.isCredentialExpired()){
                user.setLocked(true);
                databaseService.saveUser(user);
            }
        }
    }
}
