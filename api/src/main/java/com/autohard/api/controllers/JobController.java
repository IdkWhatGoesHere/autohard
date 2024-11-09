package com.autohard.api.controllers;

import java.util.Date;
import java.util.List;
import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.autohard.api.models.Execution;
import com.autohard.api.models.Job;
import com.autohard.api.models.Node;
import com.autohard.api.models.Playbook;
import com.autohard.api.models.Execution.execState;
import com.autohard.api.models.session.AuthToken;
import com.autohard.api.models.session.Role.autoHardPrivilege;
import com.autohard.api.models.session.User;

import jakarta.transaction.Transactional;

@Transactional
@RestController
public class JobController {

    private final DatabaseService databaseService;

    private static final Logger logger = LoggerFactory.getLogger(JobController.class);

    @Autowired
    public JobController(DatabaseService databaseService){
        this.databaseService = databaseService;
    }

    @GetMapping("/job")
    public ResponseEntity<List<Job>> getJobs(@RequestHeader("Authorization") String authHeader){
        User currentUser = AuthToken.isValid(databaseService, authHeader);

        if (currentUser == null || !currentUser.hasPrivilege(autoHardPrivilege.READ_JOB)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(databaseService.getAllJobs(), HttpStatus.OK);
    }

    @GetMapping("/job/{idJob}")
    public ResponseEntity<Job> getJob(@RequestHeader("Authorization") String authHeader, @PathVariable Integer idJob){
        User currentUser = AuthToken.isValid(databaseService, authHeader);

        if (currentUser == null || !currentUser.hasPrivilege(autoHardPrivilege.READ_JOB)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Job rescuedJob = databaseService.getJobById(idJob);

        if (rescuedJob == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(rescuedJob, HttpStatus.OK);
    }

    @PostMapping("/job")
    public ResponseEntity<Job> createJob(@RequestHeader("Authorization") String authHeader, @RequestBody Job job){
        User currentUser = AuthToken.isValid(databaseService, authHeader);

        if (currentUser == null || !currentUser.hasPrivilege(autoHardPrivilege.WRITE_JOB)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (job.getName() == null || job.getPlaybook() == null || job.getPlaybook().getId() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Playbook rescuedPb = databaseService.getPlaybookById(job.getPlaybook().getId());

        if ( rescuedPb == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Job createdJob = new Job(job.getName(), rescuedPb, currentUser);

        return new ResponseEntity<>(databaseService.saveJob(createdJob), HttpStatus.OK);
    }

    @PutMapping("/job")
    public ResponseEntity<Job> modifyJob(@RequestHeader("Authorization") String authHeader, @RequestBody Job job){
        User currentUser = AuthToken.isValid(databaseService, authHeader);

        if (currentUser == null || !currentUser.hasPrivilege(autoHardPrivilege.WRITE_JOB)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (job.getId() == null || job.getName() == null || job.getPlaybook() == null || job.getPlaybook().getId() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Playbook rescuedPb = databaseService.getPlaybookById(job.getPlaybook().getId());

        if ( rescuedPb == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Job rescuedJob = databaseService.getJobById(job.getId());
        rescuedJob.setName(job.getName());
        rescuedJob.setPlaybook(rescuedPb);

        return new ResponseEntity<>(databaseService.saveJob(rescuedJob), HttpStatus.OK);
    }

    @DeleteMapping("/job/{idJob}")
    public ResponseEntity<Job> deleteJob(@RequestHeader("Authorization") String authHeader, @PathVariable Integer idJob){
        User currentUser = AuthToken.isValid(databaseService, authHeader);

        if (currentUser == null || !currentUser.hasPrivilege(autoHardPrivilege.WRITE_JOB)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (databaseService.getJobById(idJob) == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        databaseService.deleteJobById(idJob);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/job/{idJob}/execute")
    public ResponseEntity<Execution> executeJob(@RequestHeader("Authorization") String authHeader, @PathVariable Integer idJob){
        User currentUser = AuthToken.isValid(databaseService, authHeader);

        if (currentUser == null || !currentUser.hasPrivilege(autoHardPrivilege.EXECUTE_JOB)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Job rescuedJob = databaseService.getJobById(idJob);

        if (rescuedJob == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String playbookPath = Playbook.PLAYBOOK_BASE_PATH + rescuedJob.getPlaybook().getName();
        String inventoryPath = "/tmp/autoHardInventory" + System.currentTimeMillis();
        String outputPath = "/tmp/autoHardOutput" + System.currentTimeMillis();

        try{
            rescuedJob.buildInventory(inventoryPath);
        }catch (IOException e){
            logger.error("An error ocurred when creating the inventory file for the execution");
            logger.error(e.getMessage());

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try{
            ProcessBuilder builder = new ProcessBuilder("ansible-playbook", playbookPath, "-i ", inventoryPath);
            builder.redirectOutput(new File(outputPath));
            builder.redirectError(new File(outputPath));

            builder.start();
        } catch (IOException e){
            logger.error("An error ocurred when executing the ansible playbook in the system\'s shell");
            logger.error(e.getMessage());

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }        

        Execution exec = new Execution(new Date(System.currentTimeMillis()), rescuedJob, execState.RUNNING, outputPath, inventoryPath);

        return new ResponseEntity<>(databaseService.saveExecution(exec), HttpStatus.OK);
    }

    @GetMapping("/job/{idJob}/grant/{idUser}")
    public ResponseEntity<Job> grantPermissionsOnJob(@RequestHeader("Authorization") String authHeader, @PathVariable Integer idJob, @PathVariable Integer idUser){
        User currentUser = AuthToken.isValid(databaseService, authHeader);

        if (currentUser == null || !currentUser.hasPrivilege(autoHardPrivilege.WRITE_JOB)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Job rescuedJob = databaseService.getJobById(idJob);
        User rescuedUser = databaseService.getUserById(idUser);

        if (rescuedJob == null || rescuedUser == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (rescuedJob.getAllowed().get(0).getUsername().equals(currentUser.getUsername())){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        rescuedJob.allowUser(rescuedUser);
        
        return new ResponseEntity<>(databaseService.saveJob(rescuedJob), HttpStatus.OK);
    }

    @GetMapping("/job/{idJob}/revoke/{idUser}")
    public ResponseEntity<Job> revokePermissionsOnJob(@RequestHeader("Authorization") String authHeader, @PathVariable Integer idJob, @PathVariable Integer idUser){
        User currentUser = AuthToken.isValid(databaseService, authHeader);

        if (currentUser == null || !currentUser.hasPrivilege(autoHardPrivilege.WRITE_JOB)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Job rescuedJob = databaseService.getJobById(idJob);
        User rescuedUser = databaseService.getUserById(idUser);

        if (rescuedJob == null || rescuedUser == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (rescuedJob.getAllowed().get(0).getUsername().equals(currentUser.getUsername())){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (!rescuedJob.getAllowed().contains(rescuedUser)){
            return new ResponseEntity<>(HttpStatus.OK); 
        }

        rescuedJob.revokeUser(rescuedUser);
        
        return new ResponseEntity<>(databaseService.saveJob(rescuedJob), HttpStatus.OK);
    }

    @GetMapping("/job/{idJob}/node/{idNode}")
    public ResponseEntity<Job> addNodeToJob(@RequestHeader("Authorization") String authHeader, @PathVariable Integer idJob, @PathVariable Integer idNode){
        User currentUser = AuthToken.isValid(databaseService, authHeader);

        if (currentUser == null || !currentUser.hasPrivilege(autoHardPrivilege.WRITE_JOB)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Node rescuedNode = databaseService.getNodeById(idNode);
        Job rescuedJob = databaseService.getJobById(idJob);

        if (rescuedJob == null || rescuedNode == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        rescuedJob.addNode(rescuedNode);

        return new ResponseEntity<>(databaseService.saveJob(rescuedJob), HttpStatus.OK);
    }

}
