package com.autohard.api.database;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autohard.api.database.repos.AuthTokenRepository;
import com.autohard.api.database.repos.JobRepository;
import com.autohard.api.database.repos.OperatingSystemRepository;
import com.autohard.api.database.repos.RoleRepository;
import com.autohard.api.database.repos.UserRepository;
import com.autohard.api.models.Job;
import com.autohard.api.models.OperatingSystem;
import com.autohard.api.models.session.AuthToken;
import com.autohard.api.models.session.Role;
import com.autohard.api.models.session.User;

@Service
public class DatabaseService {

    private JobRepository jobRepository;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private AuthTokenRepository authTokenRepository;
    private OperatingSystemRepository operatingSystemRepository;

    @Autowired
    public DatabaseService(JobRepository jobRepository, UserRepository userRepository, RoleRepository roleRepository,
            AuthTokenRepository authTokenRepository, OperatingSystemRepository operatingSystemRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authTokenRepository = authTokenRepository;
        this.operatingSystemRepository = operatingSystemRepository;
    }

    /*
     * JOB OPERATIONS
     */

    public Job saveJob(Job job){
        return this.jobRepository.save(job);
    }

    public List<Job> getAllJobs(){
        return this.jobRepository.findAll();
    }

    public Job getJobById(Integer id){
        Optional<Job> rescuedJob = this.jobRepository.findById(id);
        if (rescuedJob.isPresent()){
            return rescuedJob.get();
        }else{
            return null;
        }
    }

    /*
     * USER OPERATIONS
     */

    public User saveUser(User user){
        return this.userRepository.save(user);
    }

    public void deleteUser(User user){
        this.userRepository.delete(user);
    }

    public List<User> getAllUsers(){
        return this.userRepository.findAll();
    }

    public User getUserById(Integer id){
        Optional<User> user = this.userRepository.findById(id);
        if (user.isPresent()){
            return user.get();
        }else{
            return null;
        }
    }

    public User getUserByName(String username){
        return this.userRepository.findByUsername(username);
    }

    /*
     * ROLE OPERATIONS
     */

    public Role saveRole(Role role){
        return roleRepository.save(role);
    }

    public Role getRoleById(Integer id){
        return roleRepository.findById(id).orElse(null);
    }

    public List<Role> getRoles(){
        return roleRepository.findAll();
    }

    public Role getRoleByName(String name){
        return roleRepository.findByName(name);
    }

    public void deleteRoleById(Integer id){
        roleRepository.deleteById(id);
    }

    public List<Role> getAllRoles(){
        return this.roleRepository.findAll();
    }

    /*
     * AUTH_TOKEN OPERATIONS
     */

    public AuthToken saveAuthToken(AuthToken token){
        return authTokenRepository.save(token);
    }

    public AuthToken getAuthTokenById(Integer id){
        return authTokenRepository.findById(id).orElse(null);
    }

    public AuthToken getAuthTokenByValue(String tokenValue){
        return authTokenRepository.getByTokenValue(tokenValue);
    }

    public void deleteAuthTokenByValue(String tokenValue){
        authTokenRepository.deleteByTokenValue(tokenValue);
    }

    /*
     * OPERATING_SYSTEM OPERATIONS
     */

    public List<OperatingSystem> getAllOperatingSystems(){
        return this.operatingSystemRepository.findAll();
    }

    public OperatingSystem getOperatingSystemById(Integer id){
        return this.operatingSystemRepository.findById(id).orElse(null);
    }
}

