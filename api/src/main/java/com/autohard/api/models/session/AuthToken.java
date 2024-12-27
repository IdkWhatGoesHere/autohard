package com.autohard.api.models.session;

import java.util.Date;

import com.autohard.api.database.DatabaseService;
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
@Table(name = "auth_token")
public class AuthToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "token_value")
    private String tokenValue;

    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public AuthToken(){
        super();
    }

    public AuthToken(String token, User user){
        this.tokenValue = token;
        this.user = user;
        this.dateCreated = new Date(System.currentTimeMillis());
    }

    public static User isValid(DatabaseService databaseService, String rawToken){
        String parsedToken = rawToken.substring(7);
        AuthToken rescuedToken = databaseService.getAuthTokenByValue(parsedToken);
        
        if (rescuedToken == null){
            return null;
        }

        if (rescuedToken.getUser().getLocked()){
            return null;
        }

        return rescuedToken.getUser();
    }

    public static User isValidAndModifiable(DatabaseService databaseService, String rawToken){
        String parsedToken = rawToken.substring(7);
        AuthToken rescuedToken = databaseService.getAuthTokenByValue(parsedToken);
        
        if (rescuedToken == null){
            return null;
        }

        return rescuedToken.getUser();
    }

    public boolean isExpired(){
        Date current = new Date(System.currentTimeMillis());
        Long difference =  current.getTime() - this.dateCreated.getTime();

        return  ((difference / 3600000) > 1);
    }
    @JsonIgnore
    public Integer getId() {
        return id;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @JsonIgnore
    public Date getDateCreated(){
        return this.dateCreated;
    }

    public void setDateCreated(Date date){
        this.dateCreated = date;
    }
}
