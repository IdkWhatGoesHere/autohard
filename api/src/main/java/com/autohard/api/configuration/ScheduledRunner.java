package com.autohard.api.configuration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.autohard.api.database.DatabaseService;
import com.autohard.api.models.Execution;
import com.autohard.api.models.Execution.execState;
import com.autohard.api.models.session.User;

@Component
public class ScheduledRunner {

    private final DatabaseService databaseService;

    private static final Logger logger = LoggerFactory.getLogger(ScheduledRunner.class);

    @Autowired
    public ScheduledRunner(DatabaseService databaseService){
        this.databaseService = databaseService;
    }

    @Scheduled(fixedRate = 3600000)
    public void updateUsersPasswordExpiration(){
        List<User> users = databaseService.getAllUsers();

        for (User user : users){
            if (user.isCredentialExpired()){
                user.setLocked(true);
                databaseService.saveUser(user);
            }
        }
    }

    @Scheduled(fixedRate = 10000)
    public void updateExecutionState(){
        List<Execution> execs = databaseService.getAllExecutions();

        boolean finished = false;

        for(Execution exec : execs){
            try{
                finished = exec.outputIsFinished();
            }catch (FileNotFoundException e){
                logger.error("Error when retrieving the output file for execution {}. The file was not found.", exec.getId());
                exec.setState(execState.ERROR);

                databaseService.saveExecution(exec);
            }

            if (finished){
                exec.setState(execState.FINISHED);

                try{
                    Files.deleteIfExists(Paths.get(exec.getInventoryFilePath()));
                }catch (IOException e){
                    logger.error("Error when deleting the inventory file for execution {}", exec.getId());
                    logger.error(e.getMessage());
                }

                try{
                    exec.setOutput(Files.readString(Paths.get(exec.getOutputFilePath()), StandardCharsets.UTF_8));
                }catch (IOException e){
                    logger.error("Error when reading the output file for execution {}", exec.getId());
                    logger.error(e.getMessage());
                }

                databaseService.saveExecution(exec);
            }
        }
    }
}
