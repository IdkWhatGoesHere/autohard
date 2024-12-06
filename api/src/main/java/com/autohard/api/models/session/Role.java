package com.autohard.api.models.session;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public class Role {

    public enum autoHardPrivilege{
        WRITE_JOB,
        READ_JOB,
        EXECUTE_JOB,
        WRITE_USER,
        READ_USER,
        WRITE_ROLE,
        READ_ROLE,
        READ_OPERATING_SYSTEM,
        READ_PLAYBOOK,
        WRITE_NODE,
        READ_NODE,
        READ_EXECUTION
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "name")
    private String name;

    @ElementCollection
    @Column(name = "privileges")
    private List<autoHardPrivilege> privileges;

    public Role(){
        super();
    }

    public Role(String name, List<autoHardPrivilege> privileges){
        this.name = name;
        this.privileges = privileges;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<autoHardPrivilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<autoHardPrivilege> privileges) {
        this.privileges = privileges;
    }

    
}
