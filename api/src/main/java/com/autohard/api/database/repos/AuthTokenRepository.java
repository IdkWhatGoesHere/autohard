package com.autohard.api.database.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.autohard.api.models.session.AuthToken;

@Repository
public interface AuthTokenRepository extends JpaRepository<AuthToken, Integer>{
    public AuthToken getByTokenValue(String tokenValue);
    public void deleteByTokenValue(String tokenValue);
}
