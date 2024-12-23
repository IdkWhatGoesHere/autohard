package com.autohard.api.models;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.autohard.api.models.session.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "playbook_id", referencedColumnName = "id")
    private Playbook playbook;

    @ElementCollection
    private List<Node> nodes = new ArrayList<>();

    @ElementCollection
    private List<User> allowed = new ArrayList<>();

    public Job(){
        super();
    }

    public Job(String name, Playbook playbook, User owner) {
        this.name = name;
        this.playbook = playbook;
        this.allowed.add(owner);
    }



    /*
     * METHODS
     */

    public void buildInventory(String inventoryPath) throws IOException{
        File inventoryFile = new File(inventoryPath);
        FileWriter writer = new FileWriter(inventoryFile);

        for (Node node : this.nodes){
            writer.write(node.getIp() + " ansible_user=" + node.getUsername() + 
                                        " ansible_password=" + node.getPassword() + 
                                        " ansible_become_password=" + node.getPassword() + 
                                        " ansible_ssh_extra_args=\'-o stricthostkeychecking=no\'" + System.getProperty("line.separator"));
        }

        writer.close();
    }

    public void allowUser(User user){
        this.allowed.add(user);
    }

    public void revokeUser(User user){
        if (!isOwner(user)){
            for (User person : this.allowed){
                if (person.getId().equals(user.getId())){
                    this.allowed.remove(person);
                }
            }
        }
    }

    public boolean isAllowed(User user){
        for (User person : this.allowed){
            if (person.getId().equals(user.getId())){
                return true;
            }
        }

        return false;
    }

    public boolean isOwner(User user){
        return this.allowed.get(0).getId().equals(user.getId());
    }

    public void addNode(Node node){
        this.nodes.add(node);
    }

    public void deleteNode(Node node){
        for (Node element : this.nodes){
            if (element.getId().equals(node.getId())){
                this.nodes.remove(element);
            }
        }
    }

    /*
     * GETTERS AND SETTERS
     */

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Playbook getPlaybook() {
        return playbook;
    }

    public void setPlaybook(Playbook playbook) {
        this.playbook = playbook;
    }

    @JsonIgnore
    public List<Node> getNodes(){
        return this.nodes;
    }

    public List<String> getAddedNodes(){
        List<String> addedNodes = new ArrayList<>();

        for (Node node : nodes){
            addedNodes.add(node.getIp());
        }

        return addedNodes;
    }

    public void setNodes(List<Node> nodes){
        this.nodes = nodes;
    }

    @JsonIgnore
    public List<User> getAllowed(){
        return allowed;
    }

    public List<String> getAllowedUsers() {
        List<String> allowedUsers = new ArrayList<>();

        for (User user : this.allowed){
            allowedUsers.add(user.getUsername());
        }

        return allowedUsers;
    }
}
