package com.autohard.api.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "executions")
public class Execution {

    public enum execState{
        RUNNING,
        FINISHED
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "execution_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date executionDate;

    @Column(name = "output")
    private String output;

    @Column(name = "state")
    private execState state;

    @Column(name = "output_file_path")
    private String outputFilePath;

    @OneToOne
    @JoinColumn(name = "job_id", referencedColumnName = "id")
    private Job job;

    public Execution(Date executionDate, Job job, execState state, String outputfile) {
        this.executionDate = executionDate;
        this.job = job;
        this.state = state;
        this.outputFilePath = outputfile;
    }

    /*
     * GETTERS AND SETTERS
     */

    public Integer getId() {
        return id;
    }

    public Date getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Date executionDate) {
        this.executionDate = executionDate;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public execState getState(){
        return this.state;
    }

    public void setState(execState state){
        this.state = state;
    }

    @JsonIgnore
    public String getOutputFilePath(){
        return this.outputFilePath;
    }
}
