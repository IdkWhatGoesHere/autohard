package com.autohard.api.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Node {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
 
    @Column(name = "hostname")
    private String hostname;

    @Column(name = "ip")
    private String ip;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "operating_system_id", referencedColumnName = "id")
    private OperatingSystem operatingSystem;

    public Node(String hostname, String ip, String username, String password, OperatingSystem operatingSystem) {
        this.hostname = hostname;
        this.ip = ip;
        this.username = username;
        this.password = password;
        this.operatingSystem = operatingSystem;
    }

    /*
     * GETTERS AND SETTERS
     */

    public Integer getId() {
        return id;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public OperatingSystem getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(OperatingSystem operatingSystem) {
        this.operatingSystem = operatingSystem;
    }    
}
