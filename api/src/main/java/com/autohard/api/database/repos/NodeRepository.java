package com.autohard.api.database.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.autohard.api.models.Node;

@Repository
public interface NodeRepository extends JpaRepository<Node, Integer> {
    
}
