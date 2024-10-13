package com.autohard.api.models;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "playbooks")
public class Playbook {

    public enum playbookType{
        HARDENING,
        AUDITING
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "operating_system_id", referencedColumnName = "id")
    private OperatingSystem operatingSystem;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private playbookType type;

    @Column(name = "modules")
    private List<String> modules;

    @Transient
    private static final String PLAYBOOK_BASE_PATH = "/etc/autohard/playbooks/";

    public Playbook(OperatingSystem operatingSystem, String name, playbookType type, List<String> modules) {
        this.operatingSystem = operatingSystem;
        this.name = name;
        this.type = type;
        this.modules = modules;
    }
    
    /*
     * GETTERS AND SETTERS
     */

    public Integer getId() {
        return id;
    }

    public OperatingSystem getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(OperatingSystem operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public playbookType getType() {
        return type;
    }

    public void setType(playbookType type) {
        this.type = type;
    }

    public List<String> getModules() {
        return modules;
    }

    public void setModules(List<String> modules) {
        this.modules = modules;
    }

    
}
