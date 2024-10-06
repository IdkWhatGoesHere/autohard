package com.autohard.api.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "playbooks")
public class Playbook {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
}
