package com.autohard.api.database.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.autohard.api.models.session.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{
    public Role findByName(String name);
}
