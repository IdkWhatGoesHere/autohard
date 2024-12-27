package com.autohard.api.models.session;

import java.util.Date;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.autohard.api.models.session.Role.autoHardPrivilege;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "Users", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    @Column(name = "date_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastPasswordUpdate;

    @Column(name = "locked")
    private boolean locked = false;

    /*
     * CONSTRUCTORS
     */

    public User(){
        super();
    }

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.lastPasswordUpdate = new Date(System.currentTimeMillis());
    }

    public boolean hasPrivilege(autoHardPrivilege priv){
        return this.role.getPrivileges().contains(priv);
    }

    public User update(PasswordEncoder passwordEncoder, User newUser){
        this.username = newUser.getUsername();

        if (newUser.getPassword() != null){
            if (!passwordEncoder.matches(newUser.getPassword(), password)){
                this.password = passwordEncoder.encode(newUser.getPassword());
                this.lastPasswordUpdate = new Date(System.currentTimeMillis());
                this.locked = false;
            }
        }

        this.role = newUser.getRole();

        return this;
    }

    /*
     *  GETTERS AND SETTERS
     */

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getLastPasswordUpdate() {
        return lastPasswordUpdate;
    }

    public void setLastPasswordUpdate(Date updateDate) {
        this.lastPasswordUpdate = updateDate;
    }

    public boolean getLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isCredentialExpired() {
        Date current = new Date(System.currentTimeMillis());
        Long difference =  current.getTime() - this.lastPasswordUpdate.getTime();

        return  ((difference / 86400000) > 90);
    }  
}
