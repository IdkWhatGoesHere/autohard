package com.autohard.api.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autohard.api.database.repos.AuthTokenRepository;
import com.autohard.api.database.repos.ExecutionRepository;
import com.autohard.api.database.repos.JobRepository;
import com.autohard.api.database.repos.NodeRepository;
import com.autohard.api.database.repos.OperatingSystemRepository;
import com.autohard.api.database.repos.PlaybookRepository;
import com.autohard.api.database.repos.RoleRepository;
import com.autohard.api.database.repos.UserRepository;
import com.autohard.api.models.Execution;
import com.autohard.api.models.Job;
import com.autohard.api.models.Node;
import com.autohard.api.models.OperatingSystem;
import com.autohard.api.models.Playbook;
import com.autohard.api.models.Execution.execState;
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
    private NodeRepository nodeRepository;
    private PlaybookRepository playbookRepository;
    private ExecutionRepository executionRepository;

    @Autowired
    public DatabaseService(JobRepository jobRepository, UserRepository userRepository, RoleRepository roleRepository,
            AuthTokenRepository authTokenRepository, OperatingSystemRepository operatingSystemRepository,
            NodeRepository nodeRepository, PlaybookRepository playbookRepository, ExecutionRepository executionRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authTokenRepository = authTokenRepository;
        this.operatingSystemRepository = operatingSystemRepository;
        this.nodeRepository = nodeRepository;
        this.playbookRepository = playbookRepository;
        this.executionRepository = executionRepository;
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
        return this.jobRepository.findById(id).orElse(null);
    }

    public void deleteJobById(Integer id){
        this.jobRepository.deleteById(id);
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

    public List<AuthToken> getAllAuthTokens(){
        return authTokenRepository.findAll();
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

    public OperatingSystem saveOperatingSystem(OperatingSystem os){
        return this.operatingSystemRepository.save(os);
    }

    /*
     * NODE OPERATIONS
     */

    public List<Node> getAllNodes(){
        return this.nodeRepository.findAll();
    }

    public Node getNodeById(Integer id){
        return this.nodeRepository.findById(id).orElse(null);
    }

    public Node saveNode(Node node){
        return this.nodeRepository.save(node);
    }

    public void deleteNodeById(Integer id){
        this.nodeRepository.deleteById(id);
    }

    /*
     * PLAYBOOK OPERATIONS
     */

    public List<Playbook> getAllPlaybooks(){
        return this.playbookRepository.findAll();
    }

    public Playbook getPlaybookById(Integer id){
        return this.playbookRepository.findById(id).orElse(null);
    }

    public Playbook savePlaybook(Playbook pb){
        return this.playbookRepository.save(pb);
    }

    /*
     * EXECUTION OPERATIONS
     */

    public Execution saveExecution(Execution exec){
        return this.executionRepository.save(exec);
    }

    public List<Execution> getAllRunningExecutions(){
        List<Execution> total = this.executionRepository.findAll();
        List<Execution> result = new ArrayList<>();

        for (Execution execution : total){
            if (execution.getState().equals(execState.RUNNING)){
                result.add(execution);
            }
        }

        return result;
    }

    public Execution getExecutionById(Integer id){
        return this.executionRepository.findById(id).orElse(null);
    }

    public List<Execution> getAllExecutionsByJob(Job job){
        return this.executionRepository.findByJob(job);
    }
}

